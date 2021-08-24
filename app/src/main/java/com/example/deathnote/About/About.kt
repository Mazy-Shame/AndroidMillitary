package com.example.deathnote.About

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.deathnote.R

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.hide()
    }
}