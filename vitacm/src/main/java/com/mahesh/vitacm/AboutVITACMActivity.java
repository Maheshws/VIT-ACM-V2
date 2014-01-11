package com.mahesh.vitacm;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by Mahesh on 1/3/14.
 */
public class AboutVITACMActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_vitacm_activity);
        TextView aboutText = (TextView) findViewById(R.id.aboutText);
        String message = "The Association for Computing Machinery is the world's largest educational computing society and the premier membership organization for computing professionals. ACM aims to advance computing as a science as well as a profession by way of delivering resources such as journals, conferences, workshops and seminars.\n\nOur goal at the Vidyalankar ACM is to bring home this mandate and advance the applications of technology. We strive to disseminate this information by organizing guest lectures, workshops conducted by eminent personalities in that field, projects and various other activities.\n\nWe also aim to give back to society and the College by tutoring and spreading awareness of technology and the applications of computing.\n\n Visit our website\nhttp://vitmumbai.acm.org/";
        aboutText.setText(message);
    }
}
