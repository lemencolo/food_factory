package com.food_factory_spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author caicl
 * @since 2025-01-16
 */
@Getter
@Setter
@TableName("ingredients")
@ApiModel(value = "Ingredients对象", description = "")
public class Ingredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("text")
    private String text;

    @TableField("counts")
    private Integer counts;

    @TableField("completed")
    private Boolean completed;

    @TableField("completed_time")
    private LocalDateTime completedTime;  // 使用 LocalDateTime 类型

    @TableField("create_time")
    private LocalDateTime createTime;     // 使用 LocalDateTime 类型
}
