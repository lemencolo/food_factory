package com.food_factory_spring.entity;


import lombok.Data;

@Data
public class Compare {
    private String taskName;
    private String ocrName;
    private float taskCount;
    private float comCount;
}