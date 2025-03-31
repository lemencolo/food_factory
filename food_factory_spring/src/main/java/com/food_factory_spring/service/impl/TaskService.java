package com.food_factory_spring.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import com.food_factory_spring.common.TableNameGenerator;

import java.time.LocalDate;
import java.util.*;

@Service
public class TaskService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 从application.properties中读取数据库名称
    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    /**
     * 管理表并插入实际内容
     *
     * @param taskName   任务名称
     * @param taskCount  任务数量
     * @return 操作结果
     */
     public List<Integer> manageTask(String taskName, Integer taskCount) {  
        try {  
            // 解析数据库名称  
            String databaseName = datasourceUrl.split("\\?")[0].split("/")[3];  
  
            LocalDate currentDate = LocalDate.now();  
            String tableName = "task_" + currentDate.toString().replaceAll("-", "_");  
            System.out.println("Attempting to create table: " + tableName);  
  
            // 查询现有表  
            List<String> existingTables = jdbcTemplate.queryForList(  
                    "SELECT table_name FROM information_schema.tables WHERE table_schema = ? AND table_name = ?",  
                    new Object[]{databaseName, tableName},  
                    String.class  
            );  
  
            if (existingTables.isEmpty()) {  
                // 创建表  
                jdbcTemplate.execute("CREATE TABLE " + tableName + " (" +  
                        "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +  
                        "task_name VARCHAR(255), " +  
                        "task_count INT, " +  
                        "completed_count INT DEFAULT 0, " +  
                        "task_index INT," +  
                        "completed TINYINT(1) DEFAULT 0)");  
                System.out.println("Table created: " + tableName);  
            } else {  
                System.out.println("Table already exists: " + tableName);  
            }  
  
            // 获取当前表中的最大 task_index 值  
//            Integer maxTaskIndex = jdbcTemplate.queryForObject(  
//                    "SELECT COALESCE(MAX(task_index), 0) FROM " + tableName,  
//                    Integer.class  
//            );  
            // 查询当前表中 task_index 最大的记录  
            List<Map<String, Object>> result = jdbcTemplate.queryForList(  
                    "SELECT task_index, task_count " +  
                            "FROM " + tableName +  
                            " ORDER BY task_index DESC LIMIT 1"  
            );  
  
            // 提取 maxTaskIndex 和对应的 taskCount            
            Integer maxTaskIndex = 1;  
            Integer taskCountForMaxIndex = 0;  
  
            if (!result.isEmpty()) {  
                Map<String, Object> row = result.get(0);  
                maxTaskIndex = (Integer) row.get("task_index");  
                taskCountForMaxIndex = (Integer) row.get("task_count");  
            }  
  
            // 计算新的 task_index 范围  
            int startTaskIndex = maxTaskIndex + taskCountForMaxIndex; // 1 + 2  
            int endTaskIndex = maxTaskIndex + taskCountForMaxIndex + taskCount -1 ;  //  
  
            // 构建任务索引列表  
            List<Integer> taskIndices = new ArrayList<>();  
            for (int i = startTaskIndex; i <= endTaskIndex; i++) {  
                taskIndices.add(i);  
            }  
  
//            // 插入实际内容  
//            for (int taskIndex : taskIndices) {  
//                jdbcTemplate.update(  
//                        "INSERT INTO " + tableName + " (task_name, task_count, task_index) VALUES (?, ?, ?)",  
//                        taskName, taskCount, taskIndex  
//                );  
//            }  
  
  
            jdbcTemplate.update(  
                    "INSERT INTO " + tableName + " (task_name, task_count, task_index) VALUES (?, ?, ?)",  
                    taskName, taskCount, taskIndices.get(0)  
            );  
  
            System.out.println("Content added to table: " + tableName);  
  
            return taskIndices; // 返回任务索引列表  
        } catch (Exception e) {  
            System.err.println("Error managing task: " + e.getMessage());  
            e.printStackTrace(); // 打印详细的错误信息  
            return null; // 如果发生异常，返回 null 表示失败  
        }  
    }

    public boolean updateTaskStatus(Integer task_id) {  
    System.out.println("updateTaskStatus task_id "+task_id);  
    try {  
        // 解析数据库名称  
        String databaseName = datasourceUrl.split("\\?")[0].split("/")[3];  
  
        LocalDate currentDate = LocalDate.now();  
        String tableName = "task_" + currentDate.toString().replaceAll("-", "_");  
        String tableName_table = TableNameGenerator.generateTableName();  
  
        // 查询是否存在对应 task_id 的记录  
        String selectSql1 = "SELECT task_count_number FROM " + tableName_table + " WHERE task_index = ?";  
  
        // 获取task_count_number  
        List<Integer>  taskCountNumbers = jdbcTemplate.queryForList(  
                selectSql1,  
                new Object[]{task_id},  
                Integer.class  
        );  
  
        // 检查所有值是否相同  
        Integer firstValue = taskCountNumbers.get(0);  
        if (!taskCountNumbers.stream().allMatch(num -> num.equals(firstValue))) {  
            System.err.println("Inconsistent task_count_number values for task_id: " + task_id);  
            return false; // 或者抛出异常  
        }  
  
        Integer task_index = taskCountNumbers.get(0);;  
  
        System.out.println("Attempting to create table: " + tableName);  
  
        String selectTaskDetailsSql = "SELECT task_count, completed_count FROM " + tableName + " WHERE task_index = ?";  
        Map<String, Object> taskDetails = jdbcTemplate.queryForObject(  
                selectTaskDetailsSql,  
                new Object[]{task_index},  
                (rs, rowNum) -> {  
                    Map<String, Object> row = new HashMap<>();  
                    row.put("task_count", rs.getInt("task_count"));  
                    row.put("completed_count", rs.getInt("completed_count"));  
                    return row;  
                }  
        );  
  
        if (taskDetails == null || taskDetails.isEmpty()) {  
            // 如果没有找到对应的记录，返回失败  
            return false;  
        }  
  
        // 提取 task_count 和 completed_count        
        int taskCount = (int) taskDetails.get("task_count");  
        int completedCount = (int) taskDetails.get("completed_count");  
  
        String updateCompletedCountSql = "UPDATE " + tableName + " SET completed_count = completed_count + 1 WHERE task_index = ?";  
        int rowsAffected = jdbcTemplate.update(updateCompletedCountSql, task_index);  
  
        if (rowsAffected <= 0) {  
            // 如果更新失败，返回 false
            return false;
        }  
  
        if (completedCount + 1 >= taskCount) {  
            // 如果完成任务，将 completed 设置为 1            
            String updateCompletedStatusSql = "UPDATE " + tableName + " SET completed = 1 WHERE task_index = ?";  
            jdbcTemplate.update(updateCompletedStatusSql, task_index);  
        }  
        return true;  
    } catch (Exception e) {  
        System.err.println("Error managing task: " + e.getMessage());  
        e.printStackTrace(); // 打印详细的错误信息  
        return false;  
    }  
}
}
