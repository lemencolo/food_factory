package com.food_factory_spring.controller;

import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.service.ITableTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author caicl
 * @since 2025-01-24
 */
@RestController
@RequestMapping("/tableTemplate")
@CrossOrigin
public class TableTemplateController {

    @Autowired
    private ITableTemplateService tableTemplateService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        try {
            // 调用Service层逻辑，进行分页查询
            return tableTemplateService.listPage(query);
        } catch (Exception e) {
            // 异常处理，返回错误信息
            return Result.fail();
        }
    }

    @PostMapping("/listPageC")
    public Result listPageC() {
        try {
            // 调用Service层逻辑，进行分页查询
            return tableTemplateService.listPageC();
        } catch (Exception e) {
            // 异常处理，返回错误信息
            return Result.fail();
        }
    }
    @GetMapping("/listPageC2")
    public List listPageC2(@RequestParam Integer task_id){
        return tableTemplateService.listPageC2(task_id);
    }

    @GetMapping("/listPageC3")
    public List listPageC3(){
        return tableTemplateService.listPageC3();
    }

    @GetMapping("/listPageC4")
    public Map listPageC4(){
        return (Map) tableTemplateService.listPageC4();
    }

    @GetMapping("/maxTaskId")
    public Integer maxTaskId(){
        return  tableTemplateService.maxTaskId();
    }

    @GetMapping("/updateStatus")
    public Result updateStatus(@RequestParam Integer task_id){
        return tableTemplateService.updateStatus(task_id)?Result.suc():Result.fail();
    }

}
