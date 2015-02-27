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
import com.thanksbingo.bingo.speechtotext.SpeechToText;
import com.thanksbingo.db.BingoDB;

public class EditFoodFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


//    private static final String LOC_CODE = "LOC CODE";
    private static final String FIV_ID="FIV_ID";
    private static final String ARG_PARAM2 = "param2";

    public static EditText entered_date_text;
    public static EditText expiry_date_text;

    private EditText foodNameEditText;
    private EditText foodAmountText;

    private SpeechToText stt;

    BingoDB bingoDB;

    public static EditFoodFragment newInstance(String param1, String param2) {
        EditFoodFragment fragment = new EditFoodFragment();
        Bundle args = new Bundle();
        args.putString(FIV_ID, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(getArguments() != null) {

        }

        View v = inflater.inflate(R.layout.fragment_edit_food, container, false);

        bingoDB = new BingoDB(v.getContext());


        foodNameEditText = (EditText)v.findViewById(R.id.edit_food_fragment_foodname);
        foodAmountText = (EditText)v.findViewById(R.id.edit_food_fragment_foodnum);

        //등록일자와 유통기한 텍스트 두개
        entered_date_text = (EditText)v.findViewById(R.id.edit_food_fragment_entered_date);
        expiry_date_text = (EditText)v.findViewById(R.id.edit_food_fragment_edit_expiry);

        //Calendar 추가
        CalendarFragment c = new CalendarFragment();
        FragmentManager m = getChildFragmentManager();
        FragmentTransaction t = m.beginTransaction();
        t.add(R.id.edit_food_fragment_calendar_container, c).commit();

        Button confirmBtn = (Button)v.findViewById(R.id.edit_food_fragment_btn_change);
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        Button cancelBtn = (Button)v.findViewById(R.id.edit_food_fragment_btn_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        return v;
    }

    public static void refreshDatesText(String enteredDate, String expiryDate) {
        entered_date_text.setText(enteredDate);
        expiry_date_text.setText(expiryDate);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("수정하시겠습니까?").setCancelable(true)
                .setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dismissDialog();
//                        String food_name = foodNameEditText.getText().toString();
//                        food_name = food_name.replace(" ", "");

                        //아이디 값에 해당하는 음식 정보를 가져와서 edittext에 붙여넣기

                       

                       // fifd.

//                        FoodInFridgeContract.FIFData fif = new FoodInFridgeContract.FIFData();
//                        fif.food_name = food_name;
//                        fif.food_id = bingoDB.getFoodInfoIdOf(food_name);
//                        fif.amount = Integer.parseInt(foodAmountText.getText().toString());
//                        fif.position = getArguments().getString(LOC_CODE);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
//                        try {
//                            fif.reg_date = sdf.parse(entered_date_text.getText().toString());
//                            fif.exp_date = sdf.parse(expiry_date_text.getText().toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        fif.history = 0;

                        //bingoDB.writeDataToFoodInFridgeTable(fif);
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
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
        getDialog().getWindow().setWindowAnimations(R.style.dialog_fade_out_animation);
        super.onResume();

    }

    private void dismissDialog(){
        this.dismiss();
    }

}

