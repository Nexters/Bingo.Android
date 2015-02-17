package com.thanksbingo.bingo.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.thanksbingo.bingo.R;

/**
 * Created by LG on 2015-02-10.
 */
public class CalendarAdapter extends BaseAdapter{
    public static List<Date> daysInMonth;
    //현재 월
    private Calendar month;
    //오늘 날짜
    private Date todayDate;

    //인플레이터
    private LayoutInflater inflater;

    //이 클래스에서 쓸 simpledateformat
    private SimpleDateFormat mDateFormat;

    //주기적 날짜 계산을 위한 Calendar 변수
    private Calendar cal;
    private int firstDay;

    //하이라이트 할 날짜들을 보관하는 리스트
    private List<Date> highlightingDates;


    public CalendarAdapter(Context c, GregorianCalendar month) {
        daysInMonth = new ArrayList<Date>();
        cal = Calendar.getInstance();
        highlightingDates = new ArrayList<Date>();
        //Local을 Korea를 지정하더라.
        CalendarFragment.setLocalToKorea();
        this.month = month;
        //오늘 날짜를 가져온다
        todayDate = ((GregorianCalendar)month.clone()).getTime();
        Log.d("Testing", "today date : " + todayDate);
        //이 뒤는 무슨 작동을하는건지 잘 모르겠지만 없으면 잘 안됨
        month.set(GregorianCalendar.DAY_OF_MONTH, 1);

        //인플레이터 생성
        inflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //mDateFormat 설정
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        //this.month 정보를 토대로 이 달에 있는 날짜들을 생성한다
        refreshDays();
    }

    public void refreshDays() {
        daysInMonth.clear();
        //이 뒤는 사실 먼지 생각하기귀찮음
        //그냥 그대로 따라함
        GregorianCalendar pmonth = (GregorianCalendar)month.clone();
        firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
        int maxWeekNumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
        int mnthlength = maxWeekNumber * 7;
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            pmonth.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        int maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        int calMaxP = maxP - (firstDay - 1);
        GregorianCalendar pmonthmaxset = (GregorianCalendar)pmonth.clone();
        pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

        refreshDaysInMonthDataSet(mnthlength, pmonthmaxset);
    }

    private void refreshDaysInMonthDataSet(int monthLength, GregorianCalendar maxDate) {
        Date d = null;
        for (int n = 0; n < monthLength; n++) {
            d = maxDate.getTime();
            daysInMonth.add(d);
            maxDate.add(GregorianCalendar.DATE, 1);
        }
    }

    public Date getTodayDate() {
        return this.todayDate;
    }

    @Override
    public int getCount() {
        return daysInMonth.size();
    }

    @Override
    public Object getItem(int position) {
        return daysInMonth.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.calendar_item, null);
            holder.textView = (TextView)convertView.findViewById(R.id.calendar_date_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (holder.textView != null) {
            cal.setTime(daysInMonth.get(position));
            //지금 이 포지션에 있는 날짜 변수
            Date thisDate = daysInMonth.get(position);

            int dayOfMonth = getDayOfMonth(cal);
            int dateOfMonth = getDateOfMonth(cal);
            holder.textView.setText(String.valueOf(dateOfMonth));

            if (isNotThisMonth(dateOfMonth, position)) {
                //Gray
                holder.textView.setTextColor(Color.LTGRAY);
            } else if (isWeekend(dayOfMonth)) {
                //Red
                holder.textView.setTextColor(Color.RED);
            } else {
                //Black
                holder.textView.setTextColor(Color.BLACK);
            }
            if(needHighlight(thisDate)) {
                //하이라이트가 필요하다면 한다
                if (isStartOfHighlight(thisDate)) {
                    if(isToday(thisDate)) {
                        //시작일이면서 오늘
                        convertView.setBackgroundResource(R.drawable.calendar_today_selected_start);
                    } else {
                        //시작일지만 오늘은 아님
                        convertView.setBackgroundResource(R.drawable.calendar_highlight_start);
                    }
                } else if (isEndOfHighlight(thisDate)) {
                    if (isToday(thisDate)) {
                        //끝일이면서 오늘
                        convertView.setBackgroundResource(R.drawable.calendar_today_selected_end);
                    } else {
                        //끝일이지만 오늘은 아님
                        convertView.setBackgroundResource(R.drawable.calendar_highlight_end);
                    }
                } else {
                    if (isToday(thisDate)) {
                        //중간이면서 오늘
                        convertView.setBackgroundResource(R.drawable.calendar_today_selected_middle);
                    } else {
                        //중간이지만 오늘은 아님
                        convertView.setBackgroundResource(R.drawable.calendar_highlight_middle);
                    }
                }
                return convertView;
            }

            if(isToday(thisDate)) {
                convertView.setBackgroundResource(R.drawable.calendar_today_mark);
                return convertView;
            }
            convertView.setBackgroundResource(R.drawable.calendar_none_fill);
        }
        return convertView;
    }

    private boolean isStartOfHighlight(Date date) {
        return highlightingDates.get(0).equals(date) ? true : false;
    }

    private boolean isEndOfHighlight(Date date) {
        return highlightingDates.get(highlightingDates.size()-1).equals(date) ? true : false;
    }

    private boolean needHighlight(Date date) {
        if (highlightingDates.isEmpty()) return false;
        return highlightingDates.contains(date) ? true : false;
    }

    private boolean isToday(Date date) {
        if (date.equals(todayDate)) return true;
        return false;
    }

    public void refreshHighlightDays(Date startDate, Date endDate) {
        highlightingDates.clear();
        cal.setTime(startDate);
        while(true) {
            highlightingDates.add(cal.getTime());
            if (cal.getTime().equals(endDate)) break;
            cal.add(Calendar.DATE, 1);
        }
    }

    private int getDayOfMonth(Calendar cal) {
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    private int getDateOfMonth(Calendar cal) {
        return cal.get(Calendar.DATE);
    }

    private boolean isNotThisMonth(int date, int pos) {
        if ((date > 1) && (pos < firstDay)) {
            return true;
        } else if ((date < 7) && (pos > 28)) {
            return true;
        }
        return false;
    }

    private boolean isWeekend(int day) {
        if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
            return true;
        }
        return false;
    }

    public String dateToString(Date date) {
        return mDateFormat.format(date);
    }

    private static class ViewHolder {
        public TextView textView;
    }
}
