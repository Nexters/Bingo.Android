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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        v = (RelativeLayout) inflater.inflate(R.layout.tab_fragment, container, false);

        //해당 칸에 선반 갯수가 몇개인지
        DoorNo pref = new DoorNo(getActivity().getApplicationContext());
        int count = pref.getValue(CONST_STRINGS.FREEZER_DOOR_ROW_CNT, 0);
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
        whatFridge = "0A";

//        public HorizontalListViewAdapter(Context context, ArrayList<String> _howManyRow, String _whatFridge) {

        ListView lv = (ListView) v.findViewById(R.id.main_list);// 죽은건가? ㅋ 전체화면 풀어바
        HorizontalScrollView hsv = (HorizontalScrollView) v.findViewById(R.id.main_horizontal);
        mAdapter = new HorizontalListViewAdapter(getActivity().getApplicationContext(), howManyFridge, whatFridge, getActivity().getSupportFragmentManager());
        lv.setAdapter(mAdapter);
        return v;
    }
//도현이가 코드를 줬ㄴㄴ데 저기서 에러가 떠요
//            도현이가 하라고 한건
//    일단 tapfragment 를 자기가 준 파일로 모두 교체하고
//            myBingofragment에서 수정하고 그 정도였는데
//            준 fragment에서 에러가 나요 히힛
//            HorizontalListViewAdapter 쪽은 고친거 없대?
    //있어용ㅋㅋ 반영했어? ㅋ
    //넹넹 ㅇ반영도 하고 고칠라고 하다보니 조금 수정된 곳도 있고 망함...
    // ㅋㅋ 윤딧이 어케 넘겨줌?
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


//
//    // 음식물 수정 및 보기 Fragment
//    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate) {
//
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//
//        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
//        f.show(fm, "");
//    }

}
