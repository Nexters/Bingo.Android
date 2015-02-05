package com.thanksbingo.db;

import android.provider.BaseColumns;

import java.util.Date;

public final class FoodInfoContract {

    public FoodInfoContract() {
    }

    public static abstract class FoodInfo implements BaseColumns {

        public static final String TABLE_NAME = "FoodInfo";
        public static final String COLUMN_NAME_FOOD_NAME = "food_name";
        public static final String COLUMN_NAME_REC_EXP = "rec_exp";
        public static final String COLUMN_NAME_ICON_IMG1 = "icon_img1";
        public static final String COLUMN_NAME_ICON_IMG2 = "icon_img2";
        public static final String COLUMN_NAME_FREQUENCY = "frequency";
    }

    public static class FIData {

        public int food_id;
        public String food_name;
        public int rec_exp;
        public String icon_img1;
        public String icon_img2;
        public long frequency;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FoodInfo.TABLE_NAME + " (" +
                    FoodInfo._ID + SqlWords.INT_TYPE + SqlWords.PRIMARY_KEY + SqlWords.COMMA_SEP +
                    FoodInfo.COLUMN_NAME_FOOD_NAME + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInfo.COLUMN_NAME_REC_EXP + SqlWords.INT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInfo.COLUMN_NAME_ICON_IMG1 + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInfo.COLUMN_NAME_ICON_IMG2 + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInfo.COLUMN_NAME_FREQUENCY + SqlWords.INT_TYPE + SqlWords.DEFAULT(0) +
                    " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FoodInfo.TABLE_NAME;

}
