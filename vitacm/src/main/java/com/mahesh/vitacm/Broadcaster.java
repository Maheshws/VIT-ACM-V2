package com.mahesh.vitacm;

/**
 * Created by Mahesh on 1/18/14.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Broadcaster extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent service = new Intent(context, NotificationService.class);
        context.startService(service);


    }
}

