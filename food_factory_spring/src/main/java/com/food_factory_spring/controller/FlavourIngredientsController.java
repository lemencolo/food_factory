package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.entity.Flavour;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.entity.Ingredients;
import com.food_factory_spring.service.IFlavourIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caicl
 * @since 2025-01-19
 */
@CrossOrigin
@RestController
@RequestMapping("/flavourIngredients")
public class FlavourIngredientsController {

    @Autowired
    private IFlavourIngredientsService flavourIngredientsService;

//    @PostMapping("/add")
//    public Result addNoodleWithIngredients(@RequestParam String flavourName,
//                                           @RequestBody Map<String, Double> ingredients) {
//        try {
//            flavourIngredientsService.addflavourWithIngredients(flavourName, ingredients);
//            return Result.suc("Noodle and ingredients added successfully!");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Result.fail();
//        }
//    }

//    @PostMapping("/listPage")
//    public Result listPage(@RequestBody QueryPageParam query){
////        HashMap hashMap = query.getParm();
////        String name = (String)hashMap.get("name");
////        Integer sex = (Integer) hashMap.get("sex");
//
//        Page<FlavourIngredients> page = new Page<>();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//        LambdaQueryWrapper<FlavourIngredients> lambdaQueryWrapper = new LambdaQueryWrapper();
////        if (StringUtils.isNotBlank(name)) {
////            lambdaQueryWrapper.like(Ingredients::getId,name); // 匹配查询
////        }
////        if (sex != null) {
////            lambdaQueryWrapper.eq(Ingredients::getCompleted, sex);
////        }
//        lambdaQueryWrapper.orderByAsc(FlavourIngredients::getFlavourName)
//                .orderByAsc(FlavourIngredients::getOrderIndex);
//        IPage<FlavourIngredients> result =  flavourIngredientsService.page(page,lambdaQueryWrapper);
////        flavourIngredientsService.getFlavourIngredientsByFlavourName("鲜虾鱼板面");
//        return Result.suc(result.getRecords(), result.getTotal());
//    }

//    @PostMapping("/listPage1")
//    public Result listPage1(@RequestBody QueryPageParam query){
//        Page<FlavourIngredients> page = new Page<>();
//        page.setCurrent(query.getPageNum());
//        page.setSize(query.getPageSize());
//
//        LambdaQueryWrapper<FlavourIngredients> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        lambdaQueryWrapper.orderByAsc(FlavourIngredients::getFlavourName)
//                .orderByAsc(FlavourIngredients::getOrderIndex);
//
//        IPage<FlavourIngredients> result = flavourIngredientsService.page(page, lambdaQueryWrapper);
//
//        // 获取去重后的 FlavourName 列表
//        Set<String> distinctFlavourNames = result.getRecords().stream()
//                .map(FlavourIngredients::getFlavourName)
//                .collect(Collectors.toSet());
//
//        return Result.suc(new ArrayList<>(distinctFlavourNames), (long) distinctFlavourNames.size());
//    }
//    @GetMapping("/findByName")
//    public Result findByNo(@RequestParam String name){
//        System.out.println(name);
//        List list = flavourIngredientsService.lambdaQuery().eq(FlavourIngredients::getFlavourName,name).list();
//        return list.size()>0?Result.suc(list):Result.fail();
//    }
    // 删除
    @GetMapping("/delete")
    public Result delete(String name){
        return flavourIngredientsService.deleteByName(name)?Result.suc():Result.fail();
    }


//    @GetMapping("/all-grouped")
//    public Result getAllIngredientsGroupedByFlavour() {
//        List<FlavourIngredients> allIngredients = flavourIngredientsService.list();
////        System.out.println(allIngredients);
//        Flavour groupedIngredients = new Flavour();
//
//        for (FlavourIngredients ingredient : allIngredients) {
//            groupedIngredients.addIngredient(ingredient);
////            System.out.println(groupedIngredients);
//        }
//
//        if (groupedIngredients.getFlavours().isEmpty()) {
//            return Result.fail();
//        } else {
//            return Result.suc(groupedIngredients);
//        }
//    }
    @GetMapping("/all-grouped")
    public Result getAllIngredientsGroupedByFlavour() {
        List<FlavourIngredients> allIngredients = flavourIngredientsService.list();
        System.out.println(allIngredients);
        Flavour groupedIngredients = new Flavour();

        for (FlavourIngredients ingredient : allIngredients) {
            groupedIngredients.addIngredient(ingredient);
        }

        // 对每个风味的成分列表进行排序（例如按成分名称排序）
        for (String flavourName : groupedIngredients.getFlavours().keySet()) {
            groupedIngredients.sortIngredients(flavourName, Comparator.comparing(FlavourIngredients::getOrderIndex));
        }
        // 获取按风味名称排序的结果
        groupedIngredients.getSortedFlavours();
        if (groupedIngredients.getFlavours().isEmpty()) {
            return Result.fail();
        } else {
            return Result.suc(groupedIngredients);
        }
    }

//    @PostMapping("/add1")
//    public Result addFlavour(@RequestBody Map<String, Object> payload) {
//        String flavourName = (String) payload.get("flavour_name");
//        List<Map<String, Object>> ingredientsData = (List<Map<String, Object>>) payload.get("data");
//
//        try {
//            String result = flavourIngredientsService.addFlavour(flavourName, ingredientsData);
//            System.out.println(result);
//            return Result.suc(result);
//        } catch (IllegalArgumentException e) {
//            return Result.fail();
//        }
//    }

    @PutMapping("/addOrUpdate")
    public Result updateFlavour(@RequestBody Map<String, Object> payload) {
        String oldFlavourName = (String) payload.get("old_flavour_name");
        String newFlavourName = (String) payload.get("new_flavour_name");
        List<Map<String, Object>> ingredientsData = (List<Map<String, Object>>) payload.get("data");

        try {
            String result = flavourIngredientsService.updateFlavour(oldFlavourName, newFlavourName, ingredientsData);
            System.out.println(result);
            return Result.suc(result);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
            return Result.fail();
        }
    }

    @GetMapping("/distinct-names")
    public Result getDistinctFlavourNames() {
        return flavourIngredientsService.getAllDistinctFlavourNames().size()>0?Result.suc(flavourIngredientsService.getAllDistinctFlavourNames()):Result.fail();
    }

}
