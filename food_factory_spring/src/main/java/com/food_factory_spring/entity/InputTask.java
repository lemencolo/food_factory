package com.food_factory_spring.entity;

import lombok.Data;

@Data
public class InputTask {
    private String taskName;
    private Integer taskCount;
    private Integer completedCount;
    public Integer getCompletedCount() {
        return completedCount; 
    }
    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount;
    }
}
