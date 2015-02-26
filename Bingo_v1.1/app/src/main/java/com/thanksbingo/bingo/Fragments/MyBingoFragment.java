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
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.FoodInFridgeContract;

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
        BingoDB bingoDB = new BingoDB(getActivity().getApplicationContext());
        ArrayList<FoodInFridgeContract.FIFData> fif_list = bingoDB.getListOfFoodInFridge("0A01");
        for (int i = 0; i < fif_list.size(); i++) {
            Food f = new Food();
            f.fif = fif_list.get(i);
            f.flagFooter = false;
            foodList.add(f);
        }
        Food f = new Food();
        f.fif = null;
        f.flagFooter = true;
        foodList.add(f);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = (RelativeLayout) inflater.inflate(R.layout.activity_fridge_row2, container, false);


        //아이콘들을 붙일 레이아웃 동적 생성
        HorizontalListView listview = (HorizontalListView) v.findViewById(R.id.lv_fridge_img1);
        adapter = new FridgeRowAdapter(getActivity().getApplicationContext(), foodList);

        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Food f = (Food) parent.getAdapter().getItem(position);

                if(f.flagFooter == true){
                    callViewFoodDialog("0A01");
                } else {
                // 물품이름, 갯수, 등록일자, 유통기한
                callEditFoodDialog(f.fif.food_name, f.fif.amount, f.fif.reg_date.toString(), f.fif.exp_date.toString());

                }

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
    private void callViewFoodDialog(String loc_code) {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        ViewFoodFragment f = ViewFoodFragment.newInstance(loc_code, "Hi");
        f.show(fm,"");
    }


    // 음식물 수정 및 보기 Fragment
    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate ) {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
        f.show(fm,"");
    }

}
