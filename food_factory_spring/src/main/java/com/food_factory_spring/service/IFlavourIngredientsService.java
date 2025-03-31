package com.food_factory_spring.service;

import com.food_factory_spring.entity.FlavourIngredients;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author caicl
 * @since 2025-01-19
 */
public interface IFlavourIngredientsService extends IService<FlavourIngredients> {

    void addflavourWithIngredients(String flavourName, Map<String, Double> ingredients);

    List<FlavourIngredients> getFlavourIngredientsByFlavourName(String flavourName);

    boolean deleteByName(String name);

    List<FlavourIngredients> getIngredientsByFlavourName(String flavourName);

    String addFlavour(String flavourName, List<Map<String, Object>> ingredientsData);

    String updateFlavour(String oldFlavourName, String newFlavourName, List<Map<String, Object>> ingredientsData);

    Set<String> getAllDistinctFlavourNames();
}
