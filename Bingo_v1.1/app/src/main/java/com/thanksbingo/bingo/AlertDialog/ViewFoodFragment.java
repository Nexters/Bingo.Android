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
import android.widget.ImageView;

import com.thanksbingo.bingo.Calendar.CalendarFragment;
import com.thanksbingo.bingo.R;
import com.thanksbingo.bingo.speechtotext.SpeechToText;


public class ViewFoodFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static EditText entered_date_text;
    public static EditText expiry_date_text;

    private EditText foodNameEditText;
    private SpeechToText stt;
    /*
    담이누나
    이 ViewFoodFragment가 달력이 있는 창이야
    음식물을 눌렀을 때 뜨는 창이 이거야
    음식을 클릭했을 때, 그 클릭한 음식에 대한 정보를 가지고 이 fragment가 열려야할거아니야?
    그럼 takMainActivity.class에 있는 callViewFoodDialog() 메쏘드처럼 하면 돼.
    거기에보면 ViewFoodFragment.newInstance(~~)가 있잖아
    지금은 String값을 파라미터로 넘겨주지만 나중엔 누나가 String대신 그 음식의 정보를 넘겨주면 되겠지.
    그럼 이 fragment에서 onCreateView에서 그 정보들을 가져오는거야
    밑에 보면 if(getArguments() != null) {} 있잖아. 여기서 가져오는거야
    이렇게만하면될듯!!!!

     */
    public static ViewFoodFragment newInstance(String param1, String param2) {
        ViewFoodFragment fragment = new ViewFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);            //Titlebar 없앰
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(getArguments() != null) {
           /*
           Bundle args = getArguments();
           String hello = args.getString(~~);
            */
        }

        View v = inflater.inflate(R.layout.view_food_fragment, container, false);

        //음성인식 버튼 달기
        foodNameEditText = (EditText)v.findViewById(R.id.view_food_fragment_edit_foodname);
        ImageView mic = (ImageView)v.findViewById(R.id.view_food_fragment_image_mic);
        stt = new SpeechToText(getActivity());
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stt.setUpSTT();
                stt.startSTT(new SpeechToText.OnCallbackSTT() {
                    @Override
                    public void onCallbackSTT(String result) {
                        foodNameEditText.setText(result);
                    }
                });
            }
        });

        //등록일자와 유통기한 텍스트 두개
        entered_date_text = (EditText)v.findViewById(R.id.view_food_fragment_edit_entered_date);
        expiry_date_text = (EditText)v.findViewById(R.id.view_food_fragment_edit_expiry);

        //Calendar 추가
        CalendarFragment c = new CalendarFragment();
        FragmentManager m = getChildFragmentManager();
        FragmentTransaction t = m.beginTransaction();
        t.add(R.id.view_food_fragment_calendar_container, c).commit();

        Button confirmBtn = (Button)v.findViewById(R.id.view_food_fragment_btn_confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        Button cancelBtn = (Button)v.findViewById(R.id.view_food_fragment_btn_cancel);
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
        builder.setMessage("등록하시겠습니까?").setCancelable(true)
                .setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismissDialog();
                    }
                }).setNegativeButton("아뇨", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onResume() {

        //없어질때 fade out 하는 애니메이션
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation);
        super.onResume();

    }

    private void dismissDialog(){
        this.dismiss();
    }

    //EditFoodFragment 창을 불러낸다.
    //나중에는 bundle에 현재 food의 information을 담아서 같이 보내줘서
    //EditFoodFragment에 현재 저장되어 있는 정보를 보여준다.
    /*
    private void callEditFoodFragment() {
        Dialog d = this.getDialog();
        FragmentManager m = getFragmentManager();
        d.dismiss();        //ViewFoodDialog는 없앤다.
        EditFoodFragment e = new EditFoodFragment();        //EditFoodFragment를 부른다.
        e.show(m,"");    //Tag는 일단 그냥 빈 상태로 둔다.
    }*/
}
