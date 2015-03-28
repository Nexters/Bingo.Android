package com.thanksbingo.bingo.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.thanksbingo.bingo.BackPressCloseHandler;
import com.thanksbingo.bingo.Entities.TimeAlarm;
import com.thanksbingo.bingo.Fragments.CustomerFragment;
import com.thanksbingo.bingo.Fragments.DeadlineFragment;
import com.thanksbingo.bingo.Fragments.DeveloperFragment;
import com.thanksbingo.bingo.Fragments.MyBingoFragment;
import com.thanksbingo.bingo.Fragments.RecipeFragment;
import com.thanksbingo.bingo.Fragments.StoregeFragment;
import com.thanksbingo.bingo.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.liveo.interfaces.NavigationLiveoListener;
import br.liveo.navigationliveo.NavigationLiveo;

// 탁다임은 멍청하다.

public class MainActivity extends NavigationLiveo implements NavigationLiveoListener, MyBingoFragment.OnFragmentInteractionListener {

    private List<String> drawerItemList;
    private List<Integer> drawerItemIconList;
    private List<Integer> dividerItemList;
    private SparseIntArray counterItem;

    private static String userId = "Team. 냉장GO";
    private static String BingoType = "  B I N G O";

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //notification
        //createScheduledNotification();

        //setContentView(R.layout.activity_main);
        ImageView toolbar_logo = (ImageView)findViewById(R.id.toolbar_logo);
        toolbar_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new MyBingoFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragment != null) {
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                }
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    public void onUserInformation() {
        this.mUserName.setText(userId);
        this.mUserEmail.setText(BingoType);
        this.mSettings.setImageResource(R.drawable.ic_settings_grey600_18dp);
        this.mUserBackground.setBackgroundColor(Color.parseColor("#4ac6be"));
    }

    public void setDrawerItemCounter(int pos, int cnt) {
        counterItem.put(pos, cnt);
//        this.setNavigationAdapter(drawerItemList, drawerItemIconList, dividerItemList, counterItem);
    }

    @Override
    public void onInt(Bundle savedInstanceState) {

        this.setNavigationListener(this);

        this.setDefaultStartPositionNavigation(0);

        drawerItemList = new ArrayList<>();
        drawerItemList.add(0, "내 냉장고 보기");
        drawerItemList.add(1, "유통기한 임박품목");
        drawerItemList.add(2, "알람 설정");
        drawerItemList.add(3, "");
        drawerItemList.add(4, "보관 방법");
        drawerItemList.add(5, "요리법");
        drawerItemList.add(6, "");
        //drawerItemList.add(7, "고객 센터");
        drawerItemList.add(7, "제작자 정보");

        drawerItemIconList = new ArrayList<>();
        drawerItemIconList.add(0, R.drawable.ic_account_circle_grey600_18dp);
        drawerItemIconList.add(1, R.drawable.ic_event_note_grey600_18dp);
        drawerItemIconList.add(2, R.drawable.ic_notifications_grey600_18dp);
        drawerItemIconList.add(3, 0);
        drawerItemIconList.add(4, R.drawable.ic_info_grey600_18dp);
        drawerItemIconList.add(5, R.drawable.ic_receipt_grey600_18dp);
        drawerItemIconList.add(6, 0);
        //drawerItemIconList.add(7, R.drawable.ic_headset_mic_grey600_18dp);
        drawerItemIconList.add(7, R.drawable.ic_info_grey600_18dp);

        dividerItemList = new ArrayList<>();
        dividerItemList.add(3);
        dividerItemList.add(6);

        counterItem = new SparseIntArray();
        counterItem.put(1, 0);

        //this.setFooterInformationDrawer(R.string.logout);
        this.setFooterInformationDrawer(R.string.exit);
        this.setNavigationAdapter(drawerItemList, drawerItemIconList, dividerItemList, counterItem);
    }



    @Override
    public void onClickFooterItemNavigation(View v) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("나가기")
                .setMessage("Bingo 앱을 종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("아니오", null)
                .show();
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {

        Fragment fragment = null;
        switch (position) {
            //내 냉장고 보기
            case 0:
                fragment = new MyBingoFragment();
                break;
            // 유통기한 임박품목
            case 1:
                fragment = new DeadlineFragment();
                break;
            // case 2: 알람 설정
            //case 3 : 라인
            //보관 방법
            case 4:
                fragment = new StoregeFragment();
                break;
            //요리법
            case 5:
                fragment = new RecipeFragment();
                break;
            // case 6 : 라인
            // 고객 센터
//            case 7:
//                fragment = new CustomerFragment();
//                break;
            // 제작자 정보
            case 7:
                fragment = new DeveloperFragment();
                break;

            default:
                break;
        }

        //fragment.setArguments(args);
        FragmentManager frgManager = getSupportFragmentManager();
        if (fragment != null){
            frgManager.beginTransaction().replace(layoutContainerId, fragment).commit();
        }
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    //notification
    private void createScheduledNotification()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 05);
        calendar.set(Calendar.MINUTE, 31);
        calendar.set(Calendar.SECOND, 00);

        Intent intent = new Intent(this, TimeAlarm.class);
        // Prepare the pending intent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Register the alert in the system. You have the option to define if the device has to wake up on the alert or not

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
