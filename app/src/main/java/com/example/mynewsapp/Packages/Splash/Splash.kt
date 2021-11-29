package com.example.mynewsapp.Packages.Splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mynewsapp.Packages.DashBoard.DashBoard
import com.example.mynewsapp.R


lateinit var handler: Handler

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        handler = Handler()
        handler.postDelayed({

            val intent = Intent(this, DashBoard::class.java)
            startActivity(intent)
        },2500)
        }
    }
