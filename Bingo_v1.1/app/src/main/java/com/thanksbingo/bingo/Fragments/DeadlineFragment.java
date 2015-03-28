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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_deadline, container, false);

        return v;
    }


}
