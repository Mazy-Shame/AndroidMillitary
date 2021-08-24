package com.example.deathnote.HelperClasses;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.deathnote.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyClass {

    String uri;
    private final Context mContext;

    public CopyClass(Context context) {
        this.mContext = context;
    }


   public void copy() throws IOException {



        uri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/war1.db";


        File file = new File(uri);


        FileInputStream in = new FileInputStream(uri);


        OutputStream out = new FileOutputStream("data/data/com.example.deathnote/databases/war4.sqlite3");

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();

    }



}
