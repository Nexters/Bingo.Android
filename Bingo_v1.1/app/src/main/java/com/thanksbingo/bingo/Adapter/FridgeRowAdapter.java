package com.thanksbingo.bingo.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
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

public class FridgeRowAdapter extends ArrayAdapter<Food> {

    private Context context;
    ArrayList<Food> foodList;
    LayoutInflater inflater;
    View rootView;
    Food food;

    public FridgeRowAdapter(Context context, ArrayList<Food> foodList) {
        super(context, R.layout.fridge_img_list_item, foodList);
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

    //viewholder
    class ViewHolder {
        public TextView dday;
        public ImageView icon;
        public TextView text;
        boolean flagFooter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        //food객체에 현재 position값에 있는 객체를 넣는다
        food = (Food) getItem(position);

        //캐시된 뷰가 없을 경우 새로 생성하고 뷰홀더를 생성한다.
        //*****주의사항*****//
        //http://stackoverflow.com/questions/15614279/horizontal-list-view-add-footer-view
        //Be cautious when updating the list. You should remove the last dummy item and add the additional list and then add the dummy item if required
        /*
        Override getCount() of listview adapter to return data.size()+1 elements;
        In getView() insert something like this:

        if (position == data.size()){ return getFooterView();
        }

        In getFooterView() inflate proper layout with button.
         */
        if (convertView == null) {
            holder = new ViewHolder();
            //check whether the view is the footer view or not
            if (food.flagFooter) {
                holder.flagFooter = true;
                ///*  rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fridge_img_list_item, null);
                convertView = inflater.from(parent.getContext()).inflate(R.layout.list_footer, null);
            } else {
                convertView = inflater.from(parent.getContext()).inflate(R.layout.fridge_img_list_item, null);
            }

            //assign holder views all findViewById goes here
            holder.dday = (TextView) convertView.findViewById(R.id.fridge_img_dday);
            holder.icon = (ImageView) convertView.findViewById(R.id.fridge_img_icon);
            holder.text = (TextView) convertView.findViewById(R.id.fridge_img_text);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            //check whether the view is the footer view or not
            if (food.flagFooter) {
                holder.flagFooter = true;
                convertView = inflater.from(parent.getContext()).inflate(R.layout.list_footer, null);
                convertView.setTag(holder);
            } else {
                //check if the view which is being reused is a footer view
                //if it is footer view a list row view should be used.
                if (holder.flagFooter) {
                    holder.flagFooter = false;
                    convertView = inflater.from(parent.getContext()).inflate(R.layout.fridge_img_list_item, null);

                    convertView.setTag(holder);
                }
            }
        }

        holder.dday.setText(caldate(food.getExpiryDate()));
        if (food.getFoodName() == null || food.getFoodName().equals("")) {
            holder.icon.setImageResource(R.drawable.ic_launcher);
        } else if (food.getFoodName() == "멸치") {
            holder.icon.setImageResource(R.drawable.anchovy);
        } else if (food.getFoodName() == "사과") {
            holder.icon.setImageResource(R.drawable.apple);
        } else if (food.getFoodName() == "베리") {
            holder.icon.setImageResource(R.drawable.berry);
        } else if (food.getFoodName() == "맥주") {
            holder.icon.setImageResource(R.drawable.can_beer);
        } else if (food.getFoodName() == "콜라") {
            holder.icon.setImageResource(R.drawable.can_cola);
        } else if (food.getFoodName() == "당근") {
            holder.icon.setImageResource(R.drawable.carrot);
        } else if (food.getFoodName() == "치킨") {
            holder.icon.setImageResource(R.drawable.chicken);
        } else if (food.getFoodName() == "clam") {
            holder.icon.setImageResource(R.drawable.clam);
        } else if (food.getFoodName() == "달걀") {
            holder.icon.setImageResource(R.drawable.egg);
        } else if (food.getFoodName() == "생선") {
            holder.icon.setImageResource(R.drawable.fish);
        } else if (food.getFoodName() == "leek") {
            holder.icon.setImageResource(R.drawable.leek);
        } else if (food.getFoodName() == "고기") {
            holder.icon.setImageResource(R.drawable.meat);
        } else if (food.getFoodName() == "버섯") {
            holder.icon.setImageResource(R.drawable.mushroom);
        } else if (food.getFoodName() == "양파") {
            holder.icon.setImageResource(R.drawable.onion);
        } else if (food.getFoodName() == "감자") {
            holder.icon.setImageResource(R.drawable.potato);
        } else if (food.getFoodName() == "새우") {
            holder.icon.setImageResource(R.drawable.shrimp);
        } else if (food.getFoodName() == "오징어") {
            holder.icon.setImageResource(R.drawable.squid);
        } else if (food.getFoodName() == "토마토") {
            holder.icon.setImageResource(R.drawable.tomato);
        } else if (food.getFoodName() == "요거트") {
            holder.icon.setImageResource(R.drawable.yogurt);
        } else if (food.getFoodName() == "통1") {
            holder.icon.setImageResource(R.drawable.locknlock_1);
        } else if (food.getFoodName() == "통2") {
            holder.icon.setImageResource(R.drawable.locknlock_2);
        } else if (food.getFoodName() == "통3") {
            holder.icon.setImageResource(R.drawable.locknlock_3);
        } else if (food.getFoodName() == "통4") {
            holder.icon.setImageResource(R.drawable.locknlock_4);
        } else if (food.getFoodName() == "통5") {
            holder.icon.setImageResource(R.drawable.locknlock_5);
        } else {
            holder.icon.setImageResource(R.drawable.ic_launcher);
        }
        holder.text.setText(food.getFoodName());

        //update view here
        return convertView;

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

    //dday 계산 코드
    public String caldate(String mDday) {

        try {
            final Calendar today = Calendar.getInstance(); //현재 오늘 날짜
            final Calendar dday = Calendar.getInstance();

            today.setTime(new Date());


            DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate = sdFormat.parse(mDday);

            // D-day의 날짜를 입력
            dday.setTime(tempDate);

            long day = dday.getTimeInMillis() / 86400000;
            // 각각 날의 시간 값을 얻어온 다음
            //( 1일의 값(86400000 = 24시간 * 60분 * 60초 * 1000(1초값) ) )

            long tday = today.getTimeInMillis() / 86400000;
            long count = tday - day; // 오늘 날짜에서 dday 날짜를 빼w준다

            count = count - 1;

            Log.e("----------------today = ", today + "");
            if (count == 0) {
                Log.e("count = ", count + "");
                Log.e("dday = ", mDday);
                return " D - day ";
            } else if (count < 0) {
                Log.e("count = ", count + "");
                Log.e("dday = ", mDday);
                return " D - " + Math.abs(count) + " ";
            } else if (count > 0) {
                Log.e("count = ", count + "");

                Log.e("dday = ", mDday);
                return " D + " + Math.abs(count) + " ";
            }

            return null;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
