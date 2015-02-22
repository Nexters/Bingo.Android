package com.thanksbingo.bingo.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.devsmart.android.ui.HorizontalListView;
import com.thanksbingo.bingo.Adapter.FridgeRowAdapter;
import com.thanksbingo.bingo.Adapter.HorizontalListViewAdapter;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FridgeRow extends Fragment {
    private static final String ARG_ROWHEIGHT = "rowheight";

    private int rowHeight;

    ArrayList<Food> foodList = null;
    private HorizontalListViewAdapter mAdapter;
    View v;
//    ImageView icon;
//    TextView fName;
//    TextView dday;


    public FridgeRow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = new ArrayList<Food>();
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
        Food f = new Food("flag", "flag");
        f.flagFooter = true;
        foodList.add(f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = (RelativeLayout) inflater.inflate(R.layout.fragment_fridge_row_image, container, false);

        ListView lv = (ListView) v.findViewById(R.id.main_list);
        HorizontalScrollView hsv = (HorizontalScrollView) v.findViewById(R.id.main_horizontal);
        mAdapter = new HorizontalListViewAdapter(getActivity().getApplicationContext(), foodList,2);
        lv.setAdapter(mAdapter);
        return v;
    }


}
