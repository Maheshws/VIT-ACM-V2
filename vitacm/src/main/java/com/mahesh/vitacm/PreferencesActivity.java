package com.mahesh.vitacm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Mahesh on 12/21/13.
 */
public class PreferencesActivity extends PreferenceActivity {
    Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference_layout_holder);
        addPreferencesFromResource(R.xml.preferences_activity);
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setTintResource(R.drawable.ab_bottom_solid_bluetheme);

        SharedPreferences pref = getSharedPreferences("user_settings", 0); // 0 - for private mode
        editor = pref.edit();
        final CheckBoxPreference Notification = (CheckBoxPreference) getPreferenceManager().findPreference("Notification");
        Notification.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                editor.putBoolean("Notification", (Boolean) newValue);
                editor.commit();
                return true;
            }
        });

        Preference CurrentVersion = findPreference("CurrentVersion");
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            CurrentVersion.setSummary("v" + versionName);
            CurrentVersion.setTitle("Application Version");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Preference ClearData = findPreference("ClearData");
        ClearData.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                UtilitiesMethod utils = new UtilitiesMethod();
                utils.setContext(PreferencesActivity.this);
                utils.clearApplicationData();
                Toast.makeText(getApplicationContext(), "Application Data Deleted", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        Preference AboutApp = findPreference("AboutApp");
        AboutApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(PreferencesActivity.this, AboutVITACMActivity.class));
                return false;
            }
        });

       /*
        final CheckBoxPreference Theme = (CheckBoxPreference) getPreferenceManager().findPreference("Theme");
        Theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Log.d("MyApp", "Pref " + preference.getKey() + " changed to " + newValue.toString());
                editor.putBoolean("Theme", (Boolean) newValue);editor.commit();
                return true;
            }
        });
        */
        editor.commit();
    }
}
