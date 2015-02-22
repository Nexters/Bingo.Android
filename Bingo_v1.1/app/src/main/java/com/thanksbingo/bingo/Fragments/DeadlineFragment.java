package com.thanksbingo.bingo.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.thanksbingo.bingo.Adapter.DeadlineFirstAdapter;
import com.thanksbingo.bingo.Adapter.DeadlineSecondAdapter;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeadlineFragment extends Fragment {

    ArrayList<Food> foodList;
    DeadlineFirstAdapter first_Adapter;
    DeadlineSecondAdapter second_Adapter;

    //생성자
    public DeadlineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = new ArrayList<Food>();
//        foodList.add(new Food("anchovy", "2015-01-30"));
//        foodList.add(new Food("사과", "2011-01-31"));
//        foodList.add(new Food("베리", "2015-02-01"));
//        //can_beer, can_cola
//        foodList.add(new Food("맥주", "2015-01-02"));
//        foodList.add(new Food("콜라", "2015-02-02"));
//        foodList.add(new Food("당근", "2015-03-30"));
//        foodList.add(new Food("치킨", "2015-04-31"));
//        foodList.add(new Food("clam", "2015-05-01"));
//        foodList.add(new Food("달걀", "2015-06-02"));
//        foodList.add(new Food("생선", "2015-07-02"));
//        foodList.add(new Food("leek", "2015-08-30"));
//        //locknlock_1
//        foodList.add(new Food("통1", "2014-09-31"));
//        foodList.add(new Food("고기", "2013-02-01"));
//        foodList.add(new Food("통2", "2005-02-02"));
//        foodList.add(new Food("버섯", "2012-02-02"));
//        foodList.add(new Food("양파", "2011-01-30"));
//        foodList.add(new Food("통3", "2010-01-31"));
//        foodList.add(new Food("감자", "2019-09-01"));
//        foodList.add(new Food("새우", "2018-02-02"));
//        foodList.add(new Food("오징어", "2017-02-02"));
//        foodList.add(new Food("토마토", "2016-01-30"));
//        foodList.add(new Food("요거트", "2015-01-01"));
//        foodList.add(new Food("통4", "2025-02-01"));
//        foodList.add(new Food("통5", "2005-02-02"));
//        foodList.add(new Food("앤초비", "1995-02-20"));
//        foodList.add(new Food("딸기", "1999-01-30"));
//        foodList.add(new Food("사과", "2015-02-31"));
//        foodList.add(new Food("당근", "2015-03-01"));
//        foodList.add(new Food("치킨", "2015-04-02"));
//        foodList.add(new Food("맥주", "2015-05-02"));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deadline, container, false);

        //0-3
        ListView deadline_first = (ListView)v.findViewById(R.id.lv_first_deadline);
        first_Adapter = new DeadlineFirstAdapter(getActivity().getApplicationContext(), foodList);
        deadline_first.setAdapter(first_Adapter);

        //4-7
        ListView deadline_second = (ListView)v.findViewById(R.id.lv_second_deadline);
        second_Adapter = new DeadlineSecondAdapter(getActivity().getApplicationContext(), foodList);
        deadline_second.setAdapter(second_Adapter);

        return v;
    }


}
