package com.mahesh.vitacm;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by Mahesh on 12/24/13.
 */
public class UtilitiesMethod {

    //private static final String DOMAIN="http://192.168.2.7/app/";
    private static final String DOMAIN = "http://vitmumbai.acm.org/app/";
    private static boolean ErrorFlag = false;
    private Context context;

    public void setContext(Context c) {
        context = c;
    }

    public void getBlog() {
        String URL = "getblog.php";
        String RESULT;
        String toFile = DOMAIN + URL;
        InputStream isr = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(toFile);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            RESULT = sb.toString();
            if (RESULT == null) {
                Log.e("log_utils_blog", "Error in http connection ");
                ErrorFlag = true;
            } else {
                SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("blogslist", RESULT);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e("log_utils_blog", "Error in http connection " + e.toString());
            ErrorFlag = true;
        }
    }

    public void getEvents() {
        String URL = "getevents.php";
        String RESULT;
        String toFile = DOMAIN + URL;
        InputStream isr = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(toFile);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            RESULT = sb.toString();
            if (RESULT == null) {
                Log.e("log_utils_blog", "Error in http connection ");
                ErrorFlag = true;
            } else {
                SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("eventslist", RESULT);
                editor.commit();
            }
        } catch (Exception e) {
            Log.e("log_utils_events", "Error in http connection " + e.toString());
            ErrorFlag = true;
        }
    }

    public void getFacts() {
        String URL = "facts.php";
        String RESULT;
        String toFile = DOMAIN + URL;
        InputStream isr = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(toFile);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            isr = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            isr.close();
            RESULT = sb.toString();
            if (RESULT == null) {
                Log.e("log_utils_facts", "Error in http connection for Facts");
                ErrorFlag = true;
            } else {
                SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("facts", RESULT);
                editor.commit();
            }

        } catch (Exception e) {
            Log.e("log_utils_facts", "Error in http connection facts" + e.toString());
            ErrorFlag = true;
        }

    }

    public boolean getErrorFlag() {
        return ErrorFlag;
    }

    //used in blog fragment.
    public void fillMyBlog(List<BlogObject> myblog, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
        String result = prefs.getString("blogslist", null);
        try {
            JSONArray jArray = new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                myblog.add(new BlogObject(json.getInt("ID"), json.getString("By"), json.getString("Title"), json.getString("Time"), json.getString("Image"), json.getString("Content")));

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag_blog", "Error Parsing Data " + e.toString());
        }

    }
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }


}
