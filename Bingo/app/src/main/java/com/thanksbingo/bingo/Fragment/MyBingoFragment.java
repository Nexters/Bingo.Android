package com.thanksbingo.bingo.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.thanksbingo.bingo.Etc.FridgeRow;
import com.thanksbingo.bingo.R;

import java.util.HashMap;
import java.util.Map;


public class MyBingoFragment extends Fragment {
    private Toolbar actionBar;
    private Button tab01;
    private Button tab02;
    private Button tab03;
    private Button tab04;

    private Button btn;
    private Button btn2;
    private final String FRIDGE_FRAGMENT_TAG = "fridge_fragment";

    private Map<Integer, Integer> fridgeAndRowMap = new HashMap<Integer, Integer>();

    private FridgeRow.Style currentStyle;
    private int currentFridgeFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.activity_main, container, false);
        //테스트하기위한 임시 코드
        fridgeAndRowMap.put(0, 2);
        fridgeAndRowMap.put(1,1);
        fridgeAndRowMap.put(2,4);
        fridgeAndRowMap.put(3,3);
        currentStyle = FridgeRow.Style.IMAGE;

        // Set up the action bar.
        // actionBar = (Toolbar)findViewById(R.id.actionBar);
        // setSupportActionBar(actionBar);

        //toolbar의 alarm image click을 테스트해보기위함
        //ImageView actionbar_alarm = (ImageView)findViewById(R.id.actionbar_alarm);
        //actionbar_alarm.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Toast.makeText(getApplicationContext(), "alarm clicked", Toast.LENGTH_SHORT).show();
        //        Intent i = new Intent(getApplicationContext(), ExpiryListActivity.class);
        //        startActivity(i);
        //        return;
        //    }
        //});

        //tab을 위장한 버튼 4개들
        tab01 = (Button)main.findViewById(R.id.main_tab01);
        tab02 = (Button)main.findViewById(R.id.main_tab02);
        tab03 = (Button)main.findViewById(R.id.main_tab03);
        tab04 = (Button)main.findViewById(R.id.main_tab04);
        tab01.setOnClickListener(tabClickListener);
        tab02.setOnClickListener(tabClickListener);
        tab03.setOnClickListener(tabClickListener);
        tab04.setOnClickListener(tabClickListener);
        //tab01.performClick();   //맨 처음 시작될 때 tab 01이 기본으로 켜지도록

        btn = (Button)main.findViewById(R.id.main_btn_view_as_image);
        btn2 = (Button)main.findViewById(R.id.main_btn_view_as_list);
        btn.setOnClickListener(lowerButtonClickListener);
        btn2.setOnClickListener(lowerButtonClickListener);
        //btn.performClick();

        setUpInitialScreen();       //맨 처음 실행시켰을 때 처음 탭과 이미지로 보기가 설정되어있는걸로 한다.
        return main;
    }

    private void setUpInitialScreen() {
        currentStyle = FridgeRow.Style.IMAGE;
        currentFridgeFragment = 0;
        setLowerButtonSelected(btn);
        setTabSelected(tab01);
        callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
    }

    private void changeRowsToImage() {
        currentStyle = FridgeRow.Style.IMAGE;
        callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);

    }

    private void changeRowsToList() {
        currentStyle = FridgeRow.Style.LIST;
        callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
    }

    private View.OnClickListener lowerButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.isSelected()) return;
            setLowerButtonSelected(v);
            switch(v.getId()) {
                case R.id.main_btn_view_as_image:
                    changeRowsToImage();
                    break;
                case R.id.main_btn_view_as_list:
                    changeRowsToList();
                    break;
            }
        }
    };

    private View.OnClickListener tabClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if (v.isSelected()) return;     //만약 이미 선택되어있던 tab이면 아무 처리도 하지 않는다
            setTabSelected(v);         //tab 4개 중 선택한 버튼을 선택된 채로 유지하기 위한 method
            switch(v.getId()) {
                case R.id.main_tab01:
                    currentFridgeFragment = 0;
                    callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
                    break;
                case R.id.main_tab02:
                    currentFridgeFragment = 1;
                    callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
                    break;
                case R.id.main_tab03:
                    currentFridgeFragment = 2;
                    callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
                    break;
                case R.id.main_tab04:
                    currentFridgeFragment = 3;
                    callUpFridgeFragment(currentFridgeFragment, fridgeAndRowMap.get(currentFridgeFragment), currentStyle);
                    break;
            }
        }
    };

    //더러운 코드
    private void setTabSelected(View v) {
        tab01.setSelected(false);
        tab02.setSelected(false);
        tab03.setSelected(false);
        tab04.setSelected(false);
        v.setSelected(true);
    }

    private void setLowerButtonSelected(View v) {
        btn.setSelected(false);
        btn2.setSelected(false);
        v.setSelected(true);
    }

    private void callUpFridgeFragment(int pos, int rowNum, FridgeRow.Style style) {
        Fragment existingFrag = getExistingFridgeFragment();
        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        Toast.makeText(getActivity(), "pos: " + pos + " rowNum: " + rowNum + " style : " + style, Toast.LENGTH_SHORT).show();
        if (existingFrag != null) {     //만약 이미 떠있는 fridgefragment가있으면 replace를한다.
            t.replace(R.id.main_fragment_container, FridgeFragment.newInstance(pos, rowNum, style), FRIDGE_FRAGMENT_TAG);
            t.commit();
            return;
        }
        t.add(R.id.main_fragment_container, FridgeFragment.newInstance(pos, rowNum, style), FRIDGE_FRAGMENT_TAG).commit();
    }

    private Fragment getExistingFridgeFragment() {
        return getChildFragmentManager().findFragmentByTag(FRIDGE_FRAGMENT_TAG);
    }

    private void callViewFoodDialog() {
        FragmentManager fm = getChildFragmentManager();
        ViewFoodFragment f = new ViewFoodFragment();
        f.show(fm,"");
    }




}
