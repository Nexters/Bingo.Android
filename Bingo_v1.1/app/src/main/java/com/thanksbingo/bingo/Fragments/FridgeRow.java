package com.thanksbingo.bingo.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.thanksbingo.bingo.Adapter.FridgeRowAdapter;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;



public class FridgeRow extends Fragment {

    View v;
    private LinearLayout linearLayout;
    private FridgeRowAdapter adapter;
    ArrayList<Food> foodList;
    Food food;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = new ArrayList<Food>();
        foodList.add(new Food("2015-02-09", "2015-02-09"));
        foodList.add(new Food("2015-02-10", "2015-02-10"));
        foodList.add(new Food("2015-02-11", "2015-02-11"));
        foodList.add(new Food("2015-02-12", "2015-02-12"));
        foodList.add(new Food("2015-02-13", "2015-02-13"));
        foodList.add(new Food("2015-02-14", "2015-02-14"));
        foodList.add(new Food("2015-02-15", "2015-02-15"));
        foodList.add(new Food("2015-02-16", "2015-02-16"));
        foodList.add(new Food("2015-02-17", "2015-02-17"));
        foodList.add(new Food("anchovy", "2015-01-30"));
        foodList.add(new Food("사과", "2011-01-31"));
        foodList.add(new Food("베리", "2015-02-01"));
        //can_beer, can_cola
        foodList.add(new Food("맥주", "2015-01-02"));
        foodList.add(new Food("콜라", "2015-02-02"));
        foodList.add(new Food("당근", "2015-03-30"));
        foodList.add(new Food("치킨", "2015-04-31"));
        foodList.add(new Food("clam", "2015-05-01"));
        foodList.add(new Food("달걀", "2015-06-02"));
        foodList.add(new Food("생선", "2015-07-02"));
        foodList.add(new Food("leek", "2015-08-30"));
        //locknlock_1
        foodList.add(new Food("통1", "2014-09-31"));
        foodList.add(new Food("고기", "2013-02-01"));
        foodList.add(new Food("통2", "2005-02-02"));
        foodList.add(new Food("버섯", "2012-02-02"));
        foodList.add(new Food("양파", "2011-01-30"));
        foodList.add(new Food("통3", "2010-01-31"));
        foodList.add(new Food("감자", "2019-09-01"));
        foodList.add(new Food("새우", "2018-02-02"));
        foodList.add(new Food("오징어", "2017-02-02"));
        foodList.add(new Food("토마토", "2016-01-30"));
        foodList.add(new Food("요거트", "2015-01-01"));
        foodList.add(new Food("통4", "2025-02-01"));
        foodList.add(new Food("통5", "2005-02-02"));
        foodList.add(new Food("앤초비", "1995-02-20"));
        foodList.add(new Food("딸기", "1999-01-30"));
        foodList.add(new Food("사과", "2015-02-31"));
        foodList.add(new Food("당근", "2015-03-01"));
        foodList.add(new Food("치킨", "2015-04-02"));
        foodList.add(new Food("맥주", "2015-05-02"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = (RelativeLayout) inflater.inflate(
                R.layout.activity_fridge_row, container, false);
/*

        int rowHeight = 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight);
        v.setLayoutParams(lp);
*/

        //아이콘들을 붙일 레이아웃 동적 생성
        HorizontalListView listview = (HorizontalListView) v.findViewById(R.id.lv_fridge_img);
        adapter = new FridgeRowAdapter(getActivity().getApplicationContext(), foodList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food f = (Food) parent.getAdapter().getItem(position);
				Toast.makeText(getActivity(), f.getFoodName(), Toast.LENGTH_SHORT)
						.show();
            }
        });


        return v;
    }

}

