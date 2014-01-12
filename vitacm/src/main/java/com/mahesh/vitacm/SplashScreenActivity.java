package com.mahesh.vitacm;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.ProgressBar;


public class SplashScreenActivity extends Activity {
    private static final int MAXPROGRESS = 160;
    ProgressBar progress;
    boolean first_run = true;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getActionBar().hide();
        }
        setContentView(R.layout.activity_splash_screen);
        SystemBarTintManager tintManager=new SystemBarTintManager(this);
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

