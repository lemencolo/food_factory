package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.entity.InputTask;
import com.food_factory_spring.entity.Users;
import com.food_factory_spring.service.IFlavourIngredientsService;
import com.food_factory_spring.service.impl.TableService;
import com.food_factory_spring.service.impl.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/tableManage")
public class TableController {

    @Autowired
    private TableService tableService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private IFlavourIngredientsService flavourIngredientsService;

//    @GetMapping("/manage-table")
   private Result manageTable(@RequestParam String flavourName,
                          @RequestParam String ingredientName,
                          @RequestParam Double weight,
                          @RequestParam Integer orderIndex,
                          @RequestParam Integer taskIndex,
                           @RequestParam Integer task_count_number) {
    boolean success = false;
    String message = "";

    try {
        success = tableService.manageTable(flavourName, ingredientName, weight, orderIndex,taskIndex,task_count_number);
        message = "Table managed successfully.";
    } catch (Exception e) {
        message = "Failed to manage table: " + e.getMessage();
        return Result.fail();
    }

    return Result.suc(success);
}
    @PostMapping("/manage")
    public Result manageTask(@RequestBody InputTask inputTask) {
        String taskName = inputTask.getTaskName();
        Integer taskCount = inputTask.getTaskCount();
        Result finalResult = null;

        // 获取任务索引列表
        List<Integer> IsTaskCreated = taskService.manageTask(taskName, taskCount);

        // 如果任务创建失败，返回失败结果
        if (IsTaskCreated == null || IsTaskCreated.isEmpty()) {
            return Result.fail();
        }
        Integer task_group = IsTaskCreated.get(0);
        // 遍历任务索引列表
        for (Integer taskIndex : IsTaskCreated) {
            QueryWrapper<FlavourIngredients> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("flavour_name", taskName);
            System.out.println(queryWrapper);

            List<FlavourIngredients> flavourIngredientsList = flavourIngredientsService.list(queryWrapper);

            if (flavourIngredientsList.isEmpty()) {
                return Result.fail(); // 如果没有找到相关配料，返回失败结果
            }

            // 初始化结果变量
            finalResult = null;

            // 处理每个配料
            for (FlavourIngredients flavourIngredient : flavourIngredientsList) {
                String flavourName = flavourIngredient.getFlavourName();
                String ingredientName = flavourIngredient.getIngredientName();
                double totalWeight = flavourIngredient.getWeight();
                Integer orderIndex = flavourIngredient.getOrderIndex();

                // 调用 manageTable 方法
                Result result = manageTable(flavourName, ingredientName, totalWeight, orderIndex, taskIndex,task_group);

                // 更新最终结果
                finalResult = result;

            }
        }

        // 如果所有操作都成功，返回最终结果
        return finalResult != null ? finalResult : Result.fail();
    }


    @PostMapping("/updateTaskStatus")
    public Result updateTaskStatus(@RequestParam Integer task_id){
        return taskService.updateTaskStatus(task_id)?Result.suc():Result.fail();
    }
}
