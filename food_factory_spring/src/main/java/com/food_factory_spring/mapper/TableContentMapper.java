package com.food_factory_spring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.food_factory_spring.entity.FlavourIngredients;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TableContentMapper extends BaseMapper<FlavourIngredients> {
}
