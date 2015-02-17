package com.thanksbingo.db;

import android.provider.BaseColumns;

/**
 * Created by Hagyut on 2015. 1. 28..
 */
public class NoMixFoodInfoContract {

    public NoMixFoodInfoContract() {}

    public static abstract class NoMixFoodInfo implements BaseColumns {

        public static final String TABLE_NAME = "NoMixFoodInfo";
        public static final String COLUMN_NAME_FOOD1_ID = "food1_id";
        public static final String COLUMN_NAME_FOOD2_ID = "food2_id";
        public static final String COLUMN_NAME_EXTRA_INFO = "extra_info";
    }

    public static class NMFIData {

        public int food1_id;
        public int food2_id;
        public String extra_info;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + NoMixFoodInfo.TABLE_NAME + " (" +
                    NoMixFoodInfo._ID + SqlWords.INT_TYPE + SqlWords.PRIMARY_KEY + SqlWords.AUTO_INC + SqlWords.COMMA_SEP +
                    NoMixFoodInfo.COLUMN_NAME_FOOD1_ID + SqlWords.INT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    NoMixFoodInfo.COLUMN_NAME_FOOD2_ID + SqlWords.INT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    NoMixFoodInfo.COLUMN_NAME_EXTRA_INFO + SqlWords.TEXT_TYPE + SqlWords.COMMA_SEP +
                    SqlWords.FOREIGN_KEY(NoMixFoodInfo.COLUMN_NAME_FOOD1_ID, FoodInfoContract.FoodInfo.TABLE_NAME, FoodInfoContract.FoodInfo._ID) + SqlWords.ON_DELETE_CASCADE + SqlWords.COMMA_SEP +
                    SqlWords.FOREIGN_KEY(NoMixFoodInfo.COLUMN_NAME_FOOD2_ID, FoodInfoContract.FoodInfo.TABLE_NAME, FoodInfoContract.FoodInfo._ID) + SqlWords.ON_DELETE_CASCADE +
                    " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + NoMixFoodInfo.TABLE_NAME;

}
