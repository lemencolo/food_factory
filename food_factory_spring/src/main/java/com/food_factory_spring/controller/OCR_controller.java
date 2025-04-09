package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food_factory_spring.common.StringSimilarity;
import com.food_factory_spring.common.TableNameGenerator;
import com.food_factory_spring.common.TableNameValidator;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.service.IFlavourIngredientsService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ocr")
@CrossOrigin
@EnableAsync
public class OCR_controller {

    // 注入 FlavourIngredientsService 用于查询数据库
    @Autowired
    private IFlavourIngredientsService flavourIngredientsService;

    // 预设有效关键词列表（动态从数据库获取）
    private static List<String> VALID_KEYWORDS;

    // 相似度阈值（超过此值则替换）
    private static final double SIMILARITY_THRESHOLD = 0;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 注入存储目录属性
    @Value("${ocr.storage.directory}")
    private String storageBaseDirectory;

    // 注入 OCR 服务的 URL
    @Value("${ocr.service.url}")
    private String ocrServiceUrl;

    @PostConstruct
    public void initializeValidKeywords() {
        // 查询 ingredient_name 列的去重值并初始化 VALID_KEYWORDS
        QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
        List<String> distinctIngredientNames = flavourIngredientsService.listObjs(queryWrapper.select("ingredient_name"))
                .stream()
                .map(Object::toString) // 转换为字符串
                .collect(Collectors.toSet()) // 使用 Set 去重
                .stream()
                .sorted() // 可选：对关键词进行排序
                .collect(Collectors.toList()); // 转换回 List

        VALID_KEYWORDS = distinctIngredientNames;
        System.out.println("初始化 VALID_KEYWORDS: " + VALID_KEYWORDS);
    }

    @PostMapping("/upload")
    public String ocr(@RequestParam("file") MultipartFile file) {
        try {
            ResponseEntity<String> response = callOcrService(file);
            String ocrResult = handleOcrResponse(response);
            asyncSaveFile(file);
            return ocrResult;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    private ResponseEntity<String> callOcrService(MultipartFile file) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        });
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        return restTemplate.exchange(
                ocrServiceUrl,
                HttpMethod.POST,
                requestEntity,
                String.class
        );
    }

    private String handleOcrResponse(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.OK) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> jsonMap = objectMapper.readValue(response.getBody(), Map.class);
                String textValue = (String) jsonMap.get("text");
                if (textValue != null) {
                    return textValue;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody());
        }
        return "";
    }

    @Async
    public CompletableFuture<Void> asyncSaveFile(MultipartFile file) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
            String dateFolderName = dateFormat.format(new Date());
            String fileName = timeFormat.format(new Date()) + ".png";
            Path storageDirectory = Paths.get(storageBaseDirectory, dateFolderName);
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
            }
            File imageFile = new File(storageDirectory.toFile(), fileName);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(file.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }

    // 校正文本：找到最相似的关键词
    private String correctText(String input) {
        double maxSimilarity = -1;
        String bestMatch = input;

        for (String keyword : VALID_KEYWORDS) {
            double similarity = StringSimilarity.similarity(input, keyword);
            System.out.println(input + keyword + similarity);
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                bestMatch = keyword;
            }
        }

        // 若相似度超过阈值则替换，否则保留原文本
        return maxSimilarity >= SIMILARITY_THRESHOLD ? bestMatch : input;
    }

    public String listPageSpecialQuery() {
        // 生成表名
        String tableName = TableNameGenerator.generateTableName();
//    tableName = "your_table_name"; // 示例中使用的实际表名，可以取消注释进行测试
        System.out.println("Generated table name: " + tableName);

        // 验证表名是否有效
        if (!TableNameValidator.isValidTableName(tableName)) { // 注意这里使用的是isValidTableName而不是C版本
            return "fail";
        }

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT ingredient_name FROM ").append(tableName)
                .append(" WHERE completed = 0")
                .append(" ORDER BY task_index ASC, order_index ASC LIMIT 1");

        System.out.println("Executing SQL: " + sqlBuilder.toString());

        // 执行查询
        List<Map<String, Object>> records;
        try {
            records = jdbcTemplate.queryForList(sqlBuilder.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }

        // 处理查询结果
        String ingredientName = null;
        if (records != null && !records.isEmpty()) {
            Map<String, Object> record = records.get(0);
            ingredientName = (String) record.get("ingredient_name");
        }

        // 返回结果
        if (ingredientName != null) {
            return ingredientName;
        } else {
            return "fail";
        }
    }
}