package com.food_factory_spring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food_factory_spring.entity.FlavourIngredients;
import com.food_factory_spring.mapper.TableContentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TableService extends ServiceImpl<TableContentMapper, FlavourIngredients> {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 从application.properties中读取数据库名称
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public boolean manageTable(String flavourName, String ingredientName, Double weight, Integer orderIndex, Integer task_index,Integer task_count_number) {  
        try {  
            // 解析数据库名称  
            String databaseName = datasourceUrl.split("\\?")[0].split("/")[3];  
    
            LocalDate currentDate = LocalDate.now();  
            String tableName = "table_" + currentDate.toString().replaceAll("-", "_");  
            System.out.println("Attempting to create table: " + tableName);  
    
            // 查询现有表  
            List<String> existingTables = jdbcTemplate.queryForList(  
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = ? AND table_name = ?",  
                    new Object[]{databaseName, tableName},  
                    String.class  
            );  
    
            if (existingTables.isEmpty()) {  
                // 手动创建表  
                jdbcTemplate.execute("CREATE TABLE " + tableName + " (" +  
                        "id INT NOT NULL AUTO_INCREMENT, " +  
                        "flavour_name VARCHAR(255), " +  
                        "ingredient_name VARCHAR(255), " +  
                        "weight DOUBLE, " +  
                        "order_index INT, " +  
                        "PRIMARY KEY (id)" +  
                        ")");  
                // 添加 task_index 列  
                jdbcTemplate.execute("ALTER TABLE " + tableName + " ADD COLUMN task_index INT");  
    
                // 新增 task_count_number 列  
                jdbcTemplate.execute("ALTER TABLE " + tableName + " ADD COLUMN task_count_number INT");  
    
                // 添加 completed 列，默认值为 0
                jdbcTemplate.execute("ALTER TABLE " + tableName + " ADD COLUMN completed TINYINT(1) DEFAULT 0");
    
                System.out.println("Table created: " + tableName);  
            } else {  
                System.out.println("Table already exists: " + tableName);  
    
                // 如果表已存在，检查是否需要添加 task_count_number 列  
                List<String> columns = jdbcTemplate.queryForList(  
                        "SELECT column_name FROM information_schema.columns WHERE table_schema = ? AND table_name = ?",  
                        new Object[]{databaseName, tableName},  
                        String.class  
                );  
    
                if (!columns.contains("task_count_number")) {  
                    jdbcTemplate.execute("ALTER TABLE " + tableName + " ADD COLUMN task_count_number INT");  
                    System.out.println("Column 'task_count_number' added to table: " + tableName);  
                }  
            }  
    
            // 插入示例内容  
            jdbcTemplate.update(  
                    "INSERT INTO " + tableName + " (flavour_name, ingredient_name, weight, order_index, task_index, task_count_number) VALUES (?, ?, ?, ?, ?, ?)",  
                    flavourName, ingredientName, weight, orderIndex, task_index, task_count_number );// 默认 task_count_number 值为 0        );
            System.out.println("Content added to table: " + tableName);  
    
            return true;    } catch (Exception e) {  
            System.err.println("Error managing table: " + e.getMessage());  
            e.printStackTrace(); // 打印详细的错误信息  
            return false;  
        }  
    }
}