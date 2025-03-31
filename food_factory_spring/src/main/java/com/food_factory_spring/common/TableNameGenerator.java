package com.food_factory_spring.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TableNameGenerator {
    public static String generateTableName() {
        LocalDate currentDate = LocalDate.now();
        return "table_" + currentDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }
    public static String generateTaskName() {
        LocalDate currentDate = LocalDate.now();
        return "task_" + currentDate.format(DateTimeFormatter.ofPattern("yyyy_MM_dd"));
    }
}
