package com.example.deathnote.HelperClasses;


import android.os.AsyncTask;
import android.webkit.CookieManager;

import com.example.deathnote.Storage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetFileName extends AsyncTask<String, Integer, String>
{
    protected String doInBackground(String... urls)
    {
        URL url;
        String filename = null;
        try {
            url = new URL(urls[0]);
            String cookie = CookieManager.getInstance().getCookie(urls[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Cookie", cookie);
            con.setRequestMethod("HEAD");
            con.setInstanceFollowRedirects(false);
            con.connect();

            String content = con.getHeaderField("Content-Disposition");
            String contentSplit[] = content.split("filename=");
            filename = contentSplit[1].replace("filename=", "").replace("\"", "").trim();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
        }

        Storage.Companion.setFilename(filename);
        return filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void onPostExecute(String result) {

    }
}
