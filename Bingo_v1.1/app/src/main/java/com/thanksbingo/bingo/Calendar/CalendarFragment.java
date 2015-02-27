package com.thanksbingo.bingo.Calendar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.thanksbingo.bingo.AlertDialog.EditFoodFragment;
import com.thanksbingo.bingo.AlertDialog.ViewFoodFragment;
import com.thanksbingo.bingo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by LG on 2015-02-09.
 */
public class CalendarFragment extends Fragment {
    private GregorianCalendar month;
    private CalendarAdapter adapter;
    private Date startDate;
    private Date endDate;
    private TextView calendarTitle;

    //테스트하기위해 권장 유통기한날짜 변수
    private int foodDuration = 2;

    private int type = 0;

    public CalendarFragment(int type){
        this.type = type;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().getAttributes().format = android.graphics.PixelFormat.RGBA_8888;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.calendar, container, false);
        //한국으로 설정
        setLocalToKorea();

        //오늘 월 instance를 가져온다
        month = (GregorianCalendar)GregorianCalendar.getInstance();

        //adapter에 이번 월 변수를 넘겨줌으로써
        //이 정보로 하여금 이번 달 날짜들을 가지게 한다.
        adapter = new CalendarAdapter(getActivity(), month);

        //mainView에 있는 gridview에 adapter를 붙인다
        GridView gridView = (GridView)mainView.findViewById(R.id.calendar_gridview);
        gridView.setAdapter(adapter);

        //Calendar의 제목 날짜 셋팅
        calendarTitle = (TextView)mainView.findViewById(R.id.calendar_title);
        calendarTitle.setText(DateFormat.format("yyyy.MM", month));

        //Calendar의 전/후 버튼에 대한 clicklistener 등록
        ImageView calendarPrevious = (ImageView)mainView.findViewById(R.id.calendar_previous);
        calendarPrevious.setOnClickListener(arrowClickListener);
        ImageView calendarAfter = (ImageView)mainView.findViewById(R.id.calendar_after);
        calendarAfter.setOnClickListener(arrowClickListener);

        //Calendar의 gridview의 각 아이템에 대해 listener를 붙인다
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //눌린 날짜를 date형식으로 가져온다
                Date selectedDate = CalendarAdapter.daysInMonth.get(position);
                if (isStartDate(selectedDate)) {
                    //권장 유통기한 날짜를 highlight 하기 위해
                    //startDate 값을 저장해둬야한다.
                    setToStartDate(selectedDate);

                } else if (isEndDate(selectedDate)) {
                    if (startDate != null)
                        setToEndDate(selectedDate);

                }
            }
        });
        return mainView;
    }

    private void setToEndDate(Date date) {
        endDate = date;
        refreshPreferredDatesHighlight();

    }

    private void setToStartDate(Date date) {
        startDate = date;
        //권장 유통기한 변수 foodDuration을 통해 endDate를 자동으로 설정
        setPreferredEndDate(startDate);
        refreshPreferredDatesHighlight();
    }

    private void setPreferredEndDate(Date startDate) {
        //시간 연산을 하기 위해 calendar 인스턴스를 생성
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        //권장 유통기한 날짜를 더해준다
        cal.add(Calendar.DATE, foodDuration);
        //endDate 변수에 대입
        endDate = cal.getTime();
    }

    private void refreshPreferredDatesHighlight() {
        //startDate와 endDate 사이의 모든 날짜들을 highlight될 날짜 대상으로 지정한다.
        adapter.refreshHighlightDays(startDate, endDate);
        adapter.notifyDataSetChanged();
        if (type == 0)
            ViewFoodFragment.refreshDatesText(adapter.dateToString(startDate), adapter.dateToString(endDate));
        else
            EditFoodFragment.refreshDatesText(adapter.dateToString(startDate), adapter.dateToString(endDate));
    }

    private boolean isStartDate(Date date) {
        Date todayDate = adapter.getTodayDate();
        Date mDate = date;
        if (mDate.before(todayDate) || mDate.equals(todayDate)) {
            return true;
        }
        return false;
    }

    private boolean isEndDate(Date date) {
        Date todayDate = adapter.getTodayDate();
        Date mDate = date;
        if (mDate.after(todayDate)) {
            return true;
        }
        return false;
    }
    //Calendar 전/후 버튼에 대한 리스너
    View.OnClickListener arrowClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.calendar_after:
                    //다음 달 달력을 보여준다.
                    showNextMonth();
                    break;
                case R.id.calendar_previous:
                    //이전 달 달력을 보여준다.
                    showPreviousMonth();
                    break;
            }
        }
    };

    private void showNextMonth() {
        //현재가 12월달이면 해를 바꾼다.
        if (month.get(GregorianCalendar.MONTH) == month.getActualMaximum(GregorianCalendar.MONTH)){
            month.set((month.get(GregorianCalendar.YEAR) + 1), month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            //12월이 아니면 그냥 다음 달을 보여준다
            month.set(GregorianCalendar.MONTH, month.get(GregorianCalendar.MONTH) + 1);
        }
        //month변수에 변화를 주었으니 month변수를 사용하는 adapter에게 변화가 있었음을 알려줘야한다.
        refreshMonth();
    }

    private void showPreviousMonth() {
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }
        refreshMonth();
    }

    private void refreshMonth() {
        //바뀐 month 정보를 통해 adapter가 가지고 있는 날짜들도 바꿔준다
        adapter.refreshDays();
        adapter.notifyDataSetChanged();

        //아울러 달력 제목도 바꿔준다.
        calendarTitle.setText(DateFormat.format("yyyy.MM", month));
    }

    public static void setLocalToKorea() {
        Locale.setDefault(Locale.KOREA);
    }
}
