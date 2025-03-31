package com.food_factory_spring.service.impl;

import com.food_factory_spring.entity.Users;
import com.food_factory_spring.mapper.UsersMapper;
import com.food_factory_spring.service.IUsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caicl
 * @since 2025-01-15
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

}
