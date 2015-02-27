package com.thanksbingo.bingo.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.R;
import com.thanksbingo.bingo.TabFragment.TabFirstDoor;
import com.thanksbingo.bingo.TabFragment.TabFirstIn;
import com.thanksbingo.bingo.TabFragment.TabSecondDoor;
import com.thanksbingo.bingo.TabFragment.TabSecondIn;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.FoodInfoContract;

import java.util.ArrayList;


public class MyBingoFragment extends Fragment {
    View v;

    ArrayList<String> howManyRow = null;
    String whatFridge = null;

    Button tab01;
    Button tab02;
    Button tab03;
    Button tab04;

    private OnFragmentInteractionListener mListener;

    public static MyBingoFragment newInstance() {
        MyBingoFragment fragment = new MyBingoFragment();
        return fragment;
    }

    public MyBingoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BingoDB bingoDB = new BingoDB(this.getActivity().getApplicationContext());
        ArrayList<FoodInfoContract.FIData> a = bingoDB.getAllFoodInfo();
        for (int i = 0; i < a.size(); i++) {
            Log.i(CONST_STRINGS.BINGO_LOG, "num " + i + ", " + a.get(i).food_name + ", " + a.get(i).icon_img1);
        }
        SharedPreferences sp = this.getActivity().getApplicationContext().getSharedPreferences(CONST_STRINGS.SP_FILE_KEY, Context.MODE_PRIVATE);
        int last_history = sp.getInt(CONST_STRINGS.SP_FOOD_INFO_LAST_HISTORY, 0);
        Log.i(CONST_STRINGS.BINGO_LOG, "last,, " + last_history);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = (RelativeLayout) inflater.inflate(R.layout.fragment_fridge_row_image, container, false);

        ListView lv = (ListView) v.findViewById(R.id.main_list);

        tab01 = (Button) v.findViewById(R.id.main_tab01);
        tab02 = (Button) v.findViewById(R.id.main_tab02);
        tab03 = (Button) v.findViewById(R.id.main_tab03);
        tab04 = (Button) v.findViewById(R.id.main_tab04);

        tab01.setOnClickListener(mTabClickListener);
        tab02.setOnClickListener(mTabClickListener);
        tab03.setOnClickListener(mTabClickListener);
        tab04.setOnClickListener(mTabClickListener);

        //처음 실행될때 냉장실 안이 선택되어 있는 상태로
        Fragment tabFirstIn = new TabFirstIn();
        FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
        transaction2.replace(R.id.tab_container, tabFirstIn).commit();

//        tab02.setBackgroundColor(Color.parseColor("#7ae4dd"));
//        tab02.setTextColor(Color.parseColor("#ffffff"));
        tab01.setBackgroundColor(Color.parseColor("#ffffff"));
        tab01.setTextColor(Color.parseColor("#4ac6be"));
        tab02.setBackgroundColor(Color.parseColor("#7ae4dd"));
        tab02.setTextColor(Color.parseColor("#ffffff"));
        tab03.setBackgroundColor(Color.parseColor("#ffffff"));
        tab03.setTextColor(Color.parseColor("#4ac6be"));
        tab04.setBackgroundColor(Color.parseColor("#ffffff"));
        tab04.setTextColor(Color.parseColor("#4ac6be"));

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private View.OnClickListener mTabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //냉장실문
                case R.id.main_tab01:
//                    Toast.makeText(v.getContext(), "냉장실문", Toast.LENGTH_SHORT).show();
                    FragmentManager fm = getChildFragmentManager();

                    Fragment tabFirstDoor = new TabFirstDoor();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.tab_container, tabFirstDoor).commit();

                    tab01.setBackgroundColor(Color.parseColor("#7ae4dd"));
                    tab01.setTextColor(Color.parseColor("#ffffff"));
                    tab02.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab02.setTextColor(Color.parseColor("#4ac6be"));
                    tab03.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab03.setTextColor(Color.parseColor("#4ac6be"));
                    tab04.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab04.setTextColor(Color.parseColor("#4ac6be"));

                    break;
                //냉장실안
                case R.id.main_tab02:
//                    Toast.makeText(v.getContext(), "냉장실안", Toast.LENGTH_SHORT).show();
                    Fragment tabFirstIn = new TabFirstIn();
                    FragmentTransaction transaction2 = getChildFragmentManager().beginTransaction();
                    transaction2.replace(R.id.tab_container, tabFirstIn).commit();


                    tab01.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab01.setTextColor(Color.parseColor("#4ac6be"));
                    tab02.setBackgroundColor(Color.parseColor("#7ae4dd"));
                    tab02.setTextColor(Color.parseColor("#ffffff"));
                    tab03.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab03.setTextColor(Color.parseColor("#4ac6be"));
                    tab04.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab04.setTextColor(Color.parseColor("#4ac6be"));

                    break;
                //냉동실문
                case R.id.main_tab03:
//                    Toast.makeText(v.getContext(), "냉동실문", Toast.LENGTH_SHORT).show();
                    Fragment tabSecondDoor = new TabSecondDoor();
                    FragmentTransaction transaction3 = getChildFragmentManager().beginTransaction();
                    transaction3.replace(R.id.tab_container, tabSecondDoor).commit();

                    tab01.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab01.setTextColor(Color.parseColor("#4ac6be"));
                    tab02.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab02.setTextColor(Color.parseColor("#4ac6be"));
                    tab03.setBackgroundColor(Color.parseColor("#7ae4dd"));
                    tab03.setTextColor(Color.parseColor("#ffffff"));
                    tab04.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab04.setTextColor(Color.parseColor("#4ac6be"));

                    break;
                //냉동실 안
                case R.id.main_tab04:
//                    Toast.makeText(v.getContext(), "냉동실안", Toast.LENGTH_SHORT).show();
                    Fragment tabSecondIn = new TabSecondIn();
                    FragmentTransaction transaction4 = getChildFragmentManager().beginTransaction();
                    transaction4.replace(R.id.tab_container, tabSecondIn).commit();

                    tab01.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab01.setTextColor(Color.parseColor("#4ac6be"));
                    tab02.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab02.setTextColor(Color.parseColor("#4ac6be"));
                    tab03.setBackgroundColor(Color.parseColor("#ffffff"));
                    tab03.setTextColor(Color.parseColor("#4ac6be"));
                    tab04.setBackgroundColor(Color.parseColor("#7ae4dd"));
                    tab04.setTextColor(Color.parseColor("#ffffff"));

                    break;
            }
        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
