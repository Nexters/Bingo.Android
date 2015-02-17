package com.thanksbingo.bingo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//냉장고 이미지로보기 어댑터

public class DeadlineSecondAdapter extends ArrayAdapter<Food> {

    private Context context;
    ArrayList<Food> foodList;
    LayoutInflater inflater;
    View rootView;
    Food food;

    public DeadlineSecondAdapter(Context context, ArrayList<Food> foodList) {
        super(context, R.layout.deadline_food_list_item, foodList);
        this.context = context;
        this.foodList = (ArrayList<Food>) foodList;
        Locale.setDefault(Locale.KOREA);

        foodList = new ArrayList<Food>();
        foodList.add(new Food("2015-02-09", "2015-02-09"));
        foodList.add(new Food("2015-02-10", "2015-02-10"));
        foodList.add(new Food("2015-02-11", "2015-02-11"));
        foodList.add(new Food("2015-02-12", "2015-02-12"));
        foodList.add(new Food("2015-02-13", "2015-02-13"));
        foodList.add(new Food("2015-02-14", "2015-02-14"));
        foodList.add(new Food("2015-02-15", "2015-02-15"));
        foodList.add(new Food("2015-02-16", "2015-02-16"));
        foodList.add(new Food("2015-02-17", "2015-02-17"));
        foodList.add(new Food("멸치", "2015-01-30"));
        foodList.add(new Food("사과", "2011-01-31"));
        foodList.add(new Food("베리", "2015-02-01"));
        //can_beer, can_cola
        foodList.add(new Food("맥주", "2015-01-02"));
        foodList.add(new Food("콜라", "2015-02-02"));
        foodList.add(new Food("당근", "2015-03-30"));
        foodList.add(new Food("치킨", "2015-04-31"));
        foodList.add(new Food("clam", "2015-05-01"));
        foodList.add(new Food("달걀", "2015-06-02"));
        foodList.add(new Food("생선", "2015-07-02"));
        foodList.add(new Food("leek", "2015-08-30"));
        //locknlock_1
        foodList.add(new Food("통1", "2014-09-31"));
        foodList.add(new Food("고기", "2013-02-01"));
        foodList.add(new Food("통2", "2005-02-02"));
        foodList.add(new Food("버섯", "2012-02-02"));
        foodList.add(new Food("양파", "2011-01-30"));
        foodList.add(new Food("통3", "2010-01-31"));
        foodList.add(new Food("감자", "2019-09-01"));
        foodList.add(new Food("새우", "2018-02-02"));
        foodList.add(new Food("오징어", "2017-02-02"));
        foodList.add(new Food("토마토", "2016-01-30"));
        foodList.add(new Food("요거트", "2015-01-01"));
        foodList.add(new Food("통4", "2025-02-01"));
        foodList.add(new Food("통5", "2005-02-02"));
        foodList.add(new Food("멸치", "1995-02-20"));
        foodList.add(new Food("베리", "1999-01-30"));
        foodList.add(new Food("사과", "2015-02-31"));
        foodList.add(new Food("당근", "2015-03-01"));
        foodList.add(new Food("치킨", "2015-04-02"));
        foodList.add(new Food("맥주", "2015-05-02"));

    }

    public int getCount() {
        return foodList.size();
    }

    public Food getItem(int position) {
        return foodList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.deadline_food_list_item, null);

        //food객체에 현재 position값에 있는 객체를 넣는다
        food = (Food) getItem(position);


        CheckBox chk_deadline = (CheckBox) rootView.findViewById(R.id.chk_deadline);

        //0-3일 임박 품목은 1 리턴
        //4-7 임박 품목은 2 리턴

        //해당 음식물의 이름을 가져온다.
        TextView text = (TextView) rootView.findViewById(R.id.txt_deadline);
        if (calDeadline(food.getExpiryDate()) == 2) {
            text.setText(food.getFoodName());

        }

        return rootView;
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void add(Food food) {
        foodList.add(food);
        notifyDataSetChanged();
        super.add(food);
    }

    @Override
    public void remove(Food food) {
        foodList.remove(food);
        notifyDataSetChanged();
        super.remove(food);
    }

    //유통기한 계산 코드
    public int calDeadline(String txtDeadline) {
        //0-3일 임박 품목은 1 리턴
        //4-7 임박 품목은 2 리턴

        try {
            final Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            final Calendar deadline = Calendar.getInstance();

            today.setTime(new Date());


            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = sdFormat.parse(txtDeadline);

            // D-day의 날짜를 입력
            deadline.setTime(tempDate);

            long day = deadline.getTimeInMillis() / 86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = day - tday; // 오늘 날짜에서 dday 날짜를 빼w준다

            count = count - 1;

            //유통기한 임박한 날짜가 오늘로부터 ~4일일때
            if (count >= 0 || count < 4) {
                return 1;
            } else if (count >= 4 || count < 8) {
                return 2;
            }
            return 0;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
