package com.thanksbingo.bingo.TabFragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.Adapter.HorizontalListViewAdapter;
import com.thanksbingo.bingo.Entities.DoorNo;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;

public class TabSecondIn extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View v;
    private LinearLayout linearLayout;
    private HorizontalListViewAdapter mAdapter;

    ArrayList<Food> foodList;
    Food food;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";


    ArrayList<String> howManyFridge = null;
    String whatFridge = null;

    private OnFragmentInteractionListener mListener;

    public static TabSecondIn newInstance(String foodname, int count, String boughtdate, String expirydate) {
        TabSecondIn fragment = new TabSecondIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, foodname);
        args.putInt(ARG_PARAM2, count);
        args.putString(ARG_PARAM3, boughtdate);
        args.putString(ARG_PARAM4, expirydate);
        fragment.setArguments(args);
        return fragment;
    }

    public TabSecondIn() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodList = new ArrayList<Food>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = (RelativeLayout) inflater.inflate(R.layout.tab_fragment, container, false);

        //해당 칸에 선반 갯수가 몇개인지
        DoorNo pref = new DoorNo(getActivity().getApplicationContext());
        int count = pref.getValue(CONST_STRINGS.FRIDGE_IN_ROW_CNT, 0);
        howManyFridge = new ArrayList<>();
        if(count==2){
            howManyFridge.add("1");
            howManyFridge.add("2");
        }else if(count == 3){
            howManyFridge.add("1");
            howManyFridge.add("2");
            howManyFridge.add("3");
        }else if(count == 4){
            howManyFridge.add("1");
            howManyFridge.add("2");
            howManyFridge.add("3");
            howManyFridge.add("4");
        }else if(count == 5){
            howManyFridge.add("1");
            howManyFridge.add("2");
            howManyFridge.add("3");
            howManyFridge.add("4");
            howManyFridge.add("5");
        }else if(count == 6){
            howManyFridge.add("1");
            howManyFridge.add("2");
            howManyFridge.add("3");
            howManyFridge.add("4");
            howManyFridge.add("5");
            howManyFridge.add("6");
        }
        //냉장고 어느 칸인지
        whatFridge = "0D";

        ListView lv = (ListView) v.findViewById(R.id.main_list);

        HorizontalScrollView hsv = (HorizontalScrollView) v.findViewById(R.id.main_horizontal);
        mAdapter = new HorizontalListViewAdapter(getActivity().getApplicationContext(), howManyFridge, whatFridge, getActivity().getSupportFragmentManager());
        lv.setAdapter(mAdapter);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

    }



    // 음식물 수정 및 보기 Fragment
//    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate) {
//
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
//        f.show(fm, "");
//    }

}
