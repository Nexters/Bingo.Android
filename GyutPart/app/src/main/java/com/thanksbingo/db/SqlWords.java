package com.thanksbingo.db;

public final class SqlWords {

    public static final String COMMA_SEP = ",";
    public static final String AUTO_INC = " AUTOINCREMENT";
    public static final String PRIMARY_KEY = " PRIMARY KEY";
    public static final String NOT_NULL = " NOT NULL";
    public static final String ON_DELETE_CASCADE = " ON DELETE CASCADE";
    public static final String TEXT_TYPE = " TEXT";
    public static final String INT_TYPE = " INTEGER";
    public static final String DATE_TYPE = " DATE";

    public static String DEFAULT(int value) {
        return " DEFAULT=" + value;
    }

    public static String DEFAULT(String value) {
        return " DEFAULT=" + value;
    }

    public static String FOREIGN_KEY(String column_in_this_table, String target_table, String column_in_target_table) {
        return " FOREIGN KEY(" + column_in_this_table + ") REFERENCES " + target_table + "(" + column_in_target_table + ")";
    }
}
