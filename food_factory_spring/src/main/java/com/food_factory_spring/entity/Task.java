package com.food_factory_spring.entity;


import lombok.Data;

@Data
public class Task {
    private Long id;
    private String taskName;
    private Integer taskCount;
    private Integer completedCount;
    private Integer taskIndex;
    private Boolean completed;
    public Integer getCompletedCount() {
        return completedCount;
    }
    public void setCompletedCount(Integer completedCount) {
        this.completedCount = completedCount; 
    }
}
