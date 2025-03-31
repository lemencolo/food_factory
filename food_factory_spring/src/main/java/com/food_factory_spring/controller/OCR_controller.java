package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.benjaminwan.ocrlibrary.OcrResult;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.common.StringSimilarity;
import com.food_factory_spring.common.TableNameGenerator;
import com.food_factory_spring.common.TableNameValidator;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.service.IFlavourIngredientsService;
import io.github.mymonstercat.Model;
import io.github.mymonstercat.ocr.InferenceEngine;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//@RestController
//@RequestMapping("/ocr")
//@CrossOrigin
//public class OCR_controller {
//
//    private static final String API_KEY = "7GEDg6qgImibbX3hQV0zDv2f";
//    private static final String SECRET_KEY = "YXVTS7I9WJ38LkbKGScvKk3i7fh4czod";
//
//    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//
//    /**
//     * 接收前端上传的图片，并调用百度OCR API进行识别
//     *
//     * @param file 前端上传的图片文件
//     * @return OCR识别结果
//     */
////    @RequestParam("file") MultipartFile file
//    @PostMapping("/upload")
//    public String uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
//        // 获取Base64编码的图片数据
//        String base64ImageData = Base64.getEncoder().encodeToString(file.getBytes());
////        System.out.println(base64ImageData);
////        System.out.println("123");
//        // 调用百度OCR API
//        String res = callBaiduOCR(base64ImageData);
//        System.out.println(res);
//        // 提取并清理OCR识别结果
//        return extractAndCleanWords(res);
////        return "123";
//    }
//
//    /**
//     * 调用百度OCR API进行文字识别
//     *
//     * @param base64ImageData Base64编码的图片数据
//     * @return OCR识别结果
//     * @throws IOException IO异常
//     */
//    private String callBaiduOCR(String base64ImageData) throws IOException {
//        String accessToken = getAccessToken();
//
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "image=" + java.net.URLEncoder.encode(base64ImageData, "UTF-8") +
//                "&detect_direction=true&probability=false&detect_alteration=false");
//
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/rest/2.0/ocr/v1/handwriting?access_token=" + accessToken)
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .addHeader("Accept", "application/json")
//                .build();
//
//        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            return response.body().string();
//        }
//    }
//
//    /**
//     * 从用户的AK，SK生成鉴权签名（Access Token）
//     *
//     * @return 鉴权签名（Access Token）
//     * @throws IOException IO异常
//     */
//    private String getAccessToken() throws IOException {
//        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
//        RequestBody body = RequestBody.create(mediaType, "grant_type=client_credentials&client_id=" + API_KEY +
//                "&client_secret=" + SECRET_KEY);
//
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded")
//                .build();
//
//        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
//            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//
//            JSONObject jsonObject = new JSONObject(response.body().string());
//            return jsonObject.getString("access_token");
//        }
//    }
//    /**
//     * 从OCR识别结果中提取words字段并去除空格等
//     *
//     * @param ocrResult OCR识别结果的JSON字符串
//     * @return 清理后的OCR识别结果
//     */
//    private String extractAndCleanWords(String ocrResult) {
//        JSONObject jsonObject = new JSONObject(ocrResult);
//        JSONArray wordsResultArray = jsonObject.getJSONArray("words_result");
//
//        StringBuilder cleanedWords = new StringBuilder();
//
//        for (int i = 0; i < wordsResultArray.length(); i++) {
//            JSONObject wordObject = wordsResultArray.getJSONObject(i);
//            String word = wordObject.getString("words");
//
//            // 去除空格和其他不必要的字符
//            String cleanedWord = word.replaceAll("\\s+", ""); // 去除所有空白字符
//            cleanedWords.append(cleanedWord);
//        }
//
//        return cleanedWords.toString();
//    }
//}

//@RestController
//@RequestMapping("/ocr")
//@CrossOrigin
//public class OCR_controller {
//
//    @PostMapping("/upload")
//    public  String ocr(String[] args) {
//        InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);
//        OcrResult ocrResult = engine.runOcr("E:\\Desktop\\java_project\\food_factory\\paddle_ocr\\4.png");
//        System.out.println(ocrResult.getStrRes().trim());
//        return ocrResult.getStrRes().trim();
//    }
//
//}

@RestController
@RequestMapping("/ocr")
@CrossOrigin
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
            // 获取当前日期和时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");

            String dateFolderName = dateFormat.format(new Date());
            String fileName = timeFormat.format(new Date()) + ".png";

            // 创建存储目录
            Path storageDirectory = Paths.get("/Users/user/code/food_factory/paddle_ocr", dateFolderName);
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
            }

            // 创建文件路径
            File imageFile = new File(storageDirectory.toFile(), fileName);

            // 将上传的图片保存到指定路径
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(file.getBytes());
            }

            // 初始化OCR引擎
            InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);

            // 执行OCR识别
            OcrResult ocrResult = engine.runOcr(imageFile.getAbsolutePath());

            String ocrText = ocrResult.getStrRes().trim();

            // 校正识别结果
//            String correctedText = correctText(ocrText);

            return ocrText;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
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

    @PostMapping("/upload1s")
    public Result ocr_1s(@RequestParam("file") MultipartFile file) {
        try {
            String next_ingredient_name = listPageSpecialQuery();
            System.out.println(next_ingredient_name);

            // 获取当前日期和时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");

            String dateFolderName = dateFormat.format(new Date());
            String fileName = timeFormat.format(new Date()) + ".png";

            // 创建存储目录
            Path storageDirectory = Paths.get("/Users/user/code/food_factory/paddle_ocr_1s", dateFolderName);
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
            }

            // 创建文件路径
            File imageFile = new File(storageDirectory.toFile(), fileName);

            // 将上传的图片保存到指定路径
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(file.getBytes());
            }

            // 初始化OCR引擎
            InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);

            // 执行OCR识别
            OcrResult ocrResult = engine.runOcr(imageFile.getAbsolutePath());

            // 返回OCR识别结果
            String ocrText = ocrResult.getStrRes().trim();

            // 删除临时文件
            boolean deleted = imageFile.delete();
            if (!deleted) {
                return Result.fail();
            }
            if (ocrText.equals(next_ingredient_name)){
                return Result.suc("2000");
            }
            return Result.suc(ocrText) ;

        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail();
        }
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