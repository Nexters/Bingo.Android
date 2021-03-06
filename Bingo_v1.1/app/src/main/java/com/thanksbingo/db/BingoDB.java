package com.thanksbingo.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BingoDB {

    public BingoDB(Context _context) {
        context = _context;
        db_helper = new BingoDbHelper(_context, DATABASE_NAME, DATABASE_VERSION);
    }

    /*
     * public int getFoodInfoIdOf(String food_name):
     *
     *      FoodInfo 테이블에서 food_name attr 값으로 해당 food info의 id 값을 리턴한다.
     *
     * public void initiateFoodInfoDbAndStartFirstActivity(final Class<?> cls):
     *
     *      final Class<?> cls -> 다음에 실행될 Activity class
     *
     *      앱이 처음 구동되어서 FoodInfo 테이블에 데이터가 비어있을 때, 호출되어야 하는 메소드.
     *
     *      기존에 생성된 FoodInfo 테이블이 있다면, 아래의
     *      public void updateFoodInfoDbAndStartFirstActivity(final Class<?> cls) 메소드가 호출되어야 한다.
     *
     *      initiateFoodInfoDbAndStartFirstActivity 메소드가 호출되면 FoodInfo 테이블을 서버에 있는 데이터로 초기화하고,
     *      Food icon 들을 다운받은 뒤, cls로 지정된 Activity를 실행한다.
     *
     * public void updateFoodInfoDbAndStartFirstActivity(final Class<?> cls):
     *
     *      final Class<?> cls -> 다음에 실행될 Activity class
     *
     *      기존에 생성되어 있는 FoodInfo 테이블을 서버로부터 데이터를 가져와 업데이트 한다.
     *       업데이트 기준은 Last history라는 데이터가 된다.
     *
     *      FoodInfo 데이터를 업데이트 한뒤 cls로 지정된 Activity를 실행한다.
     *
     * public void writeDataToFoodInFridgeTable(FoodInFridgeContract.FIFData data):
     *
     *      FoodInFridge 테이블에 데이터 저장.
     *      내 냉장고에 음식물 등록할 때 사용.
     *
     * public void updateFoodInFridgeDataOnDB(FoodInFridgeContract.FIFData data):
     *
     * public void deleteFoodInFridgeDataOnDB(int _id):
     * public void deleteFoodInFridgeDataOnDB(ArrayList<Integer> _ids):
     *
     *
     * public ArrayList<FoodInFridgeContract.FIFData> getListOfFoodInFridge(String loc_code):
     *
     * public String[] getIconPath(int food_id):
     *
     * public ArrayList<OtherClasses.SomeFood> getFoodList():
     *
     * public void getFoodList(OnGetFoodListHandler handler):
     *
     * public ArrayList<FoodInfoContract.FIData> getAllFoodInfo():
     *
     *
     *
     */


    public int getFoodInfoIdOf(String food_name) {

        int food_id = -1;

        db = db_helper.getReadableDatabase();

        String[] projection = {
                FoodInfoContract.FoodInfo._ID
        };
        String selection = FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME + " == ?";
        String[] selectionArgs = { new String(food_name) };

        Cursor c = db.query(
                FoodInfoContract.FoodInfo.TABLE_NAME,      // The table to query
                projection,                                 // The columns to return
                selection,                                       // The columns for the WHERE clause
                selectionArgs,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                   // The sort order
        );

        if (c.getCount() != 0) {
            c.moveToFirst();
            food_id = c.getInt(c.getColumnIndex(FoodInfoContract.FoodInfo._ID));
        }

        return food_id;
    }

    public void writeFoodInfoToDB(FoodInfoContract.FIData data) {

        db = db_helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodInfoContract.FoodInfo._ID, data.food_id);
        values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME, data.food_name);
        values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP, data.rec_exp);
        values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1, data.icon_img1);
        values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2, data.icon_img2);
        values.put(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY, data.frequency);

        db.insert(
                FoodInfoContract.FoodInfo.TABLE_NAME,
                "null",
                values
        );

    }

    public void initiateFoodInfoDbAndStartFirstActivity(final Class<?> cls) {

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
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int resp_code, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i(CONST_STRINGS.BINGO_LOG, "loadAndStoreFoodInfoStartActivity method$ response code: " + resp_code);
            }
        });
    }

    private void handleFoodInfoJSONArray(JSONArray jArray) {
        try {
            IconDownloader icon_downloader = new IconDownloader(null, CONST_STRINGS.ICON_PATH);
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


    public void updateFoodInfoDbAndStartFirstActivity(final Class<?> cls) {

        int last_history = 0;
        final SharedPreferences sharedPref = context.getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        last_history = sharedPref.getInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, 0);

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
                                // type0 : 새로이 추가된 데이터
                                handleFoodInfoJSONArray(type0_arr);
                            }
                            JSONArray type1_arr = new JSONArray(jObject.getString("type1"));
                            if (type1_arr.length() != 0) {
                                /* type 1: modified data (except icon image)
                                 * 수정된 데이터 값 업데이트.
                                 * 아직 구현 안함.
                                 */
                            }
                            JSONArray type2_arr = new JSONArray(jObject.getString("type2"));
                            if (type2_arr.length() != 0) {
                                /* type 2: modified data & icon
                                 * 수정된 데이터 값 업데이트와 아이콘 다운
                                 * 아직 구현 안함
                                 */
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
                Intent intent = new Intent(context, cls);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void onFailure(int resp_code, Header[] headers, byte[] bytes, Throwable throwable) {
                Log.i(CONST_STRINGS.BINGO_LOG, "updateFoodInfoTableOnDB method$ response code: " + resp_code);
            }
        });
    }

    public void writeDataToFoodInFridgeTable(FoodInFridgeContract.FIFData data) {
        //new SavingFoodInFridgeDataToDB().execute(data);
        db = db_helper.getWritableDatabase();

//        int param_cnt = fif_arr.length;
//        for (int i = 0; i < param_cnt; i++) {

//            FoodInFridgeContract.FIFData fif = fif_arr[i];
        FoodInFridgeContract.FIFData fif = data;
            ContentValues values = new ContentValues();
            values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID, fif.food_id);
            values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME, fif.food_name);
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
//        }
    }

    public void updateFoodInFridgeDataOnDB(FoodInFridgeContract.FIFData data) {
        new UpdateFoodInFridgeDataOnDB().execute(data);
    }

    public void deleteFoodInFridgeDataOnDB(int _id) {
        new DeleteFoodInFridgeDataOnDB().execute(_id);
    }

    public void deleteFoodInFridgeDataOnDB(ArrayList<Integer> _ids) {
        new DeleteFoodInFridgeDataOnDB2().execute(_ids);
    }

    public ArrayList<FoodInFridgeContract.FIFData> getListOfFoodInFridge(String loc_code) {

        ArrayList<FoodInFridgeContract.FIFData> fif_list = new ArrayList<FoodInFridgeContract.FIFData>();

        db = db_helper.getReadableDatabase();

//        public static abstract class FoodInFridge implements BaseColumns {
//
//            public static final String TABLE_NAME = "FoodInFridge";
//            public static final String COLUMN_NAME_FOOD_ID = "food_id";
//            public static final String COLUMN_NAME_FOOD_NAME = "food_name";
//            public static final String COLUMN_NAME_REG_DATE = "reg_date";
//            public static final String COLUMN_NAME_EXP_DATE = "exp_date";
//            public static final String COLUMN_NAME_AMOUNT = "amount";
//            public static final String COLUMN_NAME_POSITION = "position";
//            public static final String COLUMN_NAME_HISTORY = "history";
//        }

        String[] projection = {
                FoodInFridgeContract.FoodInFridge._ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY
        };

        String sortOrder = FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE + " DESC";
        String selection = FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION + " LIKE \'" + loc_code + "%\'";
//        String loc = loc_code.substring(0, 4);
        //String[] selectionArgs = { loc_code };

        Cursor c = db.query(
                FoodInFridgeContract.FoodInFridge.TABLE_NAME,      // The table to query
                projection,                                 // The columns to return
                selection,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        if (c.getCount() != 0) {

            c.moveToFirst();
            while (!c.isAfterLast()) {
                FoodInFridgeContract.FIFData fif = new FoodInFridgeContract.FIFData();
                fif._id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge._ID));
                fif.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID));
                fif.food_name = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                try {
                    fif.reg_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE)));
                    fif.exp_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fif.amount = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT));
                fif.history = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY));
                fif.position = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION));
                fif_list.add(fif);

                c.moveToNext();
            }
        }

        return fif_list;
    }

    public String[] getIconPath(int food_id) {

        String[] icons = new String[2];

        if (food_id < 0) {
            icons[0] = null;
            icons[1] = null;
        }
        else {

            db = db_helper.getReadableDatabase();
            String[] projection = {
                    FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1,
                    FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2,
            };
            String selection = FoodInfoContract.FoodInfo._ID + " == ?";
            String[] selectionArgs = {String.valueOf(food_id)};

            Cursor c = db.query(
                    FoodInfoContract.FoodInfo.TABLE_NAME,      // The table to query
                    projection,                                 // The columns to return
                    selection,                                       // The columns for the WHERE clause
                    selectionArgs,                                       // The values for the WHERE clause
                    null,                                       // don't group the rows
                    null,                                       // don't filter by row groups
                    null                                   // The sort order
            );

            c.moveToFirst();

            icons[0] = Environment.getExternalStorageDirectory() + CONST_STRINGS.ICON_PATH
                    + c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1));
            icons[1] = Environment.getExternalStorageDirectory() + CONST_STRINGS.ICON_PATH
                    + c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2));
        }

        return icons;
    }

    public ArrayList<OtherClasses.SomeFood> getFoodList() {

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

        return whole_food_list;
    }

    public void getFoodList(OnGetFoodListHandler handler) {
        new GetFoodList().execute(handler);
    }

    public ArrayList<FoodInfoContract.FIData> getAllFoodInfo() {

        ArrayList<FoodInfoContract.FIData> all_food_info = new ArrayList<FoodInfoContract.FIData>();

        db = db_helper.getReadableDatabase();

//        public static abstract class FoodInfo implements BaseColumns {
//
//            public static final String TABLE_NAME = "FoodInfo";
//            public static final String COLUMN_NAME_FOOD_NAME = "food_name";
//            public static final String COLUMN_NAME_REC_EXP = "rec_exp";
//            public static final String COLUMN_NAME_ICON_IMG1 = "icon_img1";
//            public static final String COLUMN_NAME_ICON_IMG2 = "icon_img2";
//            public static final String COLUMN_NAME_FREQUENCY = "frequency";
//        }

        // for FoodInfo Table
        String[] projection = {
                FoodInfoContract.FoodInfo._ID,
                FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME,
                FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP,
                FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1,
                FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2,
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
            FoodInfoContract.FIData food_info = new FoodInfoContract.FIData();
            food_info.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo._ID));
            food_info.food_name = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FOOD_NAME));
            food_info.rec_exp = c.getInt(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_REC_EXP));
            food_info.icon_img1 = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG1));
            food_info.icon_img2 = c.getString(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_ICON_IMG2));
            food_info.frequency = c.getLong(c.getColumnIndexOrThrow(FoodInfoContract.FoodInfo.COLUMN_NAME_FREQUENCY));
            all_food_info.add(food_info);
            c.moveToNext();
        }

        return all_food_info;
    }

    public ArrayList<FoodInFridgeContract.FIFData> getJjamData(int a, int b) throws WrongTargetDateException {

        ArrayList<FoodInFridgeContract.FIFData> ret_list = new ArrayList<FoodInFridgeContract.FIFData>();

        if (a < -1 || a > b) {
            throw new WrongTargetDateException("nonono parameter shitt");
        }

        Date today = new Date();

        Calendar calendar = null;
        calendar = Calendar.getInstance(Locale.KOREA);
        calendar.add(Calendar.DATE, a);
        Date target_day1 = calendar.getTime();

        calendar = Calendar.getInstance(Locale.KOREA);
        calendar.add(Calendar.DATE, b);
        Date target_day2 = calendar.getTime();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss", Locale.KOREA);

        db = db_helper.getReadableDatabase();

        String[] projection = {
                FoodInFridgeContract.FoodInFridge._ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY
        };



        String sortOrder = FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE + " ASC";
        String selection = FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE + " >= Datetime(\'?\') AND "
                                + FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE + " <= Datetime(\'?\')";
        String[] selectionArgs = { simpleDateFormat.format(target_day1), simpleDateFormat.format(target_day2) };

        Cursor c = db.query(
                FoodInFridgeContract.FoodInFridge.TABLE_NAME,      // The table to query
                projection,                                 // The columns to return
                selection,                                       // The columns for the WHERE clause
                selectionArgs,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        if (c.getCount() != 0) {

            c.moveToFirst();
            while (!c.isAfterLast()) {
                FoodInFridgeContract.FIFData fif = new FoodInFridgeContract.FIFData();
                fif._id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge._ID));
                fif.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID));
                fif.food_name = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
                try {
                    fif.reg_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE)));
                    fif.exp_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                fif.amount = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT));
                fif.history = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY));
                fif.position = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION));
                ret_list.add(fif);

                c.moveToNext();
            }
        }

        return ret_list;
    }



    public class WrongTargetDateException extends Exception {
        public WrongTargetDateException(String message) {
            super(message);
        }
    }


    public FoodInFridgeContract.FIFData getFIF(int _id) {

        db = db_helper.getReadableDatabase();

        String[] projection = {
                FoodInFridgeContract.FoodInFridge._ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION,
                FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY
        };

        String selection = FoodInFridgeContract.FoodInFridge._ID + " == ?";
        String[] selectionArgs = { String.valueOf(_id) };

        Cursor c = db.query(
                FoodInFridgeContract.FoodInFridge.TABLE_NAME,      // The table to query
                projection,                                 // The columns to return
                selection,                                       // The columns for the WHERE clause
                selectionArgs,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                null                                  // The sort order
        );

        c.moveToFirst();

        FoodInFridgeContract.FIFData fif = new FoodInFridgeContract.FIFData();
        fif._id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge._ID));
        fif.food_id = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID));
        fif.food_name = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        try {
            fif.reg_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_REG_DATE)));
            fif.exp_date = dateFormat.parse(c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_EXP_DATE)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        fif.amount = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_AMOUNT));
        fif.history = c.getInt(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_HISTORY));
        fif.position = c.getString(c.getColumnIndexOrThrow(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_POSITION));

        return fif;
    }


    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */
    /* ******************************************************************************************************** */


    /*
     *      Private methods and variables
     */

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Bingo.db";

    private BingoDbHelper db_helper;
    private SQLiteDatabase db;

    private Context context;

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


    /*      Start
     *
     *      FoodInfo 테이블 데이터 쓰기 및 수정
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

    private void updateFoodInfoDataOnTable() {

    }


    /*      End
     *
     *      FoodInfo 테이블 데이터 쓰기 및 수정
     */


    /*      Start
     *
     *      FoodInFridge 테이블 데이터 쓰기, 수정, 삭제
     */

    private class SavingFoodInFridgeDataToDB extends AsyncTask<FoodInFridgeContract.FIFData, Void, Void> {
        @Override
        protected Void doInBackground(FoodInFridgeContract.FIFData... fif_arr) {

            db = db_helper.getWritableDatabase();

            int param_cnt = fif_arr.length;
            for (int i = 0; i < param_cnt; i++) {

                FoodInFridgeContract.FIFData fif = fif_arr[i];

                ContentValues values = new ContentValues();
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID, fif.food_id);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME, fif.food_name);
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

    private class UpdateFoodInFridgeDataOnDB extends AsyncTask<FoodInFridgeContract.FIFData, Void, Void> {
        @Override
        protected Void doInBackground(FoodInFridgeContract.FIFData... fif_arr) {

            db = db_helper.getReadableDatabase();

            int param_cnt = fif_arr.length;
            for (int i = 0; i < param_cnt; i++) {
                FoodInFridgeContract.FIFData fif = fif_arr[i];

                ContentValues values = new ContentValues();
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_ID, fif.food_id);
                values.put(FoodInFridgeContract.FoodInFridge.COLUMN_NAME_FOOD_NAME, fif.food_name);
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

    private class DeleteFoodInFridgeDataOnDB2 extends AsyncTask<ArrayList<Integer>, Void, Void> {
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

    /*      End
     *
     *      FoodInFridge 테이블 데이터
     */


    /*      Start
     *
     *      FoodInfo 데이터 가져와서 핸들러에서 처리
     */

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

    /*      End
     *
     *      FoodInfo 데이터 가져와서 핸들러에서 처리
     */


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


}


