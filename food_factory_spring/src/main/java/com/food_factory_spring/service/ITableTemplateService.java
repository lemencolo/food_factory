package com.food_factory_spring.service;

import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import com.food_factory_spring.entity.TableTemplate;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author caicl
 * @since 2025-01-24
 */
public interface ITableTemplateService extends IService<TableTemplate> {


    Result listPage(QueryPageParam query);

    Result listPageC();

    List listPageC2(Integer taskId);

    List<TableTemplate> listPageC3();

    Map listPageC4();

    Integer maxTaskId();

    boolean updateStatus(Integer task_id);
}
