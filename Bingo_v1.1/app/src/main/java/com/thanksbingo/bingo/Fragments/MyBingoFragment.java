package com.thanksbingo.bingo.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.devsmart.android.ui.HorizontalListView;
import com.thanksbingo.bingo.Adapter.FridgeRowAdapter;
import com.thanksbingo.bingo.AlertDialog.EditFoodFragment;
import com.thanksbingo.bingo.AlertDialog.ViewFoodFragment;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyBingoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyBingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyBingoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    View v;
    private LinearLayout linearLayout;
    private FridgeRowAdapter adapter;
    ArrayList<Food> foodList;
    Food food;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";

    private String foodname;
    private int count;
    private String boughtdate;
    private String expirydate;

    private OnFragmentInteractionListener mListener;


    // TODO: Rename and change types and number of parameters
    // TODO: Rename and change types and number of parameters
    public static MyBingoFragment newInstance(String foodname, int count, String boughtdate, String expirydate) {
        MyBingoFragment fragment = new MyBingoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, foodname);
        args.putInt(ARG_PARAM2, count);
        args.putString(ARG_PARAM3, boughtdate);
        args.putString(ARG_PARAM4, expirydate);
        fragment.setArguments(args);
        return fragment;
    }

    public MyBingoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = new ArrayList<Food>();
        foodList.add(new Food("사과", 3, "2011-01-31","2011-01-31"));
        foodList.add(new Food("베리", 4, "2011-01-31", "2015-02-01"));
        //can_beer, can_cola
        foodList.add(new Food("맥주", 7, "2011-01-31", "2015-01-02"));
        foodList.add(new Food("콜라",9, "2011-01-31", "2015-02-02"));
        foodList.add(new Food("당근", 12, "2011-01-31", "2015-03-30"));
        foodList.add(new Food("치킨", 15, "2011-01-31", "2015-04-31"));
        foodList.add(new Food("clam", 2, "2011-01-31", "2015-05-01"));
        //DD
        Food f = new Food("flag", "flag");
        f.flagFooter = true;
        foodList.add(f);


        if (getArguments() != null) {
            foodname = getArguments().getString(ARG_PARAM1);
            count = getArguments().getInt(ARG_PARAM2);
            boughtdate = getArguments().getString(ARG_PARAM3);
            expirydate = getArguments().getString(ARG_PARAM4);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = (RelativeLayout) inflater.inflate(R.layout.activity_fridge_row1, container, false);
/*

        int rowHeight = 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight);
        v.setLayoutParams(lp);
*/

        if (getArguments() != null) {
            foodname = getArguments().getString(ARG_PARAM1);
            count = getArguments().getInt(ARG_PARAM2);
            boughtdate = getArguments().getString(ARG_PARAM3);
            expirydate = getArguments().getString(ARG_PARAM4);
        }



        //아이콘들을 붙일 레이아웃 동적 생성
        HorizontalListView listview = (HorizontalListView) v.findViewById(R.id.lv_fridge_img);
        adapter = new FridgeRowAdapter(getActivity().getApplicationContext(), foodList);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food f = (Food) parent.getAdapter().getItem(position);

//                Toast.makeText(getActivity(), f.getFoodName(), Toast.LENGTH_SHORT)
//                        .show();

                // 물품이름, 갯수, 등록일자, 유통기한
                callEditFoodDialog(f.getFoodName(), f.getCount(), f.getBoughtDate(), f.getExpiryDate());



            }
        });


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
            mListener = (OnFragmentInteractionListener) activity;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }



    // 음식물 등록 Fragment
    private void callViewFoodDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        ViewFoodFragment f = ViewFoodFragment.newInstance("Hello", "Hi");
        f.show(fm,"");
    }


    // 음식물 수정 및 보기 Fragment
    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate ) {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
        f.show(fm,"");
    }


}
