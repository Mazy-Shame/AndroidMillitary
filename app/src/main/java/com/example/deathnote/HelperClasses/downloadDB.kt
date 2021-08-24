package com.example.deathnote.HelperClasses

import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.getSystemService
import com.example.deathnote.R
import com.example.deathnote.Storage


class downloadDB  {


    fun ondownload(URL:String, downloadManager: DownloadManager){

            val fileget = GetFileName()         //имя стандартное
            fileget.doInBackground(URL)

            val uri = Uri.parse(URL)
            val request = DownloadManager.Request(uri)
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_MOBILE )
          //  request.setTitle("war")
            request.setAllowedOverRoaming(true)
            request.setDescription("Downloading")
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            //   request.setVisibleInDownloadsUi(false)


          request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM,"${Storage.filename}")

          downloadManager.enqueue(request)
    }


}
