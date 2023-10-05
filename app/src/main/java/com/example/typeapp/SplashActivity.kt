package com.example.typeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
       Handler().postDelayed({
           val i = Intent(this@SplashActivity,PhoneActivity::class.java)
           startActivity(i)
           finish()
       },3000)

    }
}