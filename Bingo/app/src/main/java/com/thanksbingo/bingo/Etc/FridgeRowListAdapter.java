package com.thanksbingo.bingo.Etc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.liveo.ndrawer.entity.Food;


/**
 * Created by LG on 2015-01-29.
 */
public class FridgeRowListAdapter extends BaseAdapter {
    private List<Food> mData;
    private LayoutInflater mInflater;

    public FridgeRowListAdapter(Context c, List<Food> foods) {
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mData = foods;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_fridge_row_list_item, null);
            holder.foodName = (TextView)convertView.findViewById(R.id.fragment_fridge_row_list_item_food);
            holder.expiryDate = (TextView)convertView.findViewById(R.id.fragment_fridge_row_list_item_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.foodName != null) {
            /*Food의 disposition 중에 foodName이 있어야함*/
            holder.foodName.setText(mData.get(position).getFoodName());
        }
        if (holder.expiryDate != null) {
            /*Food의 disposition 중에 expiryDate가 있어야함*/
            holder.expiryDate.setText(mData.get(position).getExpiryDate());
        }

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    //static class로 한 이유는 ViewHolder holder = new ViewHolder();를 부를 때 마다
    //새로운 객체를 매번 생성하는 것을 막기 위함.
    public static class ViewHolder {
        public TextView foodName;
        public TextView expiryDate;
    }
}

