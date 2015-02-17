package com.thanksbingo.bingo.Entities;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.thanksbingo.bingo.Activities.FridgeCustom;
import com.thanksbingo.bingo.Activities.MainActivity;
import com.thanksbingo.bingo.Fragments.DeadlineFragment;
import com.thanksbingo.bingo.R;

/**
 * Created by yoonsun on 2015. 2. 12..
 */
// The class has to extend the BroadcastReceiver to get the notification from the system

public class TimeAlarm extends BroadcastReceiver {

    private int Number = 0; //DB서가져와서 개수로

    @Override
    public void onReceive(Context context, Intent paramIntent) {

        // Request the notification manager
        NotificationManager noti_m = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //activity로는 넘어가는데 fragment로 넘어가질 않아.
        Intent intent = new Intent(context, MainActivity.class); // ssibba ssibaaaaaaaaaa

        // Attach the intent to a pending intent
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the notification
        Notification notification = new NotificationCompat
                .Builder(context)
                .setContentTitle("BINGO")
                .setContentText("유통기한 임박 물품이 " + Number + "개 있습니다.")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();

        noti_m.notify(7829, notification);


    }
}
