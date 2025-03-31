package com.food_factory_spring.service.impl;

import com.food_factory_spring.common.QueryPageParam;
import com.food_factory_spring.common.Result;
import com.food_factory_spring.common.TableNameGenerator;
import com.food_factory_spring.common.TableNameValidator;
import com.food_factory_spring.entity.TableTemplate;
import com.food_factory_spring.entity.Task;
import com.food_factory_spring.mapper.TableTemplateMapper;
import com.food_factory_spring.service.ITableTemplateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author caicl
 * @since 2025-01-24
 */
@Service
public class TableTemplateServiceImpl extends ServiceImpl<TableTemplateMapper, TableTemplate> implements ITableTemplateService {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Result listPage(QueryPageParam query) {
        String tableName = TableNameGenerator.generateTableName();
        System.out.println(tableName);

        if (!TableNameValidator.isValidTableName(tableName)) {
            return Result.fail();
        }

        int pageNum = query.getPageNum();
        int pageSize = query.getPageSize();

        // 计算偏移量
        int offset = (pageNum - 1) * pageSize;

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(tableName)
                .append(" ORDER BY task_index ASC, order_index ASC")
                .append(" LIMIT ? OFFSET ?");
        System.out.println(pageSize);
        System.out.println(offset);
        // 执行查询
        List<TableTemplate> records = jdbcTemplate.query(
                sqlBuilder.toString(),
                new Object[]{pageSize, offset},
                new BeanPropertyRowMapper<>(TableTemplate.class)
        );

        // 获取总记录数
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        System.out.println(records);
        return Result.suc(records, Long.valueOf(total));
    }

    public Result listPageC() {
        // 生成表名
        String tableName = TableNameGenerator.generateTaskName();
//        tableName = "task_2025_01_24";
        System.out.println("Generated table name: " + tableName);

        // 验证表名是否有效
        if (!TableNameValidator.isValidTableNameC(tableName)) {
            return Result.fail();
        }

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(tableName)
                .append(" ORDER BY task_index ASC");

        System.out.println("Executing SQL: " + sqlBuilder.toString());

        // 执行查询
        List<Task> records = jdbcTemplate.query(
                sqlBuilder.toString(),
                new BeanPropertyRowMapper<>(Task.class)
        );

        // 获取总记录数
        Integer total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + tableName, Integer.class);
        System.out.println("Retrieved records: " + records);

        return Result.suc(records, Long.valueOf(total));
    }

    @Override
    public List<TableTemplate> listPageC2(Integer taskId) {
        String tableName = TableNameGenerator.generateTableName();
        System.out.println(tableName);

        if (!TableNameValidator.isValidTableName(tableName)) {
            return null;
        }

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(tableName)
                .append(" WHERE task_index = ? AND completed = ?")
                .append(" ORDER BY order_index ASC");

        // 执行查询并返回结果
        try {
            List<TableTemplate> records = jdbcTemplate.query(
                    sqlBuilder.toString(),
                    new Object[]{taskId, 0}, // 参数数组
                    new BeanPropertyRowMapper<>(TableTemplate.class)
            );

            return records;
        } catch (Exception e) {
            e.printStackTrace();
            // 记录异常日志或处理异常
            return null;
        }
    }

    @Override
    public List<TableTemplate> listPageC3() {
        String tableName = TableNameGenerator.generateTableName();
        System.out.println(tableName);

        if (!TableNameValidator.isValidTableName(tableName)) {
            return null;
        }

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(tableName)
                .append(" WHERE completed = ?")
                .append(" ORDER BY task_index ASC, order_index ASC");

        // 执行查询并返回结果
        try {
            List<TableTemplate> records = jdbcTemplate.query(
                    sqlBuilder.toString(),
                    new Object[]{0}, // 参数数组
                    new BeanPropertyRowMapper<>(TableTemplate.class)
            );

//            // 按照 task_index 分组
//            Map<Integer, List<TableTemplate>> groupedByTaskIndex = records.stream()
//                    .collect(Collectors.groupingBy(TableTemplate::getTaskIndex));

            return records;
        } catch (Exception e) {
            e.printStackTrace();
            // 记录异常日志或处理异常
            return null;
        }
    }

    public Map<Integer, List<TableTemplate>> listPageC4() {
        String tableName = TableNameGenerator.generateTableName();
        System.out.println(tableName);

        if (!TableNameValidator.isValidTableName(tableName)) {
            return null;
        }

        // 构建SQL查询语句
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT * FROM ").append(tableName)
                .append(" WHERE completed = ?")
                .append(" ORDER BY task_index ASC, order_index ASC");

        // 执行查询并返回结果
        try {
            List<TableTemplate> records = jdbcTemplate.query(
                    sqlBuilder.toString(),
                    new Object[]{false}, // 参数数组
                    new BeanPropertyRowMapper<>(TableTemplate.class)
            );

            // 按照 task_index 分组
            Map<Integer, List<TableTemplate>> groupedByTaskIndex = records.stream()
                    .collect(Collectors.groupingBy(TableTemplate::getTaskIndex));

            return groupedByTaskIndex;

        } catch (Exception e) {
            e.printStackTrace();
            // 记录异常日志或处理异常
            return null;
        }
    }

    @Override
    public Integer maxTaskId() {
        String tableName = TableNameGenerator.generateTableName();
        System.out.println(tableName);

        if (!TableNameValidator.isValidTableName(tableName)) {
            return null;
        }

        // 构建SQL查询语句以获取最大 task_index
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT MAX(task_index) FROM ").append(tableName);

        // 执行查询并返回结果
        try {
            Integer maxTaskIndex = jdbcTemplate.queryForObject(
                    sqlBuilder.toString(),
                    new Object[]{},
                    Integer.class
            );

            return maxTaskIndex;

        } catch (Exception e) {
            e.printStackTrace();
            // 记录异常日志或处理异常
            return null;
        }
    }

    @Override
    public boolean updateStatus(Integer taskId) {
        // 生成表名
        String tableName = TableNameGenerator.generateTableName();

        // 验证表名是否有效
        if (!TableNameValidator.isValidTableName(tableName)) {
            return false;
        }

        // 查询是否存在对应 task_id 的记录
        String selectSql = "SELECT task_index FROM " + tableName + " WHERE id = ?";
        Integer taskIndex = jdbcTemplate.queryForObject(
                selectSql,
                new Object[]{taskId},
                Integer.class
        );

        if (taskIndex == null) {
            // 没有找到对应的记录
            return false;
        }

        // 更新 completed 字段为 1
        String updateSql = "UPDATE " + tableName + " SET completed = 1 WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(updateSql, taskId);

        return rowsAffected > 0; // 返回更新是否成功
    }
}
