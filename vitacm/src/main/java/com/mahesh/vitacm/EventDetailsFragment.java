package com.mahesh.vitacm;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 12/20/13.
 */
public class EventDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_SECTION_NUMBER = "section_number";
    String result;
    Spinner sp;
    TextView textBoldMessage, textCname1, textCname2, textCphone1, textCphone2, textDesc, textFees, textDateTime, textVenue, textManager;
    ImageView imageView;
    Button Register;
    int eid;
    private static String HEADER_BAR;
    private Activity parent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.events_fragment, container, false);
    }

    public static EventDetailsFragment newInstance(int sectionNumber) {
        EventDetailsFragment fragment = new EventDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        HEADER_BAR = getString(R.string.title_section7);
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
            String EventName = sp.getSelectedItem().toString();
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


            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    textBoldMessage.setVisibility(View.GONE);
                    Register.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(null);
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

            URL url = new URL("" + json.getString("img"));
            final Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    imageView.setImageBitmap(bmp);
                }
            });
        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag", "Error Parsing Data " + e.toString());
        }
    }

    @Override
    public void onClick(View view) {

    }
}
