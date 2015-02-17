package com.thanksbingo.bingo.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;


import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.R;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.OtherClasses;

import java.util.ArrayList;

public class IntroActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        //귵님코드
        final BingoDB bingoDB = new BingoDB(getApplicationContext());

        int last_history = 0;
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        if (!sharedPref.contains(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY))
            bingoDB.initiateFoodInfoDbAndStartFirstActivity(MainActivity.class);
        else
            bingoDB.updateFoodInfoDbAndStartFirstActivity(MainActivity.class);

        ArrayList<OtherClasses.SomeFood> food_list = bingoDB.getFoodList();
        for (int i = 0; i < food_list.size(); i++) {
            Log.i(CONST_STRINGS.BINGO_LOG, "" + food_list.get(i).food_name);
        }
    }

}
