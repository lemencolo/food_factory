//package com.food_factory_spring.controller;
//
//import ai.onnxruntime.OrtEnvironment;
//import ai.onnxruntime.OrtSession;
//import ai.onnxruntime.OrtSession.SessionOptions;
//import ai.onnxruntime.OnnxTensor;
//import jakarta.annotation.PreDestroy;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.FloatBuffer;
//import java.util.Collections;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/yolo")
//@CrossOrigin
//public class YoloController {
//
//    private static final Logger logger = LoggerFactory.getLogger(YoloController.class);
//
//    private OrtEnvironment env;
//    private OrtSession session;
//
//    @Value("${yolo.model.path:classpath:/best.onnx}")    private String modelPath;    @Value("${yolo.upload.dir:uploads/}")
//    private String uploadDir;
//
//    public YoloController() {
//        try {
//            if (modelPath == null || modelPath.isEmpty()) {
//                throw new IllegalArgumentException("Model path is not specified");
//            }
//
//            File modelFile;
//            if (modelPath.startsWith("classpath:")) {
//                ClassPathResource resource = new ClassPathResource(modelPath.substring("classpath:".length()));
//                modelFile = resource.getFile();
//            } else {
//                modelFile = new File(modelPath);
//            }
//
//            if (!modelFile.exists()) {
//                throw new FileNotFoundException("Model file does not exist at " + modelFile.getAbsolutePath());
//            }
//
//            SessionOptions options = new SessionOptions();
//            options.setExecutionMode(SessionOptions.ExecutionMode.PARALLEL);
//            options.addConfigEntry("session.set_denormal_as_zero", "1");
//            env = OrtEnvironment.getEnvironment();
//            session = env.createSession(modelFile.getAbsolutePath(), options);
//            logger.info("ONNX Runtime initialized successfully with model path: {}", modelFile.getAbsolutePath());
//        } catch (Exception e) {
//            logger.error("Failed to initialize ONNX Runtime", e);
//            throw new RuntimeException("Failed to initialize ONNX Runtime", e);
//        }
//    }
//
//    @PostMapping("/upload")
//    public String yoloInference(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return "Error: File is empty";
//        }
//        try {
//            String inputImagePath = saveImageToLocal(file);
//            float[] inputData = preprocessImage(inputImagePath);
//            long[] inputShape = {1, 3, 416, 416};
//            FloatBuffer inputBuffer = FloatBuffer.wrap(inputData);
//            OnnxTensor inputTensor = OnnxTensor.createTensor(env, inputBuffer, inputShape);
//            Map<String, Object> output = (Map<String, Object>) session.run(Collections.singletonMap("images", inputTensor));
//            String inferenceResult = processOutput(output);
//            return inferenceResult;
//        } catch (Exception e) {
//            logger.error("Error during YOLO inference", e);
//            return "Error: " + e.getMessage();
//        }
//    }
//
//    private String saveImageToLocal(MultipartFile file) throws IOException {
//        File directory = new File(uploadDir);
//        if (!directory.exists()) {
//            directory.mkdirs();
//        }
//        String fileName = UUID.randomUUID().toString() + ".jpg";
//        File imageFile = new File(directory, fileName);
//        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
//            fos.write(file.getBytes());
//        }
//        logger.info("Image saved to: {}", imageFile.getAbsolutePath());
//        return imageFile.getAbsolutePath();
//    }
//
//    private float[] preprocessImage(String imagePath) throws IOException {
//        BufferedImage image = ImageIO.read(new File(imagePath));
//        BufferedImage resizedImage = new BufferedImage(416, 416, BufferedImage.TYPE_3BYTE_BGR);
//        Graphics2D g = resizedImage.createGraphics();
//        g.drawImage(image, 0, 0, 416, 416, null);
//        g.dispose();
//        float[] inputData = new float[3 * 416 * 416];
//        int index = 0;
//        for (int y = 0; y < 416; y++) {
//            for (int x = 0; x < 416; x++) {
//                int rgb = resizedImage.getRGB(x, y);
//                inputData[index++] = ((rgb >> 16) & 0xFF) / 255.0f;
//                inputData[index++] = ((rgb >> 8) & 0xFF) / 255.0f;
//                inputData[index++] = (rgb & 0xFF) / 255.0f;
//            }
//        }
//        return inputData;
//    }
//
//    private String processOutput(Map<String, Object> output) {
//        StringBuilder resultBuilder = new StringBuilder();
//        try {
//            for (Map.Entry<String, Object> entry : output.entrySet()) {
//                OnnxTensor outputTensor = (OnnxTensor) entry.getValue();
//                float[][][] outputData = (float[][][]) outputTensor.getValue();
//                for (float[][] detection : outputData) {
//                    for (float[] box : detection) {
//                        float confidence = box[4];
//                        if (confidence > 0.5) {
//                            int classId = getHighestScoreClass(box);
//                            String className = getClassName(classId);
//                            resultBuilder.append("Class: ").append(className)
//                                    .append(", Confidence: ").append(confidence)
//                                    .append(", Box: [")
//                                    .append(box[0]).append(", ")
//                                    .append(box[1]).append(", ")
//                                    .append(box[2]).append(", ")
//                                    .append(box[3]).append("]")
//                                    .append(" ");
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            logger.error("Error processing YOLO output", e);
//        }
//        return resultBuilder.toString();
//    }
//
//    private int getHighestScoreClass(float[] box) {
//        int classId = 0;
//        float maxScore = box[5];
//        for (int i = 6; i < box.length; i++) {
//            if (box[i] > maxScore) {
//                maxScore = box[i];
//                classId = i - 5;
//            }
//        }
//        return classId;
//    }
//
//    private String getClassName(int classId) {
//        String[] classNames = {"xinyidai", "honghuajiao", "qinghuajiao", "wuxiangfen", "shengjiangshui",
//                "baitang", "weijing", "shiyan", "baishatang", "doubanjiang", "jiangmo", "suanmo", "suancai", "cibalajiao"};
//        return classNames[classId];
//    }
//
//    @PreDestroy
//    public void close() {
//        try {
//            if (session != null) session.close();
//            if (env != null) env.close();
//        } catch (Exception e) {
//            logger.error("Error closing ONNX Runtime resources", e);
//        }
//    }
//}