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
import java.io.File;
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
    private static boolean downloadBlog=true;
    private static boolean downloadAnnouncement=true;
    int serverAnnouncementindex=0;

    public void setContext(Context c) {
        context = c;
    }

    public void getIndex() {
        int currentBlogIndex=0;
        int currentAnnouncementIndex=0;
        int serverBlogIndex=0;

        String URL = "getlatestindex.php";
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
            }
            else{
                String[] res=RESULT.split(",");
                serverBlogIndex=Integer.parseInt(res[0]);
                res[1]=res[1].replace("\"","").trim();
                serverAnnouncementindex=Integer.parseInt(res[1]);
                SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
                currentBlogIndex=prefs.getInt("currentBlog",0);
                currentAnnouncementIndex=prefs.getInt("currentAnnouncement",0);
                SharedPreferences.Editor editor = prefs.edit();
                if(currentAnnouncementIndex<serverAnnouncementindex) {
                    //editor.putInt("currentAnnouncement", serverAnnouncementindex);
                    Log.e("log_utils_index", "current announcement changed");
                }
                else if(currentAnnouncementIndex>=serverAnnouncementindex){
                    downloadAnnouncement=false;
                    Log.e("log_utils_index", "download announcement set false");
                }
                if(currentBlogIndex<serverBlogIndex) {
                    editor.putInt("currentBlog", serverBlogIndex);
                    Log.e("log_utils_index", "current blog changed");
                }
                else if(currentBlogIndex>=serverBlogIndex) {
                    downloadBlog=false;
                    Log.e("log_utils_index", "download blog set false");
                }
                editor.commit();
                Log.e("log_utils_index", "Server - Annc " + serverAnnouncementindex + "\nServer - Blog " + serverBlogIndex + "\nCurrent Annc " + currentAnnouncementIndex + "\nCurrent Blog " + currentBlogIndex);
            }
        } catch (Exception e) {
            Log.e("log_utils_blog", "Error in http connection " + e.toString());
            ErrorFlag = true;
        }
    }

    public void getBlog() {
        if(downloadBlog) {
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

    public void getAnnouncements() {
        if(downloadAnnouncement){
            String URL = "getannouncements.php";
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
                    Log.e("log_utils_announcements", "Error in http connection ");
                    ErrorFlag = true;
                } else {
                    SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("currentAnnouncement", serverAnnouncementindex);
                    editor.putString("announcementslist", RESULT);
                    editor.commit();
                }
            } catch (Exception e) {
                Log.e("log_utils_announcements", "Error in http connection " + e.toString());
                ErrorFlag = true;
            }
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
    public void fillMyAnnouncements(List<AnnouncementObject> myannounce, Context context) {
        SharedPreferences prefs = context.getSharedPreferences("app_data", 0);
        String result = prefs.getString("announcementslist", null);
        try {
            JSONArray jArray = new JSONArray(result);

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json = jArray.getJSONObject(i);
                myannounce.add(new AnnouncementObject(json.getInt("id"), json.getString("title"), json.getString("message")));

            }

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("log_tag_announcement", "Error Parsing Data for announcement" + e.toString());
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


    public void clearApplicationData() {
        SharedPreferences prefs = context.getSharedPreferences("user_account_info", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        prefs=context.getSharedPreferences("app_data",0);
        editor = prefs.edit();
        editor.remove("currentAnnouncement");
        editor.remove("currentBlog");
        editor.commit();
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"VITACM");
        else
            cacheDir=context.getCacheDir();
        if(cacheDir.exists())
            deleteDir(cacheDir);
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
