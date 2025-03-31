package com.food_factory_spring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.mapper.FlavourIngredientMapper;
import com.food_factory_spring.service.IFlavourIngredientsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caicl
 * @since 2025-01-19
 */
@Service
public class FlavourIngredientsServiceImpl extends ServiceImpl<FlavourIngredientMapper, FlavourIngredients> implements IFlavourIngredientsService {
    @Override
    public void addflavourWithIngredients(String flavourName, Map<String, Double> ingredients) {
        for (Map.Entry<String, Double> entry : ingredients.entrySet()) {
            String ingredientName = entry.getKey();
            double weight = entry.getValue();

            // 查询当前 flavour_name 对应的最大 order_index 值
            QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("flavour_name", flavourName)
                    .orderByDesc("order_index")
                    .last("LIMIT 1");

            FlavourIngredients lastRecord = this.getOne(queryWrapper);

            int nextOrderIndex = (lastRecord != null && lastRecord.getOrderIndex() != null) ? lastRecord.getOrderIndex() + 1 : 1;

            // 插入新记录
            FlavourIngredients noodleIngredient = new FlavourIngredients(flavourName, ingredientName, weight);
            noodleIngredient.setOrderIndex(nextOrderIndex);
            this.save(noodleIngredient);
        }
    }

    @Override
    public List<FlavourIngredients> getFlavourIngredientsByFlavourName(String flavourName) {
        QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flavour_name", flavourName)
                .orderByAsc("order_index");

        return this.list(queryWrapper);
    }

    //在用
    public boolean deleteByName(String name) {
        return this.remove(new QueryWrapper<FlavourIngredients>().eq("flavour_name", name));
    }

    public List<FlavourIngredients> getIngredientsByFlavourName(String flavourName) {
        return this.baseMapper.selectList(new QueryWrapper<FlavourIngredients>()
                .eq("flavour_name", flavourName)
                .orderByAsc("order_index"));
    }
    @Autowired
    private FlavourIngredientMapper flavourIngredientMapper;

    @Transactional
    public String addFlavour(String flavourName, List<Map<String, Object>> ingredientsData) {
        // 检查风味名称是否已存在
        QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("flavour_name", flavourName);

        if (flavourIngredientMapper.selectCount(queryWrapper) > 0) {
            throw new IllegalArgumentException("Flavour already exists");
        }

        // 插入成分记录
        for (Map<String, Object> data : ingredientsData) {
            FlavourIngredients ingredient = new FlavourIngredients();
            ingredient.setFlavourName(flavourName);
            ingredient.setIngredientName((String) data.get("ingredient_name"));
            ingredient.setWeight(((Number) data.get("weight")).doubleValue());
            ingredient.setOrderIndex(((Number) data.get("order_index")).intValue());
            flavourIngredientMapper.insert(ingredient);
        }

        return "Flavour added successfully: " + flavourName;
    }

//    @Transactional
//    public String updateFlavour(String oldFlavourName, String newFlavourName, List<Map<String, Object>> updatedIngredients) {
//        // 查找旧的风味记录
//        QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("flavour_name", oldFlavourName);
//
//        // 检查新风味名称是否已存在（如果新名称与旧名称不同）
//        if (!oldFlavourName.equals(newFlavourName)) {
//            QueryWrapper<FlavourIngredients> checkNewNameWrapper = new QueryWrapper<>();
//            checkNewNameWrapper.eq("flavour_name", newFlavourName);
//            if (flavourIngredientMapper.selectCount(checkNewNameWrapper) > 0) {
//                throw new IllegalArgumentException("New flavour name already exists");
//            }
//
//            // 更新所有旧风味名称为新风味名称
//            flavourIngredientMapper.updateFlavourName(oldFlavourName, newFlavourName);
//        }
//
//        // 删除旧的成分记录
//        flavourIngredientMapper.delete(queryWrapper);
//
//        // 插入新的成分记录
//        for (Map<String, Object> data : updatedIngredients) {
//            String ingredientName = (String) data.get("ingredient_name");
//
//            // 检查是否存在相同组合的记录
//            QueryWrapper<FlavourIngredients> checkIngredientWrapper = new QueryWrapper<>();
//            checkIngredientWrapper.eq("flavour_name", newFlavourName)
//                    .eq("ingredient_name", ingredientName);
//            if (flavourIngredientMapper.selectCount(checkIngredientWrapper) == 0) {
//                FlavourIngredients ingredient = new FlavourIngredients();
//                ingredient.setFlavourName(newFlavourName);
//                ingredient.setIngredientName(ingredientName);
//                ingredient.setWeight(((Number) data.get("weight")).doubleValue());
//                ingredient.setOrderIndex(((Number) data.get("order_index")).intValue());
//                flavourIngredientMapper.insert(ingredient);
//            } else {
//                throw new IllegalArgumentException("Ingredient '" + ingredientName + "' already exists for flavour '" + newFlavourName + "'");
//            }
//        }
//
//        return "Flavour updated successfully: " + newFlavourName;
//    }
@Transactional
public String updateFlavour(String oldFlavourName, String newFlavourName, List<Map<String, Object>> updatedIngredients) {
    if (oldFlavourName == null || oldFlavourName.trim().isEmpty()) {
        // 插入新的成分记录
        updated(newFlavourName, updatedIngredients);

        return "Flavour added successfully: " + newFlavourName;
    }

        // 查找旧的风味记录
    QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("flavour_name", oldFlavourName);

    // 检查新风味名称是否已存在（如果新名称与旧名称不同）
    if (!oldFlavourName.equals(newFlavourName)) {
        QueryWrapper<FlavourIngredients> checkNewNameWrapper = new QueryWrapper<>();
        checkNewNameWrapper.eq("flavour_name", newFlavourName);
        if (flavourIngredientMapper.selectCount(checkNewNameWrapper) > 0) {
            throw new IllegalArgumentException("New flavour name already exists");
        }
    }

    // 删除旧的成分记录
    int deletedRows = flavourIngredientMapper.delete(queryWrapper);
    System.out.println(deletedRows + " 条旧的成分记录被删除");

    // 插入新的成分记录
    updated(newFlavourName, updatedIngredients);

    return "Flavour updated successfully: " + newFlavourName;
}

    private void updated(String newFlavourName, List<Map<String, Object>> updatedIngredients) {
        for (Map<String, Object> data : updatedIngredients) {
            String ingredientName = (String) data.get("ingredient_name");

            FlavourIngredients ingredient = new FlavourIngredients();
            ingredient.setFlavourName(newFlavourName);
            ingredient.setIngredientName(ingredientName);
            ingredient.setWeight(((Number) data.get("weight")).doubleValue());
            ingredient.setOrderIndex(((Number) data.get("order_index")).intValue());

            // 直接插入新的成分记录
            try {
                flavourIngredientMapper.insert(ingredient);
            } catch (Exception e) {
                throw new IllegalArgumentException("Ingredient '" + ingredientName + "' already exists for flavour '" + newFlavourName + "'", e);
            }
        }
    }
    @Autowired
    private FlavourIngredientMapper flavourIngredientsMapper;

    /**
     * 获取所有不同的 flavourName
     *
     * @return 所有不同的 flavourName 集合
     */
    public Set<String> getAllDistinctFlavourNames() {
        QueryWrapper<FlavourIngredients> queryWrapper = Wrappers.<FlavourIngredients>query().select("DISTINCT flavour_name");
        List<Object> flavourNames = flavourIngredientsMapper.selectObjs(queryWrapper);
        return flavourNames.stream()
                .map(obj -> (String) obj)
                .collect(Collectors.toSet());
    }
}
