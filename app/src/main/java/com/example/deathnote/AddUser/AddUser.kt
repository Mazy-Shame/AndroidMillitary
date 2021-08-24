package com.example.deathnote.AddUser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.deathnote.R

class AddUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_user)
        supportActionBar?.hide()
    }
}