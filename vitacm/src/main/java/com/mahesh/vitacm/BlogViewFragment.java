package com.mahesh.vitacm;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Mahesh on 12/30/13.
 */
public class BlogViewFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static BlogObject currentBlog;
    private Activity parent;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * @param sectionNumber
     */
    public static BlogViewFragment newInstance(BlogObject sectionNumber) {
        BlogViewFragment fragment = new BlogViewFragment();
        Bundle args = new Bundle();
        currentBlog = sectionNumber;
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.blog_view_fragment, container, false);
        ((MainActivity) parent).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView Titletext = (TextView) getActivity().findViewById(R.id.BlogTitle);
        Titletext.setText(currentBlog.getBlogTitle());

        TextView Bytext = (TextView) getActivity().findViewById(R.id.BlogBy);
        Bytext.setText("Posted by: " + currentBlog.getBlogBy());

        TextView Timetext = (TextView) getActivity().findViewById(R.id.BlogTime);
        Timetext.setText("On " + currentBlog.getBlogTime());


        WebView contentView= (WebView) getActivity().findViewById(R.id.webView);
        contentView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        contentView.getSettings().setLoadWithOverviewMode(true);
        contentView.getSettings().setUseWideViewPort(true);
        contentView.getSettings().setBuiltInZoomControls(true);
        contentView.setBackgroundColor(Color.TRANSPARENT);
        contentView.loadDataWithBaseURL(null,currentBlog.getBlogContent(), "text/html","UTF-8", null);
    }
}

