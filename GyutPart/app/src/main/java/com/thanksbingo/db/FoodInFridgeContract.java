package com.thanksbingo.db;

import android.provider.BaseColumns;

import java.util.Date;

public class FoodInFridgeContract {

    public FoodInFridgeContract() {}

    public static abstract class FoodInFridge implements BaseColumns {

        public static final String TABLE_NAME = "FoodInFridge";
        public static final String COLUMN_NAME_FOOD_ID = "food_id";
        public static final String COLUMN_NAME_REG_DATE = "reg_date";
        public static final String COLUMN_NAME_EXP_DATE = "exp_date";
        public static final String COLUMN_NAME_AMOUNT = "amount";
        public static final String COLUMN_NAME_POSITION = "position";
    }

    public static class FIFData {

        public int food_id;
        public Date reg_date;
        public Date exp_date;
        public int amount;
        public String position;
    }

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + FoodInFridge.TABLE_NAME + " (" +
                    FoodInFridge._ID + SqlWords.INT_TYPE + SqlWords.PRIMARY_KEY + SqlWords.AUTO_INC + SqlWords.COMMA_SEP +
                    FoodInFridge.COLUMN_NAME_FOOD_ID + SqlWords.INT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInFridge.COLUMN_NAME_REG_DATE + SqlWords.DATE_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInFridge.COLUMN_NAME_EXP_DATE + SqlWords.DATE_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    FoodInFridge.COLUMN_NAME_AMOUNT + SqlWords.INT_TYPE + " DEFAULT=1" + SqlWords.COMMA_SEP +
                    FoodInFridge.COLUMN_NAME_POSITION + SqlWords.TEXT_TYPE + SqlWords.NOT_NULL + SqlWords.COMMA_SEP +
                    SqlWords.FOREIGN_KEY(FoodInFridge.COLUMN_NAME_FOOD_ID, FoodInfoContract.FoodInfo.TABLE_NAME, FoodInfoContract.FoodInfo._ID) + SqlWords.ON_DELETE_CASCADE + SqlWords.COMMA_SEP +
                    " )";

    public static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + FoodInFridge.TABLE_NAME;

}
