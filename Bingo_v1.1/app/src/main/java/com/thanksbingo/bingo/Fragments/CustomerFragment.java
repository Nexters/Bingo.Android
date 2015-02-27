package com.thanksbingo.bingo.Fragments;


import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thanksbingo.bingo.AlertDialog.ViewFoodFragment;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;


// 임시로 데이터의 값을 가져오는 역할을 수행한다.
// 도현이 말대로 물건을 터치했을때 그 값에 대한 정보를 가져와서 EidtText에 그 값을 보여주고
// 수정을 하고 싶을 때 해당 값을 변경한 후 저장이 가능하도록 만든다.(db 값 변경)
// 이때 수정 시, AlertDialog 창을 띄워 진짜 수정할 것인지 물어본다.
// AlertDialog는 커스텀할 수 있도록 만든다.


// 1. 한솔이의 ViewFoodFragment를 가져와서 클릭시 띄워지도록 한다. ok
// 2. 디자인팀에서 넘겨받은 이미지처럼 수정을 해본다. ok
// 3. Food 클래스를 수정한다. -> 그냥 사용한다.
// 4. ArrayList<Food>를 만들어서 값을 저장하고 바꿔본다.
// 5. 나 화이팅!


public class CustomerFragment extends Fragment {

    ArrayList<Food> foodList;

    //String foodName, int count , String boughtDate, String expiryDate
    //foodList.add(new Food("foodName","count", "boughtDate" "expiryDate"));



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        foodList = new ArrayList<Food>();
        // 가짜 음식 리스트!
//        foodList.add(new Food("사과", 3, "2012-12-12", "2014-12-14"));
//        foodList.add(new Food("배", 1, "2012-02-12", "2014-05-17"));
//        foodList.add(new Food("양파", 7, "2014-04-22", "2014-11-14"));
//        foodList.add(new Food("고구마", 9, "2012-02-25", "2014-12-17"));

       // Log.d("고구마", "")
        getActivity().getWindow().getAttributes().format = PixelFormat.RGBA_8888;

    }




    public CustomerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customer, container, false);

        //음식물 정보 등록 -> ViewFoodFragment
        Button showinfo = (Button)v.findViewById(R.id.showinfo);
        showinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callViewFoodDialog();
            }
        });

        //음식물 정보 보기 및 수정 -> EidtFoodFragment
        Button editinfo = (Button)v.findViewById(R.id.editinfo);
        editinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // callEditFoodDialog();
            }
        });
         return v;
    }


    // 음식물 등록 Fragment
    private void callViewFoodDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();

        // AlertDialog에 값을 넘겨주는거에요
        ViewFoodFragment f = ViewFoodFragment.newInstance("Hello", "Hi");
        f.show(fm,"");
    }


//    // 음식물 수정 및 보기 Fragment
//    private void callEditFoodDialog(String foodname, int count, String boughtdate, String expirydate ) {
//
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//
//        // AlertDialog에 값을 넘겨주는거에요
//        EditFoodFragment f = EditFoodFragment.newInstance(foodname, count, boughtdate, expirydate);
//        f.show(fm,"");
//    }






}
