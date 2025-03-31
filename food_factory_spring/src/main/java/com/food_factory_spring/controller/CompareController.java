package com.food_factory_spring.controller;


import com.food_factory_spring.common.Result;
import com.food_factory_spring.entity.Compare;
import com.food_factory_spring.entity.InputTask;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compare")
@CrossOrigin
public class CompareController {

    @PostMapping("/compare") // 修改为 POST 请求
    public Result compare(@RequestBody Compare compare) {

        String taskName = compare.getTaskName();
        String ocrName = compare.getOcrName();
        float taskCount = compare.getTaskCount();
        float comCount = compare.getComCount();
        System.out.println(taskName);
        System.out.println(ocrName);
        System.out.println(taskCount);
        System.out.println(comCount);

        // 输入校验
        if (taskName == null || ocrName == null || taskCount == 0 || comCount == 0) {
            return Result.fail();
        }
        // 字符串比较：使用 equals() 方法
        boolean namesMatch = taskName.equals(ocrName);

        // 浮点数比较：引入误差范围 epsilon
        final float EPSILON = 0.011f; // 定义误差范围
        System.out.println(Math.abs(taskCount - comCount));
        System.out.println(EPSILON);
        boolean countsMatch = Math.abs(taskCount - comCount) <= EPSILON;

        // 判断结果
        if (namesMatch && countsMatch) {
            return Result.suc(true); // 成功
        } else {
            return Result.fail(); // 失败
        }

    }

}