package com.thanksbingo.bingo.TabFragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.thanksbingo.bingo.Adapter.HorizontalListViewAdapter;
import com.thanksbingo.bingo.AlertDialog.EditFoodFragment;
import com.thanksbingo.bingo.AlertDialog.ViewFoodFragment;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;

public class TabFirstDoor extends Fragment {
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

    private String foodname;
    private int count;
    private String boughtdate;
    private String expirydate;

    ArrayList<String> howManyFridge = null;
    String whatFridge = null;

    private OnFragmentInteractionListener mListener;

    public static TabFirstDoor newInstance(String foodname, int count, String boughtdate, String expirydate) {
        TabFirstDoor fragment = new TabFirstDoor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, foodname);
        args.putInt(ARG_PARAM2, count);
        args.putString(ARG_PARAM3, boughtdate);
        args.putString(ARG_PARAM4, expirydate);
        fragment.setArguments(args);
        return fragment;
    }

    public TabFirstDoor() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        foodList = new ArrayList<Food>();
//
//        BingoDB bingoDB = new BingoDB(getActivity().getApplicationContext());
//        ArrayList<FoodInFridgeContract.FIFData> fif_list = bingoDB.getListOfFoodInFridge("0A01");
//        for (int i = 0; i < fif_list.size(); i++) {
//            Food f = new Food();
//            f.fif = fif_list.get(i);
//            f.flagFooter = false;
//            foodList.add(f);
//        }
//        Food f = new Food();
//        f.fif = null;
//
//        f.flagFooter = true;
//        foodList.add(f);

//        foodList.add(new Food("사과", 3, "2011-01-31","2011-01-31"));
//        foodList.add(new Food("베리", 4, "2011-01-31", "2015-02-01"));
//        //can_beer, can_cola
//        foodList.add(new Food("맥주", 7, "2011-01-31", "2015-01-02"));
//        foodList.add(new Food("콜라",9, "2011-01-31", "2015-02-02"));
//        foodList.add(new Food("당근", 12, "2011-01-31", "2015-03-30"));
//        foodList.add(new Food("치킨", 15, "2011-01-31", "2015-04-31"));
//        foodList.add(new Food("clam", 2, "2011-01-31", "2015-05-01"));
        //DD
//        Food f = new Food("flag", "flag");
//        f.flagFooter = true;
//        foodList.add(f);
//        if (getArguments() != null) {
//            foodname = getArguments().getString(ARG_PARAM1);
//            count = getArguments().getInt(ARG_PARAM2);
//            boughtdate = getArguments().getString(ARG_PARAM3);
//            expirydate = getArguments().getString(ARG_PARAM4);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


//        v = (RelativeLayout) inflater.inflate(R.layout.activity_fridge_row2, container, false);

        v = (RelativeLayout) inflater.inflate(R.layout.tab_fragment, container, false);
/*

        int rowHeight = 3;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, rowHeight);
        v.setLayoutParams(lp);
*/

//        if (getArguments() != null) {
//            foodname = getArguments().getString(ARG_PARAM1);
//            count = getArguments().getInt(ARG_PARAM2);
//            boughtdate = getArguments().getString(ARG_PARAM3);
//            expirydate = getArguments().getString(ARG_PARAM4);
//        }

//        //아이콘들을 붙일 레이아웃 동적 생성
//        HorizontalListView listview = (HorizontalListView) v.findViewById(R.id.lv_fridge_img1);
//        adapter = new FridgeRowAdapter(getActivity().getApplicationContext(), foodList);
//
//        listview.setAdapter(adapter);
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Food f = (Food) parent.getAdapter().getItem(position);
//
//                if(f.flagFooter == true){
//                    callViewFoodDialog("0A01");
//                } else {
//                // 물품이름, 갯수, 등록일자, 유통기한
//
//                callEditFoodDialog(f.fif.food_name, f.fif.amount, f.fif.reg_date.toString(), f.fif.exp_date.toString());
//
//                }
//
//            }
//        });

        //해당 칸에 선반 갯수가 몇개인지
        howManyFridge = new ArrayList<>();
        howManyFridge.add("2");
        howManyFridge.add("3");
        howManyFridge.add("4");
//        howManyFridge.add("5");
//        howManyFridge.add("6");

        //냉장고 어느 칸인지
        whatFridge = "0A";


        ListView lv = (ListView) v.findViewById(R.id.main_list);
        HorizontalScrollView hsv = (HorizontalScrollView) v.findViewById(R.id.main_horizontal);
        mAdapter = new HorizontalListViewAdapter(getActivity().getApplicationContext(), howManyFridge, whatFridge);
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
        f.show(fm, "");
    }


    // 음식물 수정 및 보기 Fragment
    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate) {

        FragmentManager fm = getActivity().getSupportFragmentManager();

        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
        f.show(fm, "");
    }

//    private void callUpFridgeFragment(int pos, int rowNum, FridgeRow.Style style) {
//        Fragment existingFrag = getExistingFridgeFragment();
//        FragmentTransaction t = getChildFragmentManager().beginTransaction();
//        Toast.makeText(getActivity(), "pos: " + pos + " rowNum: " + rowNum + " style : " + style, Toast.LENGTH_SHORT).show();
//        if (existingFrag != null) {     //만약 이미 떠있는 fridgefragment가있으면 replace를한다.
//            t.replace(R.id.main_fragment_container, FridgeFragment.newInstance(pos, rowNum, style), FRIDGE_FRAGMENT_TAG);
//            t.commit();
//            return;
//        }
//        t.add(R.id.main_fragment_container, FridgeFragment.newInstance(pos, rowNum, style), FRIDGE_FRAGMENT_TAG).commit();
//    }
//
//    private Fragment getExistingFridgeFragment() {
//        return getChildFragmentManager().findFragmentByTag(FRIDGE_FRAGMENT_TAG);
//    }


}
