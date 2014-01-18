package com.mahesh.vitacm;

/**
 * Created by Mahesh on 1/18/14.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;

public class NotificationService extends Service {
    NotificationManager nm;
    String nMessage, nTitle;
    boolean done = true;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate() {
        Log.e("service", "onCreate");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean Service = sharedPreferences.getBoolean("Notification", false);
        if (Service) {
            Log.e("service", "Preferences if OFF!!");
            this.stopSelf();
        } else {
            nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(235);
            super.onCreate();
        }
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        Boolean Service = sharedPreferences.getBoolean("Notification", false);
        if (Service) {
            Log.e("service", "Preferences if OFF!!");
            this.stopSelf();
        } else {
            Log.e("service", "onStart");

            new Thread() {
                public void run() {
                    if (getVersion())
                        showNotification();
                    done = false;
                }
            }.start();

                Calendar cal = Calendar.getInstance();

                // add 15 minutes to the calendar object
                cal.add(Calendar.MINUTE, 30);

                Intent in = new Intent(this, NotificationService.class);
                PendingIntent pi = PendingIntent.getService(this, 123, in,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
                am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);

        }
        return super.onStartCommand(intent, flags, startId);


    }


    @Override
    public void onDestroy() {
        Log.e("service", "onDestory");
        super.onDestroy();
    }

    boolean getVersion() {
        UtilitiesMethod utils=new UtilitiesMethod();
        utils.setContext(NotificationService.this);
        if(utils.getAnnouncementIndex()){
            utils.getCurrentAnnouncement();
            nTitle=utils.getATitle();
            nMessage=utils.getAMessage();
            return true;
        }
        else
            return false;
    }

    @SuppressWarnings("deprecation")
    void showNotification() {
        Notification myNotification = new Notification(R.drawable.ic_launcher,
                "New Announcement",
                System.currentTimeMillis());
        Context context = getApplicationContext();
        String notificationTitle = nTitle;
        String notificationText = nMessage;
        Intent myIntent = new Intent(this, SplashScreenActivity.class);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(NotificationService.this,
                0, myIntent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        myNotification.defaults |= Notification.DEFAULT_SOUND;
        myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
        myNotification.setLatestEventInfo(context,
                notificationTitle,
                notificationText,
                pendingIntent);
        nm.notify(235, myNotification);


    }


}

