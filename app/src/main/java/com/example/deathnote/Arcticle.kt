package com.example.deathnote

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import java.io.File


class Arcticle() : AppCompatActivity() {



    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arcticle)
        supportActionBar?.hide()


        val ContentText = findViewById<TextView>(R.id.ContentText)
        ContentText.text = Storage.text
        ContentText.setTextSize(Storage.textsize.toFloat())

        if (Storage.text == "presentation") {

            val file = File(path + "/images1/${Storage.img}")
            Log.d("filepath", "${file.path}")
            if (file.exists()) {
                Toast.makeText(applicationContext, "Существует", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Не существует", Toast.LENGTH_LONG).show()
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.openxmlformats-officedocument.presentationml.presentation")

            startActivity(intent)



            finish()
        }

        else{

            val imgcontent = findViewById<SubsamplingScaleImageView>(R.id.imageContent)

        imgcontent.setImage(
            ImageSource.uri(
                Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM                    //Ставим изображение из ExternalStorage
                ).absolutePath + "/images1/${Storage.img}"
            )
        )


        }



    }

}