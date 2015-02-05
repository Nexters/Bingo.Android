package com.thanksbingo.db;

import android.provider.BaseColumns;

/**
 * Created by Hagyut on 2015. 1. 28..
 */
public class FoodExtraInfoContract {

    public FoodExtraInfoContract() {}

    public static abstract class FoodExtraInfo implements BaseColumns {

        public static final String TABLE_NAME = "FoodExtraInfo";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_EXTRA_INFO = "extra_info";
        public static final String COLUMN_NAME_ETC = "etc";
    }

    public static class FEIData {

        public int food_id;
        public String extra_info;
        public String etc;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FoodExtraInfo.TABLE_NAME + " (" +
                    FoodExtraInfo._ID + SqlWords.INT_TYPE + SqlWords.PRIMARY_KEY + SqlWords.AUTO_INC + SqlWords.COMMA_SEP +
                    FoodExtraInfo.COLUMN_NAME_FOOD_ID + SqlWords.INT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodExtraInfo.COLUMN_NAME_EXTRA_INFO + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodExtraInfo.COLUMN_NAME_ETC + SqlWords.TEXT_TYPE + SqlWords.COMMA_SEP +
                    SqlWords.FOREIGN_KEY(FoodExtraInfo.COLUMN_NAME_FOOD_ID, FoodInfoContract.FoodInfo.TABLE_NAME, FoodInfoContract.FoodInfo._ID) + SqlWords.ON_DELETE_CASCADE +
                    " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FoodExtraInfo.TABLE_NAME;

}
