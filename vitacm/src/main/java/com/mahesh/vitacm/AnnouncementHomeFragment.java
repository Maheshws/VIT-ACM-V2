package com.mahesh.vitacm;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Mahesh on 12/27/13.
 */
public class AnnouncementHomeFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";


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
        defaultMessage.setText("Announcements Coming In Next Update");
    }

}
