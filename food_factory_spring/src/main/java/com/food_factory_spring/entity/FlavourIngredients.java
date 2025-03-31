package com.food_factory_spring.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author caicl
 * @since 2025-01-19
 */
@Data
@TableName("flavour_ingredients")
@ApiModel(value = "FlavourIngredients对象", description = "")
public class FlavourIngredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "ID")
    private Long id;

    @TableField("flavour_name")
    @ApiModelProperty(value = "口味名称")
    private String flavourName;

    @TableField("ingredient_name")
    @ApiModelProperty(value = "配料名称")
    private String ingredientName;

    @TableField("weight")
    @ApiModelProperty(value = "重量")
    private Double weight;

    @TableField("order_index")
    @ApiModelProperty(value = "插入顺序")
    private Integer orderIndex;  // 新增字段用于表示插入顺序

    public FlavourIngredients() {}

    public FlavourIngredients(String flavourName, String ingredientName, Double weight) {
        this.flavourName = flavourName;
        this.ingredientName = ingredientName;
        this.weight = weight;
    }
}