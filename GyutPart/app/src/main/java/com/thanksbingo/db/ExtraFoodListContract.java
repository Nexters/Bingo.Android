package com.thanksbingo.db;

import android.provider.BaseColumns;

public class ExtraFoodListContract {

    public ExtraFoodListContract() {}

    public static abstract class ExtraFoodList implements BaseColumns {

        public static final String TABLE_NAME = "ExtraFoodList";
        public static final String COLUMN_NAME_FOOD_NAME = "food_name";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";
    }

    public static class EFLData {

        public String food_name;
        public long frequency;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + ExtraFoodList.TABLE_NAME + " (" +
                    ExtraFoodList._ID + SqlWords.INT_TYPE + SqlWords.PRIMARY_KEY + SqlWords.AUTO_INC + SqlWords.COMMA_SEP +
                    ExtraFoodList.COLUMN_NAME_FOOD_NAME + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    ExtraFoodList.COLUMN_NAME_FREQUENCY + SqlWords.INT_TYPE + SqlWords.DEFAULT(0) +
                    " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + ExtraFoodList.TABLE_NAME;

}
