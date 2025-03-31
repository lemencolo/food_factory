package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.entity.Ingredients;
import com.food_factory_spring.service.IIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caicl
 * @since 2025-01-16
 */
@RestController
@RequestMapping("/ingredients")
@CrossOrigin
public class IngredientsController {

    @Autowired
    private IIngredientsService ingredientsService;

    @PostMapping("/list")
    public Result list(){
        return Result.suc(ingredientsService.list());
    }

    @GetMapping("/list1")
    public List list1(){
        return ingredientsService.list();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query){
//        HashMap hashMap = query.getParm();
//        String name = (String)hashMap.get("name");
//        Integer sex = (Integer) hashMap.get("sex");

        Page<Ingredients> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());
        LambdaQueryWrapper<Ingredients> lambdaQueryWrapper = new LambdaQueryWrapper();
//        if (StringUtils.isNotBlank(name)) {
//            lambdaQueryWrapper.like(Ingredients::getId,name); // 匹配查询
//        }
//        if (sex != null) {
//            lambdaQueryWrapper.eq(Ingredients::getCompleted, sex);
//        }
        IPage<Ingredients> result =  ingredientsService.page(page,lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }
}
