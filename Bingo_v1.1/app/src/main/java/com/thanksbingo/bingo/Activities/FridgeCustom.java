package com.thanksbingo.bingo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.Entities.DoorNo;
import com.thanksbingo.bingo.R;

/**
 * Created by yoonsun on 2015. 2. 12..
 */
public class FridgeCustom extends Activity{

    Spinner spinner_fridge;
    Spinner spinner_door1;
    Spinner spinner_door2;
    Spinner spinner_door3;
    Spinner spinner_door4;


    Integer dong_door, dong, jang_door, jang;

    Integer selection;

    Button btn_reset, btn_apply;

    TextView tv_dongdoor_one, tv_dong_one, tv_jang_one, tv_jangdoor_one;
    TextView tv_dongdoor_two, tv_dong_two, tv_jang_two, tv_jangdoor_two;

   // Long temp_initial, temp_total, temp_1, temp_2, temp_3, temp_4;

    String[] kind = {"양문형", "일체형"};
    String[] number = {"칸수","2", "3", "4", "5", "6"};

    ArrayAdapter<String> adapter_fridge, adapter1, adapter2, adapter3, adapter4;


    @Override
    protected void onCreate (Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.fridge_custom);
       // temp_initial = System.currentTimeMillis();
       // Log.i("initial", ""+temp_initial);
        allClear();

        spinner_fridge = (Spinner)findViewById(R.id.spinner_fridge);
        adapter_fridge = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kind);

        final LinearLayout onedoor = (LinearLayout)findViewById(R.id.one_door);
        final LinearLayout twodoor = (LinearLayout)findViewById(R.id.two_door);

        spinner_fridge.setAdapter(adapter_fridge);
        spinner_fridge.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                position = spinner_fridge.getSelectedItemPosition();
                if (position == 0) {
                    twodoor.setVisibility(View.VISIBLE);
                    onedoor.setVisibility(View.GONE);
                    allClear();
                    selection=2;
                } else {
                    twodoor.setVisibility(View.GONE);
                    onedoor.setVisibility(View.VISIBLE);
                    allClear();
                    selection=1;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, number);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, number);
        adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, number);
        adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, number);


        spinner_door1.setAdapter(adapter1);
        spinner_door2.setAdapter(adapter2);
        spinner_door3.setAdapter(adapter3);
        spinner_door4.setAdapter(adapter4);


        spinner_door1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_door1.getSelectedItemPosition();
                if (position == 0) {
                    tv_dongdoor_one.setText("");
                    tv_dongdoor_two.setText("");
                } else {
                    dong_door = position + 1;
                    if (selection == 2)
                        tv_dongdoor_two.setText(dong_door.toString());
                    else
                        tv_dongdoor_one.setText(dong_door.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        spinner_door2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_door2.getSelectedItemPosition();
                if (position == 0) {
                    tv_dong_one.setText("");
                    tv_dong_two.setText("");
                } else {
                    dong = position + 1;
                    if (selection == 2)
                        tv_dong_two.setText(dong.toString());
                    else
                        tv_dong_one.setText(dong.toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });


        spinner_door3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_door3.getSelectedItemPosition();
                if (position == 0) {
                    tv_jang_one.setText("");
                    tv_jang_two.setText("");
                } else {
                    jang = position + 1;
                    if (selection == 2)
                        tv_jang_two.setText(jang.toString());
                    else
                        tv_jang_one.setText(jang.toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });


        spinner_door4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                position = spinner_door4.getSelectedItemPosition();
                if (position == 0) {
                    tv_jangdoor_one.setText("");
                    tv_jangdoor_two.setText("");
                } else {
                    jang_door = position + 1;
                    if (selection == 2)
                        tv_jangdoor_two.setText(jang_door.toString());
                    else
                        tv_jangdoor_one.setText(jang_door.toString());
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){
            }
        });

        btn_reset = (Button)findViewById(R.id.button_reset);
        btn_apply = (Button)findViewById(R.id.button_apply);

        btn_reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_apply.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DoorNo pref = new DoorNo(getApplicationContext());
                pref.put(CONST_STRINGS.FREEZER_DOOR_ROW_CNT, dong_door);
                pref.put(CONST_STRINGS.FREEZER_IN_ROW_CNT, dong);
                pref.put(CONST_STRINGS.FRIDGE_DOOR_ROW_CNT, jang);
                pref.put(CONST_STRINGS.FRIDGE_IN_ROW_CNT, jang_door);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    public void allClear(){

        tv_dongdoor_one = (TextView)findViewById(R.id.dongdoor_one);
        tv_dong_one = (TextView)findViewById(R.id.dong_one);
        tv_jang_one = (TextView)findViewById(R.id.jang_one);
        tv_jangdoor_one = (TextView)findViewById(R.id.jangdoor_one);

        tv_dongdoor_two = (TextView)findViewById(R.id.dongdoor_two);
        tv_dong_two = (TextView)findViewById(R.id.dong_two);
        tv_jang_two = (TextView)findViewById(R.id.jang_two);
        tv_jangdoor_two = (TextView)findViewById(R.id.jangdoor_two);

        spinner_door1 = (Spinner)findViewById(R.id.spinner_1);
        spinner_door2 = (Spinner)findViewById(R.id.spinner_2);
        spinner_door3 = (Spinner)findViewById(R.id.spinner_3);
        spinner_door4 = (Spinner)findViewById(R.id.spinner_4);


        spinner_door1.setSelection(0);
        spinner_door2.setSelection(0);
        spinner_door3.setSelection(0);
        spinner_door4.setSelection(0);


    }

}
