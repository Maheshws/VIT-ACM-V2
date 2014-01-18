package com.mahesh.vitacm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 12/27/13.
 */
public class RegisteredEventsFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String HEADER_BAR;
    private Activity parent;
    AlertDialog alert;
    TextView defaultMessage;

    private List<RegEventsObject> allRegEvents = new ArrayList<RegEventsObject>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.reg_events_fragment_layout, container, false);
    }

    public static RegisteredEventsFragment newInstance(int sectionNumber) {
        RegisteredEventsFragment fragment = new RegisteredEventsFragment();
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

        defaultMessage = (TextView) getActivity().findViewById(R.id.fragment_message);
        defaultMessage.setText("No Events Found");
        final SharedPreferences prefs = this.getActivity().getSharedPreferences("user_account_info", 0);
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Checking", "Please wait ...", true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (!prefs.getBoolean("account_valid", false)) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            AccountInvalid();
                        }
                    });

                } else {
                    final String roll = prefs.getString("user_rollno", null);
                    if (allRegEvents.isEmpty()) {

                        UtilitiesMethod utils = new UtilitiesMethod();
                        utils.getRegEvents(roll, getActivity());
                        utils.fillRegEvents(allRegEvents, getActivity());
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateListView();
                            dialog.dismiss();

                        }
                    });

                }
            }
        }).start();


    }


    private void populateListView() {
        ArrayAdapter<RegEventsObject> adapter = new MyEventsListAdapter(this.getActivity(), R.layout.reg_event_item_layout, allRegEvents);
        ListView list = (ListView) getActivity().findViewById(R.id.regEventslistView);
        list.setAdapter(adapter);
        defaultMessage.setVisibility(View.INVISIBLE);
    }


    private class MyEventsListAdapter extends ArrayAdapter<RegEventsObject> {

        RegEventsObject current;

        public MyEventsListAdapter(Context context, int resource, List<RegEventsObject> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater(null).inflate(R.layout.reg_event_item_layout, parent, false);
            }

            current = allRegEvents.get(position);

            TextView Titletext = (TextView) view.findViewById(R.id.item_title);
            Titletext.setText(current.getEventTitle());


            TextView Timetext = (TextView) view.findViewById(R.id.item_index);
            Timetext.setText("Registration no. " + current.getRegID());


            return view;
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


}
