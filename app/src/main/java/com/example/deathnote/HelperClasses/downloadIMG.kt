package com.example.deathnote.HelperClasses

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import com.example.deathnote.Storage


class downloadIMG {
    fun ondownload(URL: String, downloadManager: DownloadManager){

        val fileget = GetFileName()         //имя стандартное
        fileget.doInBackground(URL)

        val uri = Uri.parse(URL)
        val request = DownloadManager.Request(uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_MOBILE)
        request.setDescription("Downloadingimg")
        request.setNotificationVisibility(1);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)


        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DCIM, "images.zip")

        var downloadID =  downloadManager.enqueue(request)

        val onDownloadComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                //Fetching the download id received with the broadcast
                val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                //Checking if the received broadcast is for our enqueued download by matching download id
                if (downloadID === id) {

                }
            }
        }
        


    }


}