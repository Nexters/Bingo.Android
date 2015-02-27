package com.thanksbingo.bingo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thanksbingo.CONST_STRINGS;
import com.thanksbingo.bingo.AlertDialog.ViewFoodFragment;
import com.thanksbingo.bingo.Entities.Food;
import com.thanksbingo.bingo.Entities.FoodImageView;
import com.thanksbingo.bingo.R;
import com.thanksbingo.db.BingoDB;
import com.thanksbingo.db.FoodInFridgeContract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//냉장고 이미지로보기 어댑터

public class HorizontalListViewAdapter extends ArrayAdapter<String> {


    private Context context;
    View rootView;
    ArrayList<String> howManyRow;
    String whatFridge;

    FragmentManager fm;
    //howManyFridge = { 2, 3, 4, 5, 6 };
    //whatFridge = 0A(냉장실문), 0B(냉장실안), 0C(냉동실문), 0D(냉동실안)
    public HorizontalListViewAdapter(Context context, ArrayList<String> _howManyRow, String _whatFridge, FragmentManager fm) {
        super(context, R.layout.horizontal_list, _howManyRow);
        this.context = context;
        this.howManyRow = _howManyRow;
        this.whatFridge = _whatFridge;
        this.fm=fm;
        Locale.setDefault(Locale.KOREA);
    }

    public int getCount() {
        return howManyRow.size();
    }

    public String getItem(int position) {
        return howManyRow.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list, null);

        LinearLayout wrapLinear = (LinearLayout) rootView.findViewById(R.id.wrap_linear);

        final String loc_code = whatFridge + getItem(position);
        BingoDB bingoDB = new BingoDB(context);
        ArrayList<FoodInFridgeContract.FIFData> fif_list = bingoDB.getListOfFoodInFridge(loc_code);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //ArrayList<Food> food_list = null;


        wrapLinear.removeAllViews();
        wrapLinear.removeAllViewsInLayout();
        for (int i = 0; i < fif_list.size() + 1; i++) {

            Food f = null;
            View mLayout = mInflater.inflate(R.layout.fridge_img_list_item, null);
            TextView dday = (TextView) mLayout.findViewById(R.id.fridge_img_dday);
            TextView fName = (TextView) mLayout.findViewById(R.id.fridge_img_text);
            FoodImageView icon = (FoodImageView)mLayout.findViewById(R.id.fridge_img_iconn);

            if (i != fif_list.size()) {

                f = new Food();
                f.fif = fif_list.get(i);
                f.flagFooter = false;

                DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date temp = f.fif.exp_date;
                String date = sdFormat.format(temp);

//                String date = f.fif.exp_date.toString();

                dday.setText(caldate(date));

                fName.setText(f.fif.food_name);

//                wrapLinear.addView(mLayout);

                if (!f.flagFooter) {
                    if (f.fif.food_id < 0) {
                        icon.setImageResource(R.drawable.ic_launcher);
                    }
                    else {
                        Bitmap icon_bitmap = BitmapFactory.decodeFile(bingoDB.getIconPath(f.fif.food_id)[0]);
                        icon.setImageBitmap(icon_bitmap);
                        icon.setFivId(f.fif._id);
                        icon.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FoodImageView this_view = (FoodImageView)v;
                                int fiv_id = this_view.getFivId();
                                Log.i(CONST_STRINGS.BINGO_LOG, "III" + fiv_id);
                            }
                        });
                    }
                }
            }
            else {
                f = new Food();
                f.fif = null;
                f.flagFooter = true;
                //food_list.add(f);

                dday.setVisibility(View.INVISIBLE);
                fName.setVisibility(View.INVISIBLE);

                icon.setImageResource(R.drawable.btn_add);
                icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(CONST_STRINGS.BINGO_LOG, "add btn " + loc_code);
                        callViewFoodDialog(loc_code);
                    }
                });
                //mLayout = mInflater.inflate(R.layout.list_footer, null);
            }

            wrapLinear.addView(mLayout);
        }

        return rootView;
    }

    @Override
    public Filter getFilter() {
        return null;
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

    //추가 다이얼로그
    // 음식물 등록 Fragment
    private void callViewFoodDialog(String loc_code) {
        FragmentManager fragmentManager = fm;
        ViewFoodFragment f = ViewFoodFragment.newInstance(loc_code, "Hi");
        f.show(fm, "");
    }
}