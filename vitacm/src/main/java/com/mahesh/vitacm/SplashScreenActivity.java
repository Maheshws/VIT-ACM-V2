package com.mahesh.vitacm;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;


public class SplashScreenActivity extends Activity {
    private static final int MAXPROGRESS = 160;
    ProgressBar progress;
    boolean first_run = true;
    Animation animFadein;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getActionBar().hide();
        }
        setContentView(R.layout.splash_screen_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView txtMessage = (TextView) findViewById(R.id.fullscreen_content);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);
        txtMessage.startAnimation(animFadein);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintColor(Color.parseColor("#0099cc"));
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setMax(MAXPROGRESS);
        SharedPreferences prefs = getSharedPreferences("app_data", 0);
        first_run = prefs.getBoolean("first_run", true);
        new Thread(new Runnable() {
            public void run() {
                startMainActivityNew();
            }
        }).start();

    }

    public void startMainActivityNew() {
        updateProgess();
        Boolean errorFlag = false;
        if (!isNetworkAvailable() && first_run == true) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setProgress(MAXPROGRESS);
                    NoConnection("No Internet Connection. Please try again later");
                }
            });

        } else {
            try {
                UtilitiesMethod utils = new UtilitiesMethod();
                utils.setContext(getApplicationContext());
                updateProgess();
                utils.getIndex();
                updateProgess();
                utils.getAnnouncements();
                updateProgess();
                utils.getBlog();
                updateProgess();
                utils.getEvents();
                updateProgess();
                errorFlag = utils.getErrorFlag();
                updateProgess();
                if (!errorFlag) {
                    SharedPreferences prefs = getSharedPreferences("app_data", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("first_run", false);
                    editor.commit();
                    updateProgess();
                    actionComplete();
                } else if (errorFlag && first_run == true) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress.setProgress(MAXPROGRESS);
                            NoConnection("Unable to connect to server. Please try again later");
                        }
                    });

                } else
                    actionComplete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    protected void actionComplete() {
        updateProgess();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SharedPreferences pref = getSharedPreferences("user_settings", 0);
        Boolean Service = pref.getBoolean("Notification", false);
        if (Service) {
            Log.d("service", "Preferences is OFF!!");
        } else {
            startService(new Intent(this, NotificationService.class));
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    protected void updateProgess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progress.incrementProgressBy(20);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void NoConnection(String msg) {
        if (!isFinishing()) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

            dlgAlert.setMessage(msg);
            dlgAlert.setTitle("No Connection");
            dlgAlert.create().show();
            dlgAlert.setPositiveButton("Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit(0);
                        }
                    });
            AlertDialog alert = dlgAlert.create();
            alert.show();

        }
    }


}

