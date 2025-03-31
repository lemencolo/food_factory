package com.food_factory_spring.common;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TableNameValidator {

    // 正则表达式匹配 "table_2025_MM_dd" 格式的表名
    private static final String TABLE_NAME_REGEX = "^table_2025_(0[1-9]|1[012])_(0[1-9]|[12][0-9]|3[01])$";

    private static final String TABLE_NAME_REGEXC = "^task_2025_(0[1-9]|1[012])_(0[1-9]|[12][0-9]|3[01])$";

    public static boolean isValidTableName(String tableName) {
        return tableName != null && tableName.matches(TABLE_NAME_REGEX);
    }
    public static boolean isValidTableNameC(String tableName) {
        return tableName != null && tableName.matches(TABLE_NAME_REGEXC);
    }
}
