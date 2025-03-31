package com.food_factory_spring.service.impl;

import com.food_factory_spring.entity.Menu;
import com.food_factory_spring.mapper.MenuMapper;
import com.food_factory_spring.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caicl
 * @since 2025-01-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

}
