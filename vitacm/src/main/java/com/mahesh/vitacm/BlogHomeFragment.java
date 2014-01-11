package com.mahesh.vitacm;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mahesh on 12/27/13.
 */
public class BlogHomeFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static String HEADER_BAR;
    private Activity parent;

    private List<BlogObject> allBlog = new ArrayList<BlogObject>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blog_fragment_layout, container, false);
    }

    public static BlogHomeFragment newInstance(int sectionNumber) {
        BlogHomeFragment fragment = new BlogHomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        HEADER_BAR = getString(R.string.title_section5);
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

        TextView defaultMessage = (TextView) getActivity().findViewById(R.id.fragment_message);
        defaultMessage.setText("Blog Coming In Next Update");
        if (allBlog.isEmpty()) {
            UtilitiesMethod utils = new UtilitiesMethod();
            utils.fillMyBlog(allBlog, getActivity());
        }
        populateListView();

        registerClickCallback();
        defaultMessage.setVisibility(View.INVISIBLE);

    }

    private void populateListView() {
        ArrayAdapter<BlogObject> adapter = new MyBlogListAdapter(this.getActivity(), R.layout.blog_item_layout, allBlog);
        ListView list = (ListView) getActivity().findViewById(R.id.blogslistView);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView) getActivity().findViewById(R.id.blogslistView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {

                BlogObject clickedBlog = allBlog.get(position);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, BlogViewFragment.newInstance(clickedBlog))
                        .addToBackStack(null)
                        .commit();

            }
        });

    }

    private class MyBlogListAdapter extends ArrayAdapter<BlogObject> {
        ImageView imageView;
        BlogObject currentBlog;

        public MyBlogListAdapter(Context context, int resource, List<BlogObject> objects) {
            super(context, resource, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = getLayoutInflater(null).inflate(R.layout.blog_item_layout, parent, false);
            }

            currentBlog = allBlog.get(position);

            TextView Titletext = (TextView) view.findViewById(R.id.item_title);
            Titletext.setText(currentBlog.getBlogTitle());

            TextView Bytext = (TextView) view.findViewById(R.id.item_by);
            Bytext.setText("Posted by: " + currentBlog.getBlogBy());

            TextView Timetext = (TextView) view.findViewById(R.id.item_time);
            Timetext.setText("On " + currentBlog.getBlogTime());

            imageView = (ImageView) view.findViewById(R.id.item_img);
            ImageLoader imgLoader = new ImageLoader(parent.getContext());
            imgLoader.DisplayImage(currentBlog.getBlogImage(), R.drawable.ic_launcher, imageView);

            return view;
        }

    }


}
