package com.food_factory_spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
 * @since 2025-01-24
 */
@Getter
@Setter
@TableName("table_template")
@ApiModel(value = "TableTemplate对象", description = "")
public class TableTemplate implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("flavour_name")
    private String flavourName;

    @TableField("ingredient_name")
    private String ingredientName;

    @TableField("weight")
    private Double weight;

    @TableField("order_index")
    private Integer orderIndex;

    @TableField("task_index")
    private Integer taskIndex;

    @TableField("completed")
    private Boolean completed;
}
