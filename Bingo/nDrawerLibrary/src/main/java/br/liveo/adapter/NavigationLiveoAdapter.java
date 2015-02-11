/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.liveo.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.List;

import br.liveo.RbPreferences;
import br.liveo.navigationliveo.R;


public class NavigationLiveoAdapter extends BaseAdapter {

    private int mColorIcon = 0;
    private int mColorName = 0;

    private int mNewDrawable = 0;
    private int mColorDefault = 0;
	private final Context mcontext;
    private boolean mRemoveAlpha = false;
	private final List<NavigationLiveoItemAdapter> mList;
    RbPreferences pref = null;




	public NavigationLiveoAdapter(Context context, List<NavigationLiveoItemAdapter> list, int drawable, int colorDefault, boolean removeAlpha, int colorIcon, int colorName) {
		this.mList = list;		
		this.mcontext = context;
        this.mColorIcon = colorIcon;
        this.mColorName = colorName;
        this.mNewDrawable = drawable;
        this.mRemoveAlpha = removeAlpha;
        this.mColorDefault = colorDefault;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public NavigationLiveoItemAdapter getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		return getItem(position).isHeader ? 0 : 1;
	}

	@Override
	public boolean isEnabled(int position) {
		return !getItem(position).isHeader;
	}

	public void setChecked(int pos, boolean checked) {
		mList.get(pos).checked = checked;
		notifyDataSetChanged();			
	}

	public void resetarCheck() {
		for (int i = 0; i < mList.size(); i++) {
			mList.get(i).checked = false;
		}
		this.notifyDataSetChanged();
	}

    public void setNewCounterValue(int position, int value){
        mList.get(position).counter = value;
        notifyDataSetChanged();
    }

	public void setIncreasingCounterValue(int position, int value){
		mList.get(position).counter = ( mList.get(position).counter + value);
		notifyDataSetChanged();
	}

    public void setDecreaseCountervalue(int position, int value){
        mList.get(position).counter = ( mList.get(position).counter - value);
        notifyDataSetChanged();
    }

    ///////////////////
    //수정!!!!
    class ViewHolder {
		public TextView title;
		public ImageView icon;
        public TextView counter;
        public ToggleButton alarm;  //추가

        public RelativeLayout layoutDados;
        public LinearLayout layoutSeparator;

		public ViewHolder(){
		}
	}

    private void setAlpha(View v, float alpha) {
        if (!mRemoveAlpha) {
            v.setAlpha(alpha);
        }else{
            v.setAlpha(1f);
        }
    }

	public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
		NavigationLiveoItemAdapter item = mList.get(position);


        pref = new RbPreferences(mcontext);


        // get (오류시 기본값은 true)
        //boolean callValue = mPref.getString("key", "default value");
        final boolean alarmonoff = pref.getValue(RbPreferences.ALARM_STATE, false);

        Log.i("aaa", Boolean.toString(alarmonoff));



		if (convertView == null) {
			holder = new ViewHolder();
			
			int layout = R.layout.navigation_list_item_icon;

            LayoutInflater inflater = (LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, parent, false);

			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.counter = (TextView) convertView.findViewById(R.id.counter);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

            holder.layoutDados = (RelativeLayout) convertView.findViewById(R.id.layoutDados);
            holder.layoutSeparator = (LinearLayout) convertView.findViewById(R.id.layoutSeparator);
            holder.alarm = (ToggleButton)convertView.findViewById(R.id.alarm);

            convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}

		if (holder.title != null){

            holder.title.setText(item.title);

            if (!item.isHeader) {
                holder.layoutSeparator.setVisibility(View.GONE);
                holder.layoutDados.setVisibility(View.VISIBLE);
                setAlpha(holder.title, (item.checked ? 1f : 0.87f));

                holder.title.setTextColor((!item.isHeader && item.checked && item.colorSelected > 0 ?
                        mcontext.getResources().getColor(item.colorSelected) :
                        mColorName > 0 ? mcontext.getResources().getColor(mColorName) :
                        mcontext.getResources().getColor(R.color.nliveo_black)));
            }else{
                holder.layoutSeparator.setVisibility(View.VISIBLE);

                if (item.title.equals("")){
                    holder.title.setVisibility(View.GONE);
                    holder.layoutDados.setVisibility(View.GONE);
                }else{
                    holder.layoutDados.setVisibility(View.VISIBLE);
                }
            }
		}




        //alarm 보이게 하는거 추가
        if (holder.alarm != null) {
            if (position == 1) {
                holder.alarm.setVisibility(View.VISIBLE);




                //true일 경우
                if(alarmonoff){
                //일단 off로 설정한다.
                holder.alarm.setBackgroundResource(R.drawable.toggleon);
                }else{
                    holder.alarm.setBackgroundResource(R.drawable.toggleoff);
                }



                holder.alarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){ //on
                            Log.i("aaa", "checked");
                           holder.alarm.setBackgroundResource(R.drawable.toggleon);
                            // set
                            pref.put(RbPreferences.ALARM_STATE, true);
                            Toast.makeText(mcontext, "TRUE"+alarmonoff, Toast.LENGTH_SHORT );

                        }else{ //off
                            Log.i("aaa","not checked");
                            holder.alarm.setBackgroundResource(R.drawable.toggleoff);
                            //holder.alarm.setBackgroundDrawable(getResources().getDrawable(R.drawable.duke01));
                            pref.put(RbPreferences.ALARM_STATE, false);
                            Toast.makeText(mcontext, "FALSE"+alarmonoff, Toast.LENGTH_SHORT );

                        }
                    }
                });

            } else {
                holder.alarm.setVisibility(View.GONE);
            }
        }

		if (holder.counter != null) {
			if (item.counter >= 1) {
				holder.counter.setVisibility(View.VISIBLE);
                holder.counter.setText((item.counter > 99) ? "99+" : item.counter + "");
			} else {
				holder.counter.setVisibility(View.GONE);

                if (item.counter < 0){
                    setNewCounterValue(position, 0);
                }
			}
		}
		
		if (holder.icon != null) {
			if (item.icon != 0) {
				holder.icon.setVisibility(View.VISIBLE);
				holder.icon.setImageResource(item.icon);
                setAlpha(holder.icon, (!item.isHeader && item.checked ? 1f : 0.54f));

                holder.icon.setColorFilter((!item.isHeader && item.checked && item.colorSelected > 0 ?
                        mcontext.getResources().getColor(item.colorSelected) :
                        (mColorDefault != 0 ? mcontext.getResources().getColor(mColorDefault) :
                                mColorIcon > 0 ? mcontext.getResources().getColor(mColorIcon) :
                                           mcontext.getResources().getColor(R.color.nliveo_black))));
			} else {
				holder.icon.setVisibility(View.GONE);
			}
		}
	
		if (!item.isHeader) {			
			if (item.checked) {
                convertView.setBackgroundResource((!item.removeSelector ? ( mNewDrawable == 0 ? R.drawable.selector_check_item_navigation : mNewDrawable) : R.drawable.selector_no_check_item_navigation));
			} else {
                convertView.setBackgroundResource(R.drawable.selector_no_check_item_navigation);
			}
		}else{
            convertView.setBackgroundResource(R.drawable.selector_no_check_item_navigation);
        }

	    return convertView;		
	}
}
