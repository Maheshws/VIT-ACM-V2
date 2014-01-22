package com.mahesh.vitacm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
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
public class AnnouncementHomeFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String HEADER_BAR;
    private Activity parent;
    private List<AnnouncementObject> allAnnouncements = new ArrayList<AnnouncementObject>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.announcement_fragment_layout, container, false);
    }

    public static AnnouncementHomeFragment newInstance(int sectionNumber) {
        AnnouncementHomeFragment fragment = new AnnouncementHomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        HEADER_BAR = getString(R.string.title_section4);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView defaultMessage = (TextView) getActivity().findViewById(R.id.fragment_message);
        defaultMessage.setText("Latest Announcements : ");
        if (allAnnouncements.isEmpty()) {
            UtilitiesMethod utils = new UtilitiesMethod();
            utils.fillMyAnnouncements(allAnnouncements, getActivity());
        }
        populateListView();

    }

    private void populateListView() {
        ArrayAdapter<AnnouncementObject> adapter = new MyAnnouncementListAdapter(this.getActivity(), R.layout.announcement_item_layout, allAnnouncements);
        ListView list = (ListView) getActivity().findViewById(R.id.announcementslistView);
        list.setAdapter(adapter);
    }

    private class MyAnnouncementListAdapter extends ArrayAdapter<AnnouncementObject> {
        AnnouncementObject currentAnnouncement;

        public MyAnnouncementListAdapter(Context context, int resource, List<AnnouncementObject> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater(null).inflate(R.layout.announcement_item_layout, parent, false);
            }

            currentAnnouncement = allAnnouncements.get(position);

            TextView Titletext = (TextView) view.findViewById(R.id.item_title);
            Titletext.setText(Html.fromHtml(currentAnnouncement.getATitle()));
            TextView Messagetext = (TextView) view.findViewById(R.id.item_content);
            Messagetext.setText(Html.fromHtml(currentAnnouncement.getAContent()));

            return view;
        }

    }

    public void onResume() {
        super.onResume();
        ((MainActivity) parent).setActionBarTitle(HEADER_BAR);
    }

}
