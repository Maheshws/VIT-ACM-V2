package com.mahesh.vitacm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.format.Formatter;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Mahesh on 12/20/13.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String HEADER_BAR;
    String result;
    Spinner sp;
    TextView textBoldMessage, textCname1, textCname2, textCphone1, textCphone2, textDesc, textFees, textDateTime, textVenue, textManager;
    String fullname, email, phone, branch, rollno, ipadd, macadd, EventName;
    ImageView imageView;
    Button Register;
    int eid;
    AlertDialog alert;
    private Activity parent;

    public static EventDetailsFragment newInstance(int sectionNumber) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.events_fragment, container, false);
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
        textCname1 = (TextView) getActivity().findViewById(R.id.textCname1);
        textCname2 = (TextView) getActivity().findViewById(R.id.textCname2);
        textCphone1 = (TextView) getActivity().findViewById(R.id.textCphone1);
        textCphone2 = (TextView) getActivity().findViewById(R.id.textCphone2);
        textDesc = (TextView) getActivity().findViewById(R.id.textDesc);
        textDateTime = (TextView) getActivity().findViewById(R.id.textDateTime);
        textVenue = (TextView) getActivity().findViewById(R.id.textVenue);
        textBoldMessage = (TextView) getActivity().findViewById(R.id.textBoldMessage);
        textManager = (TextView) getActivity().findViewById(R.id.textManager);
        imageView = (ImageView) getActivity().findViewById(R.id.imageView);
        textFees = (TextView) getActivity().findViewById(R.id.textFees);
        Register = (Button) getActivity().findViewById(R.id.bRegister);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                textDesc.setText("");
                textCname1.setText("");
                textCname2.setText("");
                textCphone1.setText("");
                textCphone2.setText("");
                textDateTime.setText("");
                textVenue.setText("");
                textFees.setText("");
                textBoldMessage.setText("");
                textManager.setText("");
                Register.setVisibility(View.GONE);
            }
        });

        SharedPreferences prefs = this.getActivity().getSharedPreferences("app_data", 0);
        result = prefs.getString("eventslist", null);
        sp = (Spinner) getActivity().findViewById(R.id.mainmenu);
        setSpinner();

        new Thread(new Runnable() {
            public void run() {
                setValues();
            }
        }).start();
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                new Thread(new Runnable() {
                    public void run() {
                        setValues();
                    }
                }).start();

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        Register.setOnClickListener(this);
    }

    public void setSpinner() {
        final List<String> list = new ArrayList<String>();
        try {
            JSONArray jArray = new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                list.add("" + json.getString("ename"));
            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error Parsing Data " + e.toString());
        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, list);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);

    }

    public void setValues() {

        final String Cname1, Cname2, Cphone1, Cphone2, Desc, DateTime, Venue, Fees;
        final int Status;
        try {
            EventName = sp.getSelectedItem().toString();
            JSONArray jArray = new JSONArray(result);
            JSONObject json = null;
            for (int i = 0; i < jArray.length(); i++) {
                json = jArray.getJSONObject(i);
                if (json.getString("ename").equalsIgnoreCase(EventName)) {
                    break;
                }
            }

            eid = json.getInt("id");
            String temp12 = json.getString("edesc");
            Desc = temp12.replace("&#39;", "'");
            Cname1 = json.getString("contactn1");
            Cname2 = json.getString("contactn2");
            Cphone1 = json.getString("contactp1");
            Cphone2 = json.getString("contactp2");
            DateTime = json.getString("date") + " At " + json.getString("time");
            Venue = json.getString("venue");
            Fees = json.getString("fees");
            Status = json.getInt("status");
            final String imageURL = "" + json.getString("img");
            URL url = new URL("" + json.getString("img"));
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    textBoldMessage.setVisibility(View.GONE);
                    Register.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(null);
                    ImageLoader imgLoader = new ImageLoader(getActivity());

                    imgLoader.DisplayImage(imageURL, 0, imageView);
                    textDesc.setText(Html.fromHtml(Desc));

                    textDesc.setMovementMethod(LinkMovementMethod.getInstance());
                    textManager.setText("Contact Event Managers:");
                    textCname1.setText(Cname1);
                    textCname2.setText(Cname2);
                    textCphone1.setText(Cphone1);
                    textCphone2.setText(Cphone2);
                    textDateTime.setText("Event On : " + DateTime);
                    textVenue.setText("Event Venue : " + Venue);
                    textFees.setText("Event Fees : " + Fees);
                    if (Status == 0) {
                        textBoldMessage.setVisibility(View.VISIBLE);
                        textBoldMessage.setText("REGISTRATIONS ARE CLOSED");
                        Register.setVisibility(View.GONE);
                    }
                }
            });


        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error Parsing Data " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        if (!prefs.getBoolean("account_valid", false)) {
            AccountInvalid();
        } else {
            final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Registering", "Please wait ...", true);
            new Thread(new Runnable() {
                public void run() {
                    submit();
                    dialog.dismiss();
                }
            }).start();
        }
    }

    private void submit() {

        SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        fullname = prefs.getString("user_name", null);
        email = prefs.getString("user_email", null);
        phone = prefs.getString("user_phone", null);
        branch = prefs.getString("user_branch", null);
        rollno = prefs.getString("user_rollno", null);
        ipadd = getLocalIpAddress();
        try {
            //Get MAC Address
            WifiManager manager = (WifiManager) getActivity().getSystemService(parent.WIFI_SERVICE);
            WifiInfo info = manager.getConnectionInfo();
            macadd = info.getMacAddress();
        } catch (Exception e1) {
            macadd = null;
            Log.e("log_tag", "Error Optaining MAC ID " + e1.toString());
        }

        HttpClient httpclient = new DefaultHttpClient();
        //Address to Register PHP
        HttpPost httppost = new HttpPost("http://vitmumbai.acm.org/app/register.php");
        try {


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("id", Integer.toString(eid)));
            nameValuePairs.add(new BasicNameValuePair("ename", EventName));
            nameValuePairs.add(new BasicNameValuePair("name", fullname));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            nameValuePairs.add(new BasicNameValuePair("phone", phone));
            nameValuePairs.add(new BasicNameValuePair("branch", branch));
            nameValuePairs.add(new BasicNameValuePair("rollno", rollno));
            nameValuePairs.add(new BasicNameValuePair("ipadd", ipadd));
            nameValuePairs.add(new BasicNameValuePair("macadd", macadd));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);


            final String Responsesrv = EntityUtils.toString(response.getEntity());
            if (Responsesrv.equals("Already Registered"))
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "YOU HAVE ALREADY REGISTERED FOR THIS EVENT", Toast.LENGTH_LONG).show();
                    }
                });
            else if (Responsesrv.substring(0, 7).equals("Success"))
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Thank You \n\nYour Registration Number is " + Responsesrv.substring(7) + "\nPlease make sure you write down your registration number", Toast.LENGTH_LONG).show();

                    }
                });
            else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity(), "Oops Something went wrong. Please try again later", Toast.LENGTH_LONG).show();

                    }
                });

            }

        } catch (Exception e) {
            Log.e("log_tag", "Error sending data " + e.toString());
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity(), "Unable to connect to server. Please try again later.", Toast.LENGTH_LONG).show();

                }
            });

        }

    }

    public void AccountInvalid() {
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getActivity());

        dlgAlert.setMessage("Please fill account information first");
        dlgAlert.setTitle("Invalid Account Information");
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                        fragmentManager.beginTransaction()
                                .replace(R.id.container, AccountSettingsFragment.newInstance(3))
                                .addToBackStack(null)
                                .commit();

                    }
                });
        alert = dlgAlert.create();
        alert.show();

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
}
