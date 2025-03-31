package com.food_factory_spring.service.impl;

import com.food_factory_spring.entity.Ingredients;
import com.food_factory_spring.mapper.IngredientsMapper;
import com.food_factory_spring.service.IIngredientsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caicl
 * @since 2025-01-16
 */
@Service
public class IngredientsServiceImpl extends ServiceImpl<IngredientsMapper, Ingredients> implements IIngredientsService {

}
