package com.mahesh.vitacm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Mahesh on 12/21/13.
 */
public class ContactUsFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String HEADER_BAR;
    private Activity parent;


    TextView texthead, regMsg;
    EditText userMessage;
    String fullname, email, phone, branch, rollno, message, ipadd, macadd;
    Button sendMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contact_fragment, container, false);
    }

    public static ContactUsFragment newInstance(int sectionNumber) {
        ContactUsFragment fragment = new ContactUsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        HEADER_BAR = getString(R.string.title_section6);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void onResume() {
        super.onResume();
        ((MainActivity) parent).setActionBarTitle(HEADER_BAR);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendMessage = (Button) getActivity().findViewById(R.id.send_message);
        userMessage = (EditText) getActivity().findViewById(R.id.userMessage);
        regMsg = (TextView) getActivity().findViewById(R.id.regMsg);
        regMsg.setText("");
        sendMessage.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        if (!prefs.getBoolean("account_valid", false)) {
            AccountInvalid();
        } else {
            final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Sending Message", "Please wait ...", true);
            new Thread(new Runnable() {
                public void run() {
                    submit();
                    dialog.dismiss();
                }
            }).start();
        }
    }

    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());

                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("log_tag", "Error Obtaining IP Address " + ex.toString());
        }
        return null;
    }

    private void submit() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        fullname = prefs.getString("user_name", null);
        email = prefs.getString("user_email", null);
        phone = prefs.getString("user_phone", null);
        branch = prefs.getString("user_branch", null);
        rollno = prefs.getString("user_rollno", null);
        message = userMessage.getText().toString();
        ipadd = getLocalIpAddress();
        try {
            WifiManager manager;
            manager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macadd = info.getMacAddress();
        } catch (Exception e1) {
            macadd = null;
            Log.e("log_tag", "Error Obtaining MAC ID " + e1.toString());
        }

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://vitmumbai.acm.org/app/contactus.php");
        try {


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("name", fullname));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("branch", branch));
            nameValuePairs.add(new BasicNameValuePair("rollno", rollno));
            nameValuePairs.add(new BasicNameValuePair("message", message));
            nameValuePairs.add(new BasicNameValuePair("ipadd", ipadd));
            nameValuePairs.add(new BasicNameValuePair("macadd", macadd));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);

            final String Responsesrv = EntityUtils.toString(response.getEntity());
            if (Responsesrv.equals("invalid input"))
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        regMsg.setText("Invalid Data Input\nPlease check all fields are filled properly");
                    }
                });
            else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        regMsg.setText("Thank You\nMessage sent successfully ");
                        userMessage.setText("");
                    }
                });

            }

        } catch (Exception e) {
            Log.e("log_tag", "Error sending data " + e.toString());
        }

    }

    AlertDialog alert;

    public void AccountInvalid() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());

        dlgAlert.setMessage("Please fill account information first");
        dlgAlert.setTitle("Invalid Account Information");
        dlgAlert.create().show();
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, AccountSettingsFragment.newInstance(3))
                                .commit();

                    }
                });
        alert = dlgAlert.create();
        alert.show();


    }
}
