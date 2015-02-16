package com.thanksbingo.lasttest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.OtherClasses;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BingoDB bingoDB = new BingoDB(getApplicationContext());

        Button btn_load_db = (Button)findViewById(R.id.btn_load_db);
        btn_load_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int last_history = 0;
                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
                if (!sharedPref.contains(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY))
                    bingoDB.initiateFoodInfoDbAndStartFirstActivity(SecondActivity.class);
                else
                    bingoDB.updateFoodInfoDbAndStartFirstActivity(SecondActivity.class);
            }
        });

        Button btn_get_foodlist = (Button)findViewById(R.id.btn_get_foodlist);
        btn_get_foodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<OtherClasses.SomeFood> food_list = bingoDB.getFoodList();
                for (int i = 0; i < food_list.size(); i++) {
                    Log.i(CONST_STRINGS.BINGO_LOG, "" + food_list.get(i).food_name);
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
