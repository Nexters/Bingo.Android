package com.thanksbingo.bingo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//냉장고 이미지로보기 어댑터

public class HorizontalListViewAdapter extends ArrayAdapter<Food> {

    private Context context;
    ArrayList<Food> foodList;
    LayoutInflater inflater;
    View rootView;
    Food food;
    int rowHeight;

    public HorizontalListViewAdapter(Context context, ArrayList<Food> foodList, int rowHeight) {
        super(context, R.layout.horizontal_list, foodList);
        this.context = context;
        this.foodList = (ArrayList<Food>) foodList;
        this.rowHeight=rowHeight;
        Locale.setDefault(Locale.KOREA);
    }

    public int getCount() {
        return rowHeight;
    }

    public Food getItem(int position) {
        return foodList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list, null);

        LinearLayout wrapLinear = (LinearLayout) rootView.findViewById(R.id.wrap_linear);
        //item addVIew
        for (int i = 0; i < foodList.size(); i++) {
            Food food2 = new Food();
            food2 = foodList.get(i);

            LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View mLaout = mInflater.inflate(R.layout.fridge_img_list_item, null);
            //커스텀 레이아웃 파일(xml)내의 뷰를 불러온다.

            TextView dday = (TextView) mLaout.findViewById(R.id.fridge_img_dday);
            ImageView icon = (ImageView) mLaout.findViewById(R.id.fridge_img_icon);
            TextView fName = (TextView) mLaout.findViewById(R.id.fridge_img_text);

            String date;
            //dday setting
            if (food2.getExpiryDate() == null || food2.getExpiryDate().equals("")) {
                date = "2015-02-21";
            } else {
                date = food2.getExpiryDate();
            }
            dday.setText(caldate(date));
            //icon setting
            if (food2.getFoodName() == null || food2.getFoodName().equals("")) {
                icon.setImageResource(R.drawable.ic_launcher);
            } else if (food2.getFoodName().equals("멸치")) {
                icon.setImageResource(R.drawable.anchovy);
            } else if (food2.getFoodName().equals("사과")) {
                icon.setImageResource(R.drawable.apple);
            } else if (food2.getFoodName().equals("딸기")) {
                icon.setImageResource(R.drawable.berry);
            }else if (food2.getFoodName().equals("맥주")) {
                icon.setImageResource(R.drawable.can_beer);
            } else if (food2.getFoodName().equals("콜라")) {
                icon.setImageResource(R.drawable.can_cola);
            } else if (food2.getFoodName().equals("당근")) {
                icon.setImageResource(R.drawable.carrot);
            } else if (food2.getFoodName().equals("치킨")) {
                icon.setImageResource(R.drawable.chicken);
            } else {
                icon.setImageResource(R.drawable.ic_launcher);
            }

            //name setting
            fName.setText(food2.getFoodName());
            wrapLinear.addView(mLaout);
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
