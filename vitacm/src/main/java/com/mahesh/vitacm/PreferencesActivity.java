package com.mahesh.vitacm;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

/**
 * Created by Mahesh on 12/21/13.
 */
public class PreferencesActivity extends PreferenceActivity {
    Editor editor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences_activity);
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
