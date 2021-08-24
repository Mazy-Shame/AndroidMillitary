package com.example.deathnote.Options

import android.app.DownloadManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deathnote.HelperClasses.downloadDB
import com.example.deathnote.R
import com.example.deathnote.Storage
import java.io.File

class Options : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)
        supportActionBar?.hide()

    }


    private fun setOnclickListeners() {
        val setBig = findViewById<Button>(R.id.setbig)
        val setMedium = findViewById<Button>(R.id.setmedium)
        val setSmall = findViewById<Button>(R.id.setsmall)

        setBig.setOnClickListener {
            Storage.textsize = 20
            Toast.makeText(applicationContext, "Размер шрифта изменён на большой", Toast.LENGTH_SHORT).show()
        }
        setMedium.setOnClickListener {
            Storage.textsize = 17
            Toast.makeText(applicationContext, "Размер шрифта изменён на средний", Toast.LENGTH_SHORT).show()
        }
        setSmall.setOnClickListener {
            Storage.textsize = 15
            Toast.makeText(applicationContext, "Размер шрифта изменён на маленький", Toast.LENGTH_SHORT).show()
        }
    }




}