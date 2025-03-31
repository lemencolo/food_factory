package com.food_factory_spring.mapper;

import com.food_factory_spring.entity.FlavourIngredients;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author caicl
 * @since 2025-01-19
 */
@Mapper
public interface FlavourIngredientMapper extends BaseMapper<FlavourIngredients> {

    @Update("UPDATE flavour_ingredients SET flavour_name = #{newFlavourName} WHERE flavour_name = #{oldFlavourName}")
    void updateFlavourName(String oldFlavourName, String newFlavourName);
}
