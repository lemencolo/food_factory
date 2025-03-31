package com.food_factory_spring.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.food_factory_spring.entity.TableTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DynamicTableMapper extends BaseMapper<TableTemplate> {
    @Select("<script>" +
            "SELECT * FROM ${tableName}" +
            "<where>" +
            "   <if test='ew != null and ew.sqlSegment != null and ew.sqlSegment != \"\"'>" +
            "       ${ew.sqlSegment}" +
            "   </if>" +
            "</where>" +
            "</script>")
    IPage<TableTemplate> selectDynamicPage(Page<TableTemplate> page, @Param(Constants.WRAPPER) Wrapper<TableTemplate> wrapper, @Param("tableName") String tableName);
}
