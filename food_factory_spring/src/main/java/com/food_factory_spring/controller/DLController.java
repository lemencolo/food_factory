package com.food_factory_spring.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("/dl")
@CrossOrigin
public class DLController {

    @PostMapping("/upload")
    public String ocr(@RequestParam("file") MultipartFile file) {
        return "Hello World";
        // try {
        //     // // 获取当前日期和时间
        //     // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //     // SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");

        //     // String dateFolderName = dateFormat.format(new Date());
        //     // String fileName = timeFormat.format(new Date()) + ".png";

        //     // // 创建存储目录
        //     // Path storageDirectory = Paths.get("/Users/user/code/food_factory/dl", dateFolderName);
        //     // if (!Files.exists(storageDirectory)) {
        //     //     Files.createDirectories(storageDirectory);
        //     // }

        //     // // 创建文件路径
        //     // File imageFile = new File(storageDirectory.toFile(), fileName);

        //     // // 将上传的图片保存到指定路径
        //     // try (FileOutputStream fos = new FileOutputStream(imageFile)) {
        //     //     fos.write(file.getBytes());
        //     // }

        //     // // 初始化OCR引擎
        //     // InferenceEngine engine = InferenceEngine.getInstance(Model.ONNX_PPOCR_V3);

        //     // // 执行OCR识别
        //     // OcrResult ocrResult = engine.runOcr(imageFile.getAbsolutePath());

        //     // // 返回OCR识别结果
        //     // return ocrResult.getStrRes().trim();
        //     return "Hello World";
        // } catch (IOException e) {
        //     e.printStackTrace();
        //     return "Error: " + e.getMessage();
        // }
    }
}