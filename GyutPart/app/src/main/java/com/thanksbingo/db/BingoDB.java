package com.thanksbingo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thanksbingo.httpclient.BingoHttpClient;
import com.thanksbingo.icondownloader.IconDownloader;

import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BingoDB {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Bingo.db";

    private class BingoDbHelper extends SQLiteOpenHelper {
        public BingoDbHelper(Context context, String db_name, int db_version) {
            super(context, db_name, null, db_version);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(FoodInfoContract.SQL_CREATE_TABLE);
            db.execSQL(FoodExtraInfoContract.SQL_CREATE_TABLE);
            db.execSQL(NoMixFoodInfoContract.SQL_CREATE_TABLE);
            db.execSQL(FoodInFridgeContract.SQL_CREATE_TABLE);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(FoodInfoContract.SQL_DELETE_TABLE);
            db.execSQL(FoodExtraInfoContract.SQL_DELETE_TABLE);
            db.execSQL(NoMixFoodInfoContract.SQL_DELETE_TABLE);
            db.execSQL(FoodInFridgeContract.SQL_DELETE_TABLE);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    private BingoDbHelper db_helper;
    private SQLiteDatabase db;

    private Context context;

    public BingoDB(Context _context) {
        context = _context;
        db_helper = new BingoDbHelper(_context, DATABASE_NAME, DATABASE_VERSION);
    }

    /*
    1. FoodInfo Table 에 새로운 데이터를 INSERT 할 때,
        public void writeDataToFoodInfoTable(FoodInfoContract.FIData data)
        public void writeDataToFoodInfoTable(String food_name, int rec_exp_date, String icon_img_path1, String icon_img_path2, long frequency)
        public void writeDataToFoodInfoTable(ArrayList<FoodInfoContract.FIData> data)

    2. FoodInfo Table 과 ExtraFoodInfo Table 에 존재하는 모든 음식물의 리스트(식별자, 이름, 등록 빈도)를 받아올 때, --> for 음성 인식
        * 이 함수들을 호출할 때, AsyncTask 사용하여야 하는가?
        public ArrayList<OtherClasses.SomeFood> getListOfWholeFood()
        public ArrayList<OtherClasses.SomeFood> getListOfWholeFood(int bottom_limit)
        public ArrayList<OtherClasses.SomeFood> getListOfWholeFood(int bottom_limit, int count_limit)

     */

    public void loadFoodInfoDataFromServerAndStoreThemOnDB() {

        BingoHttpClient bh_client = new BingoHttpClient();
        final String subURL = "bingo_api/get_food_info/";
        bh_client.sendGetRequest(subURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int resp_code, Header[] headers, byte[] bytes) {
                try {
                    String foodInfoList_json = new String(bytes, "UTF-8");
                    IconDownloader iconDownloader = new IconDownloader(null, "/Bingo/Icons/");

                    try {
                        JSONObject jObject = new JSONObject(foodInfoList_json);
                        JSONArray jArray = new JSONArray(jObject.getJSONArray("food_info_list"));
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject foodInfo = jArray.getJSONObject(i);
                            FoodInfoContract.FIData data = new FoodInfoContract.FIData();
                            data.food_id = foodInfo.getInt("food_id");
                            data.food_name = foodInfo.getString("food_name");
                            data.rec_exp = foodInfo.getInt("rec_exp");
                            data.frequency = foodInfo.getInt("frequency");
                            try {
                                data.icon_img1 = iconDownloader.downloadIconImage(foodInfo.getString("icon1"));
                                data.icon_img2 = iconDownloader.downloadIconImage(foodInfo.getString("icon2"));
                            } catch (IconDownloader.NoIconUrlException e) {
                                e.printStackTrace();
                            } catch (IconDownloader.UndecidedIconsDirPath e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int resp_code, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    public void updateFoodInfoDataDB() {
        BingoHttpClient bh_client = new BingoHttpClient();

    }



//    public void writeDataToFoodInfoTable(FoodInfoContract.FIData data) {
//
//        new SavingFoodInfoDataToDB().execute(data);
//    }
//    public void writeDataToFoodInfoTable(int food_id, String food_name, int rec_exp_date, String icon_img1, String icon_img2, long frequency) {
//
//        FoodInfoContract.FIData data = new FoodInfoContract.FIData();
//        data.food_id = food_id;
//        data.food_name = food_name;
//        data.rec_exp = rec_exp_date;
//        data.icon_img1 = icon_img1;
//        data.icon_img2 = icon_img2;
//        data.frequency = frequency;
//        new SavingFoodInfoDataToDB().execute(data);
//    }
//    private class SavingFoodInfoDataToDB extends AsyncTask<FoodInfoContract.FIData, Void, Void> {
//        protected Void doInBackground(FoodInfoContract.FIData... food_info) {
//
//            db = db_helper.getWritableDatabase();
//
//            int count = food_info.length;
//            for (int i = 0; i < count; i++) {
//
//                ContentValues values = new ContentValues();
//                values.put(FoodInfoContract.FoodInfo._ID, food_info[i].food_id);
//                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME, food_info[i].food_name);
//                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP, food_info[i].rec_exp);
//                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1, food_info[i].icon_img1);
//                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2, food_info[i].icon_img2);
//                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY, food_info[i].frequency);
////                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
////                values.put(FoodListContract.FoodEntry.COLUMN_NAME_UPDATE_DATE, dateFormat.format(food_data[i].date));
//
//                db.insert(
//                        FoodInfoContract.FoodInfo.TABLE_NAME,
//                        "null",
//                        values
//                );
//            }
//
//            return null;
//        }
//
//        protected void onPostExecute(Void v) {
//        }
//    }
//
//    public void writeDataToFoodInfoTable(ArrayList<FoodInfoContract.FIData> data) {
//
//        new SavingFoodInfoArrayDataToDB().execute(data);
//    }
//    private class SavingFoodInfoArrayDataToDB extends AsyncTask<ArrayList<FoodInfoContract.FIData>, Void, Void> {
//        protected Void doInBackground(ArrayList<FoodInfoContract.FIData>... food_info_list) {
//
//            db = db_helper.getWritableDatabase();
//
//            int count = food_info_list.length;
//            // food_info[i] -->> ArrayList
//            for (int i = 0; i < count; i++) {
//                for (int j = 0; j < food_info_list[i].size(); j++) {
//                    ContentValues values = new ContentValues();
//                    FoodInfoContract.FIData food_info = food_info_list[i].get(j);
//                    values.put(FoodInfoContract.FoodInfo._ID, food_info.food_id);
//                    values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME, food_info.food_name);
//                    values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP, food_info.rec_exp);
//                    values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1, food_info.icon_img1);
//                    values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2, food_info.icon_img2);
//                    values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY, food_info.frequency);
//
//                    db.insert(
//                            FoodInfoContract.FoodInfo.TABLE_NAME,
//                            "null",
//                            values
//                    );
//                }
//            }
//
//            return null;
//        }
//
//        protected void onPostExecute(Void v) {
//        }
//    }

    public void getFoodList(OnGetFoodListHandler handler) {
        new GetFoodList().execute(handler);
    }
    private class GetFoodList extends AsyncTask<OnGetFoodListHandler, Void, Void> {
        @Override
        protected Void doInBackground(OnGetFoodListHandler... handlers) {

            OnGetFoodListHandler handler = handlers[0];

            ArrayList<OtherClasses.SomeFood> whole_food_list = new ArrayList<OtherClasses.SomeFood>();

            db = db_helper.getReadableDatabase();

            // for FoodInfo Table
            String[] projection = {
                    FoodInfoContract.FoodInfo._ID,
                    FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME,
                    FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY
            };

            String sortOrder = FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY + " DESC";

            Cursor c = db.query(
                    FoodInfoContract.FoodInfo.TABLE_NAME,      // The table to query
                    projection,                                 // The columns to return
                    null,                                       // The columns for the WHERE clause
                    null,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    sortOrder                                   // The sort order
            );

            c.moveToFirst();
            while (!c.isAfterLast()) {
                OtherClasses.SomeFood some_food = new OtherClasses.SomeFood();
                some_food.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo._ID));
                some_food.food_name = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME));
                some_food.frequency = c.getLong(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY));
                whole_food_list.add(some_food);
                c.moveToNext();
            }

            handler.onGetFoodList(whole_food_list);

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
        }
    }

//    private ArrayList<OtherClasses.SomeFood> __getFoodList() {
//
//        ArrayList<OtherClasses.SomeFood> whole_food_list = new ArrayList<OtherClasses.SomeFood>();
//
//        db = db_helper.getReadableDatabase();
//
//        // for FoodInfo Table
//        String[] projection = {
//                FoodInfoContract.FoodInfo._ID,
//                FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME,
//                FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY
//        };
//
//        String sortOrder = FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY + " DESC";
//
//        Cursor c = db.query(
//                FoodInfoContract.FoodInfo.TABLE_NAME,      // The table to query
//                projection,                                 // The columns to return
//                null,                                       // The columns for the WHERE clause
//                null,                                       // The values for the WHERE clause
//                null,                                       // don't group the rows
//                null,                                       // don't filter by row groups
//                sortOrder                                   // The sort order
//        );
//
//        c.moveToFirst();
//        while (!c.isAfterLast()) {
//            OtherClasses.SomeFood some_food = new OtherClasses.SomeFood();
//            some_food.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo._ID));
//            some_food.food_name = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME));
//            some_food.frequency = c.getLong(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY));
//            whole_food_list.add(some_food);
//            c.moveToNext();
//        }
//
//        // for ExtraFoodList Table
//        String[] projection2 = {
//                ExtraFoodListContract.ExtraFoodList._ID,
//                ExtraFoodListContract.ExtraFoodList.COLUMN_NAME_FOOD_NAME,
//                ExtraFoodListContract.ExtraFoodList.COLUMN_NAME_FREQUENCY
//        };
//
//        String selection = ExtraFoodListContract.ExtraFoodList.COLUMN_NAME_FREQUENCY + " >= ?";
//        String[] selectionArgs = { String.valueOf(bottom_limit) };
//
//        String limit = null;
//        if (count_limit != 0) {
//            limit = " LIMIT " + String.valueOf(count_limit);
//        }
//
//        String sortOrder2 = ExtraFoodListContract.ExtraFoodList.COLUMN_NAME_FREQUENCY + " DESC";
//
//        Cursor c2 = db.query(
//                ExtraFoodListContract.ExtraFoodList.TABLE_NAME,       // The table to query
//                projection,                                 // The columns to return
//                selection,                                  // The columns for the WHERE clause
//                selectionArgs,                              // The values for the WHERE clause
//                null,                                       // don't group the rows
//                null,                                       // don't filter by row groups
//                sortOrder,                                  // The sort order
//                limit                                       // limit
//        );
//
//        c2.moveToFirst();
//        int i = 0;
//        while (!c2.isAfterLast()) {
//            long freq = c.getInt(c.getColumnIndexOrThrow(ExtraFoodListContract.ExtraFoodList.COLUMN_NAME_FREQUENCY));
//            while (i < whole_food_list.size() && whole_food_list.get(i).frequency > freq) {
//                i++;
//            }
//            OtherClasses.SomeFood some_food = new OtherClasses.SomeFood();
//            some_food.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo._ID));
//            some_food.food_name = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME));
//            some_food.frequency = c.getLong(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY));
//            whole_food_list.add(i, some_food);
//            i++;
//            c.moveToNext();
//        }
//
//        return whole_food_list;
//    }

}


