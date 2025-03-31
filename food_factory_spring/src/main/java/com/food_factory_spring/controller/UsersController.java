package com.food_factory_spring.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.entity.*;
import com.food_factory_spring.service.IMenuService;
import com.food_factory_spring.service.IUsersService;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UsersController {

    @Autowired
    private IUsersService usersService;

    @Autowired
    private IMenuService menuService;

    @PostMapping("/login")
    public Result loginUser(@RequestBody UserLogin user) {
        // 从数据库中查询用户
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", user.getUsername());
        System.out.println(user.getUsername());
        List<Users> userList = usersService.list(queryWrapper);

        if (userList.size() > 0) {
            Users dbUser = userList.get(0);
            List menuList = menuService.lambdaQuery().like(Menu::getMenuRight,dbUser.getRoleID()).list();
            if (!dbUser.getIsActive()) {
                return Result.fail();
            }
//            System.out.println(user.getPassword());
            // 验证密码
            if (dbUser.checkPassword(user.getPassword())) {
                HashMap<String, Object> res = new HashMap<>();
                res.put("user", dbUser);
                // 如果有菜单相关的服务，可以在这里添加
                 res.put("menu", menuList);
                System.out.println(res);
                return Result.suc(res);
            } else {
                return Result.fail();
            }
        } else {
            return Result.fail();
        }
    }
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<Users> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Users> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        IPage<Users> result = usersService.page(page, lambdaQueryWrapper);

        List<UserShow> userShowList = result.getRecords().stream()
                .map(user -> {
                    UserShow userShow = new UserShow();
                    BeanUtils.copyProperties(user, userShow);
                    userShow.setCreated_at(user.getCreatedAt());  // 假设 Users 类中有 getCreateTime 方法
                    userShow.setUpdated_at(user.getUpdatedAt());  // 假设 Users 类中有 getUpdateTime 方法
                    return userShow;
                })
                .collect(Collectors.toList());


        return Result.suc(userShowList, result.getTotal());
    }
    @PostMapping("toggleStatus")
    public Result listPage(@RequestParam Integer userId) {
        try {
            // 从数据库中查询用户
            Users user = usersService.getById(userId);
            if (user == null) {
                return Result.fail();
            }

            // 切换用户的 isActive 状态
            user.setIsActive(!user.getIsActive());
            usersService.updateById(user);

            return Result.suc("用户状态更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail();
        }
    }
}