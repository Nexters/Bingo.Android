package com.thanksbingo.bingo.AlertDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.thanksbingo.bingo.Calendar.CalendarFragment;
import com.thanksbingo.bingo.R;

public class EditFoodFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";



    final EditText edit_food_fragment_foodname = null;
    final EditText edit_food_fragment_foodnum = null;
    final EditText edit_food_fragment_entered_date = null;
    final EditText edit_food_fragment_edit_expiry =null;

    // TODO: Rename and change types of parameters
    private String foodname;
    private int count;
    private String boughtdate;
    private String expirydate;

    public EditFoodFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static EditFoodFragment newInstance(String foodname, int count, String boughtdate, String expirydate) {
        EditFoodFragment fragment = new EditFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, foodname);
        args.putInt(ARG_PARAM2, count);
        args.putString(ARG_PARAM3, boughtdate);
        args.putString(ARG_PARAM4, expirydate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);            //Titlebar 없앰
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View v = inflater.inflate(R.layout.fragment_edit_food, container, false);


        //가져온 값을 EditText에 추가합니다.
        final EditText edit_food_fragment_foodname = (EditText)v.findViewById(R.id.edit_food_fragment_foodname);
        final EditText edit_food_fragment_foodnum = (EditText)v.findViewById(R.id.edit_food_fragment_foodnum);
        final EditText edit_food_fragment_entered_date = (EditText)v.findViewById(R.id.edit_food_fragment_entered_date);
        final EditText edit_food_fragment_edit_expiry = (EditText)v.findViewById(R.id.edit_food_fragment_edit_expiry);

        //값을 보여줍니다.
        edit_food_fragment_foodname.setText(foodname);
        edit_food_fragment_foodnum.setText(count+"");
        edit_food_fragment_entered_date.setText(boughtdate);
        edit_food_fragment_edit_expiry.setText(expirydate);


        //Calendar 추가
        CalendarFragment c = new CalendarFragment();
        FragmentManager m = getChildFragmentManager();
        FragmentTransaction t = m.beginTransaction();
        t.add(R.id.edit_food_fragment_calendar_container, c).commit();


        //취소시 실행
        Button cancelbtn = (Button)v.findViewById(R.id.edit_food_fragment_btn_cancel);
        cancelbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        // 수정시 실행
        Button changebtn = (Button)v.findViewById(R.id.edit_food_fragment_btn_change);
        changebtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {


                showAlertDialog();
            }
        });


        return v;
    }


    //AlertDialog()를 사용함 ---> custom이 필요하니까 이건 나중 문제로 합시다!
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("수정하시겠습니까?").setCancelable(true)
                .setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //getText해서 그 값을 setText해준다.
                foodname=  edit_food_fragment_foodname.getText().toString();
                count = Integer.getInteger(edit_food_fragment_foodnum.getText().toString());
                boughtdate = edit_food_fragment_entered_date.getText().toString();
                expirydate = edit_food_fragment_edit_expiry.getText().toString();




                dismissDialog();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onResume() {
        //없어질때 fade out 하는 애니메이션이죠
        getDialog().getWindow().setWindowAnimations(R.style.dialog_fade_out_animation);
        super.onResume();

    }

    private void dismissDialog(){
        this.dismiss();
    }


}
