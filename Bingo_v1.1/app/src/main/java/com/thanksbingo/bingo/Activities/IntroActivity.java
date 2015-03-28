package com.thanksbingo.bingo.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.Entities.DoorNo;
import com.thanksbingo.bingo.R;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.OtherClasses;
import com.thanksbingo.httpclient.BingoHttpClient;

import org.apache.http.Header;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class IntroActivity extends ActionBarActivity {

    private Intent i;             //계정 유무를 통해 어느 페이지로 이동할지 정할 intent
    private Thread background;

    public static final String INITIAL_UPDATE = "initial update";
    public static final String NEED_UPDATE = "need update";

    public static final String JSON_DATA = "json data";
    public static final String MSG = "msg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        //귵님코드
        BingoHttpClient client = new BingoHttpClient();
        String subURL = null;
        final String msg;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        if (!sharedPref.contains(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY)) {
            subURL = "bingo_api/get_food_info/";
            msg = INITIAL_UPDATE;
        }
        else {
            int last_history = 0;
            last_history = sharedPref.getInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, 0);
            subURL = "bingo_api/update_food_info/" + last_history + "/";
            msg = NEED_UPDATE;
        }
        client.sendGetRequest(subURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String resp_json = new String(responseBody, "UTF-8");
                    Log.i(CONST_STRINGS.BINGO_LOG, "RRR " + resp_json);
                    Intent intent = new Intent(IntroActivity.this, VideoActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(JSON_DATA, resp_json);
                    intent.putExtra(MSG, msg);
                    startActivity(intent);
                    finish();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
}
