package com.thanksbingo.httpclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;


public class BingoHttpClient {

    private static final String BASE_URL = "http://www.thanksbingo.com/";
    private static String user_token = null;
    private static boolean isLogged = false;

    public static void loginToServer(String user_id, String user_pw, Context context) {

        AsyncHttpClient clientForLogin = new AsyncHttpClient();
        final String sub_url = "login/general_user/";

        String enc_user_pw = generateSHA256(generateSHA256(user_pw));
        user_token = getToken(context);

        RequestParams params = new RequestParams();
        params.add("USER_ID", user_id);
        params.add("USER_PW", enc_user_pw);
        params.add("USER_TOKEN", user_token);

        clientForLogin.post(BASE_URL + sub_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

                // login 성공시
                    // isLogged = true;

                // login 실패시
                    // ?
            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

    public static void updateExtraFoodOnServerDB(String food_name) {

        AsyncHttpClient _client = new AsyncHttpClient();
        final String sub_url = "bingo_api/update_extra_food/";

        RequestParams params = new RequestParams();
        params.add("food_name", food_name);

        _client.post(BASE_URL + sub_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

            }
        });
    }

//    public static void getFoodInfoJsonData() {
//
//        AsyncHttpClient clientForFoodInfo = new AsyncHttpClient();
//        final String subURL = "bingo_api/get_food_info/";
//
//        clientForFoodInfo.get(BASE_URL + subURL, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                Log.i("GET FOOD INFO", "SUCCESS: " + i);
//                try {
//                    String foodInfoList_json = new String(bytes, "UTF-8");
//                    Log.i("GET FOOD INFO", "JSON DATA: " + foodInfoList_json);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                Log.i("GET FOOD INFO", "FAILURE: " + i);
//            }
//        });
//    }



    /********************
     *                  *
        OBJECT METHODS
     *                  *
     ********************/

    private AsyncHttpClient client = null;

    public BingoHttpClient() {
        client = new AsyncHttpClient();
    }

    public void sendPostRequest(String sub_url, HashMap<String, String> params, AsyncHttpResponseHandler handler) {

        RequestParams req_params = new RequestParams(params);
        client.post(BASE_URL + sub_url, req_params, handler);
    }

    public void sendGetRequest(String sub_url, HashMap<String, String> params, AsyncHttpResponseHandler handler) {

        RequestParams req_params = new RequestParams(params);
        client.get(BASE_URL + sub_url, req_params, handler);
    }

    public boolean sendPostRequestByToken(String sub_url, HashMap<String, String> params, AsyncHttpResponseHandler handler) {

        if (!isLogged) {
            return false;
        }
        else {
            params.put("USER_TOKEN", user_token);
            RequestParams req_params = new RequestParams(params);
            client.get(BASE_URL + sub_url, req_params, handler);

            return true;
        }
    }



    /*********************
     *                   *
        PRIVATE METHODS
     *                   *
     *********************/

    private static String getToken(Context context) {

        final String FILE_KEY = "NEXTERS_BINGO";
        final String TOKEN_KEY = "user_token";
        final String NO_TOKEN = "no_token";
        final String PREV_DATE_KEY = "previous_date";

        String DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Date CUR_DATE = new Date();

        long cur_date = CUR_DATE.getTime();
        long prev_date = 0;

        String _token = null;

        SharedPreferences sharedPref = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);

        prev_date = sharedPref.getLong(PREV_DATE_KEY, prev_date);

        Log.i("AAA", "prev_date: " + prev_date);
        Log.i("AAA", "cur_date: " + cur_date);

        // TOKEN이 생성된지 만 하루 이상이 경과되었다면,,
        if (isMoreThanOneDayIntervalBetween(prev_date, cur_date)) {

            // 새로운 토큰을 생성하고 SharedPreference에 저장.
            _token = generateSHA256(DEVICE_ID + CUR_DATE.toString());

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(PREV_DATE_KEY, cur_date);
            editor.putString(TOKEN_KEY, _token);
            editor.commit();
        }
        else {

            _token = sharedPref.getString(TOKEN_KEY, NO_TOKEN);
            if (_token.compareTo(NO_TOKEN) == 0) {

                _token = generateSHA256(DEVICE_ID + CUR_DATE.toString());

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putLong(PREV_DATE_KEY, cur_date);
                editor.putString(TOKEN_KEY, _token);
            }
        }

        return _token;
    }

    private static boolean isMoreThanOneDayIntervalBetween(long prev_date, long cur_date) {
        // 1970/1/1 이후 경과된 시간을 milliseconds로 표현하는 prev_date와 cur_date의 간격이 하루 이상일 시에 return true , 그 외의 경우 return true

        long interval = cur_date - prev_date;
        int day_interval = (int)(((interval / 1000) / 60) / 60); // unit -> hour

        if (day_interval >= 24) {
            return true;
        }
        else {
            return false;
        }
    }

    private static String generateSHA256(String key){
        String SHA = "";
        try{
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(key.getBytes());
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();

        }catch(NoSuchAlgorithmException e){
            e.printStackTrace();
            SHA = null;
        }
        return SHA;
    }
}
