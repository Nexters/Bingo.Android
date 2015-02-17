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

import com.thanksbingo.bingo.Calendar.CalendarFragment;
import com.thanksbingo.bingo.R;

public class EditFoodFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EditFoodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditFoodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditFoodFragment newInstance(String param1, String param2) {
        EditFoodFragment fragment = new EditFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE); //Titlebar 없앰
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(getArguments() != null) {
           /*
           Bundle args = getArguments();
           String hello = args.getString(~~);
            */
        }
        View v = inflater.inflate(R.layout.fragment_edit_food, container, false);

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
                dismissDialog();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onResume() {
        //없어질때 fade out 하는 애니메이션이죠
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation);
        super.onResume();

    }

    private void dismissDialog(){
        this.dismiss();
    }


}
