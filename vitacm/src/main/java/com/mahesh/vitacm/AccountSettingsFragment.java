package com.mahesh.vitacm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by Mahesh on 12/21/13.
 */
public class AccountSettingsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private EditText userRollno, userName, userEmail, userPhone;
    private Spinner userBranch;
    private Button saveAccountInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.accounts_info_fragment, container, false);
        return view;
    }

    public static AccountSettingsFragment newInstance(int sectionNumber) {
        AccountSettingsFragment fragment = new AccountSettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onActivityCreated(Bundle saved) {
        super.onActivityCreated(saved);
        userRollno = (EditText) getView().findViewById(R.id.rollno);
        userName = (EditText) getView().findViewById(R.id.userName);
        userEmail = (EditText) getView().findViewById(R.id.userEmail);
        userPhone = (EditText) getView().findViewById(R.id.userPhone);
        userBranch = (Spinner) getView().findViewById(R.id.branch);
        saveAccountInfo = (Button) getView().findViewById(R.id.saveAccountInfo);
        loadFromPrefs();
        saveAccountInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (validateAccInfo()) {
                    saveToPrefs();
                    showError("Account Info Saved");
                } else {

                }
            }
        }).start();
    }

    private Boolean validateAccInfo() {
        if (userName.getText().length() > 8)
            if (isValidEmailAddress(userEmail.getText().toString()))
                if (PhoneNumberUtils.isGlobalPhoneNumber(userPhone.getText().toString()) && userPhone.length() == 10)
                    if (userBranch.getSelectedItemId() > 0)
                        if (userRollno.getText().length() == 6 || userRollno.length() == 10)
                            return true;
                        else showError("Invalid Roll no");
                    else showError("Invalid Branch");
                else showError("Invalid Phone number");
            else showError("Invalid Email");
        else showError("Invalid Name");
        return false;
    }

    public static boolean isValidEmailAddress(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void showError(String msg) {
        final String displayMsg = msg;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity().getApplicationContext(), displayMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveToPrefs() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("user_name", userName.getText().toString());
        editor.putString("user_email", userEmail.getText().toString());
        editor.putString("user_phone", userPhone.getText().toString());
        editor.putString("user_branch", userBranch.getSelectedItem().toString());
        editor.putInt("user_branch_id", (int) userBranch.getSelectedItemId());
        editor.putString("user_rollno", userRollno.getText().toString());
        editor.putBoolean("account_valid", true);
        editor.commit();
    }

    private void loadFromPrefs() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        userName.setText(prefs.getString("user_name", null));
        userEmail.setText(prefs.getString("user_email", null));
        userPhone.setText(prefs.getString("user_phone", null));
        userBranch.setSelection(prefs.getInt("user_branch_id", 0));
        userRollno.setText(prefs.getString("user_rollno", null));
    }
}
