package com.food_factory_spring.common;
import org.mindrot.jbcrypt.BCrypt;


public class generate_Hash {

    public static void main(String[] args) {
        // 用户提供的原始密码
//        String rawPassword = "LiuLab123!";
        String rawPassword = "123456";
        // 生成哈希后的密码
        String hashedPassword = BCrypt.hashpw(rawPassword, BCrypt.gensalt(12)); // 设置工作因子为12
        System.out.println("Hashed Password: " + hashedPassword);
    }
}
