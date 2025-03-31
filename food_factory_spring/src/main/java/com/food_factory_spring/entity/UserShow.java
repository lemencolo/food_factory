package com.food_factory_spring.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserShow {
    private Integer id;
    private String username;
    private Integer roleID;
    private Boolean isActive;
    private LocalDateTime created_at;  // 使用 LocalDateTime 类型
    private LocalDateTime updated_at;     // 使用 LocalDateTime 类型
}
