package com.thanksbingo.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.httpclient.BingoHttpClient;
import com.thanksbingo.icondownloader.IconDownloader;

import org.apache.http.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
    *
    *
    *
     */

    public void loadAndStoreFoodInfoStartActivity(final Class<?> cls) {

        BingoHttpClient bh_client = new BingoHttpClient();
        final String subURL = "bingo_api/get_food_info/";
        bh_client.sendGetRequest(subURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int resp_code, Header[] headers, byte[] bytes) {
                try {
                    String resp_json = new String(bytes, "UTF-8");

                    try {
                        JSONObject jObject = new JSONObject(resp_json);

                        boolean need_update = jObject.getBoolean("need_update");
                        if (need_update) {
                            JSONArray jArray = new JSONArray(jObject.getString("food_info_list"));

                            int last_history = jObject.getInt("last_history");
                            SharedPreferences sharedPref = context.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, last_history);
                            editor.commit();

                            handleFoodInfoJSONArray(jArray);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, cls);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int resp_code, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i(CONST_STRINGS.BINGO_LOG, "loadAndStoreFoodInfoStartActivity method$ response code: " + resp_code);
            }
        });
    }

    /*
    *
    *
    *
     */

    public void updateFoodInfoTableOnDB() {

        int last_history = 0;
        final SharedPreferences sharedPref = context.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        last_history = sharedPref.getInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, last_history);

        BingoHttpClient bh_client = new BingoHttpClient();
        final String subURL = "bingo_api/update_food_info/" + last_history + "/";

        bh_client.sendGetRequest(subURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                try {
                    String resp_json = new String(bytes, "UTF-8");
                    try {
                        JSONObject jObject = new JSONObject(resp_json);

                        boolean need_update = jObject.getBoolean("need_update");
                        if (need_update) {
                            JSONArray type0_arr = new JSONArray(jObject.getString("type0"));
                            if (type0_arr.length() != 0) {
                                handleFoodInfoJSONArray(type0_arr);
                            }
                            JSONArray type1_arr = new JSONArray(jObject.getString("type1"));
                            if (type1_arr.length() != 0) {

                            }
                            JSONArray type2_arr = new JSONArray(jObject.getString("type2"));
                            if (type2_arr.length() != 0) {

                            }

                            int last_history = jObject.getInt("last_history");
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, last_history);
                            editor.commit();
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
                Log.i(CONST_STRINGS.BINGO_LOG, "updateFoodInfoTableOnDB method$ response code: " + resp_code);
            }
        });
    }

    private void updateFoodInfoDataOnTable() {

    }

    private void handleFoodInfoJSONArray(JSONArray jArray) {
        try {
            IconDownloader icon_downloader = new IconDownloader(null, "/Bingo/Icons/");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject food_info = jArray.getJSONObject(i);
                FoodInfoContract.FIData data = new FoodInfoContract.FIData();
                data.food_id = food_info.getInt("food_id");
                data.food_name = food_info.getString("food_name");
                data.rec_exp = food_info.getInt("rec_exp");
                data.frequency = food_info.getInt("frequency");
                try {
                    data.icon_img1 = icon_downloader.downloadIconImage(food_info.getString("icon1"));
                    data.icon_img2 = icon_downloader.downloadIconImage(food_info.getString("icon2"));
                } catch (IconDownloader.NoIconUrlException e) {
                    e.printStackTrace();
                } catch (IconDownloader.UndecidedIconsDirPath e) {
                    e.printStackTrace();
                }
                writeDataToFoodInfoTable(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //...

    /*
    *
    *
    *
     */

    public void writeDataToFoodInFridgeTable(FoodInFridgeContract.FIFData data) {
        new SavingFoodInFridgeDataToDB().execute(data);
    }
    private class SavingFoodInFridgeDataToDB extends AsyncTask<FoodInFridgeContract.FIFData, Void, Void> {
        @Override
        protected Void doInBackground(FoodInFridgeContract.FIFData... fif_arr) {

            db = db_helper.getWritableDatabase();

            int param_cnt = fif_arr.length;
            for (int i = 0; i < param_cnt; i++) {

                FoodInFridgeContract.FIFData fif = fif_arr[i];

                ContentValues values = new ContentValues();
                values.put(FoodInFridgeContract.FoodInFridge._ID, fif.food_id);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE, dateFormat.format(fif.reg_date));
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE, dateFormat.format(fif.exp_date));
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT, fif.amount);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION, fif.position);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY, fif.history);
                db.insert(
                        FoodInFridgeContract.FoodInFridge.TABLE_NAME,
                        "null",
                        values
                );
            }
            return null;
        }
    }

    public void updateFoodInFridgeDataOnDB(FoodInFridgeContract.FIFData data) {
        new UpdateFoodInFridgeDataOnDB().execute(data);
    }
    private class UpdateFoodInFridgeDataOnDB extends AsyncTask<FoodInFridgeContract.FIFData, Void, Void> {
        @Override
        protected Void doInBackground(FoodInFridgeContract.FIFData... fif_arr) {

            db = db_helper.getReadableDatabase();

            int param_cnt = fif_arr.length;
            for (int i = 0; i < param_cnt; i++) {
                FoodInFridgeContract.FIFData fif = fif_arr[i];

                ContentValues values = new ContentValues();
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID, fif.food_id);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE, dateFormat.format(fif.reg_date));
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE, dateFormat.format(fif.exp_date));
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT, fif.amount);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION, fif.position);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY, fif.history);

                String selection = FoodInFridgeContract.FoodInFridge._ID + " == ?";
                String[] selectionArgs = { String.valueOf(fif._id) };

                int cnt = db.update(
                        FoodInFridgeContract.FoodInFridge.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
            }

            return null;
        }
    }

    public void deleteFoodInFridgeDataOnDB(int _id) {
        new DeleteFoodInFridgeDataOnDB().execute(_id);
    }
    private class DeleteFoodInFridgeDataOnDB extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... _ids) {

            db = db_helper.getReadableDatabase();

            int param_cnt = _ids.length;
            for (int i = 0; i < param_cnt; i++) {
                int _id = _ids[i];
                String selection = FoodInFridgeContract.FoodInFridge._ID + " == ?";
                String[] selectionArgs = { String.valueOf(_id) };
                db.delete(FoodInFridgeContract.FoodInFridge.TABLE_NAME, selection, selectionArgs);
            }
            return null;
        }
    }

    public void deleteFoodInFridgeDataOnDB(ArrayList<Integer> _ids) {

    }
    private class DeleteFoodFridgeDataOnDB2 extends AsyncTask<ArrayList<Integer>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<Integer>... params) {

            db = db_helper.getReadableDatabase();

            int param_cnt = params.length;
            for (int i = 0; i < param_cnt; i++) {
                ArrayList<Integer> _ids = params[i];
                for (int j = 0; j < params[i].size(); j++) {
                    int _id = _ids.get(j);
                    String selection = FoodInFridgeContract.FoodInFridge._ID + " == ?";
                    String[] selectionArgs = { String.valueOf(_id) };
                    db.delete(FoodInFridgeContract.FoodInFridge.TABLE_NAME, selection, selectionArgs);
                }
            }
            return null;
        }
    }


    /*
    *
    *
    *
     */
    private void writeDataToFoodInfoTable(FoodInfoContract.FIData data) {

        new SavingFoodInfoDataToDB().execute(data);
    }
    private void writeDataToFoodInfoTable(int food_id, String food_name, int rec_exp_date, String icon_img1, String icon_img2, long frequency) {

        FoodInfoContract.FIData data = new FoodInfoContract.FIData();
        data.food_id = food_id;
        data.food_name = food_name;
        data.rec_exp = rec_exp_date;
        data.icon_img1 = icon_img1;
        data.icon_img2 = icon_img2;
        data.frequency = frequency;
        new SavingFoodInfoDataToDB().execute(data);
    }
    private class SavingFoodInfoDataToDB extends AsyncTask<FoodInfoContract.FIData, Void, Void> {
        protected Void doInBackground(FoodInfoContract.FIData... food_info) {

            db = db_helper.getWritableDatabase();

            int count = food_info.length;
            for (int i = 0; i < count; i++) {

                ContentValues values = new ContentValues();
                values.put(FoodInfoContract.FoodInfo._ID, food_info[i].food_id);
                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME, food_info[i].food_name);
                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP, food_info[i].rec_exp);
                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1, food_info[i].icon_img1);
                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2, food_info[i].icon_img2);
                values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY, food_info[i].frequency);
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
//                values.put(FoodListContract.FoodEntry.COLUMN_NAME_UPDATE_DATE, dateFormat.format(food_data[i].date));

                db.insert(
                        FoodInfoContract.FoodInfo.TABLE_NAME,
                        "null",
                        values
                );
            }

            return null;
        }

        protected void onPostExecute(Void v) {
        }
    }

    /*
    *
    *
    *
     */

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


//    private void writeDataToFoodInfoTable(ArrayList<FoodInfoContract.FIData> data) {
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


