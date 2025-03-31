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
 * @since 2025-01-28
 */
@Getter
@Setter
@TableName("menu")
@ApiModel(value = "Menu对象", description = "")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("menuCode")
    private String menuCode;

    @TableField("menuName")
    private String menuName;

    @TableField("menuLevel")
    private Byte menuLevel;

    @TableField("menuParentCode")
    private String menuParentCode;

    @TableField("menuClick")
    private String menuClick;

    @TableField("menuRight")
    private String menuRight;

    @TableField("menuComponent")
    private String menuComponent;

    @TableField("menuIcon")
    private String menuIcon;
}
