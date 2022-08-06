package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle

class SplashScreen : AppCompatActivity() {
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this, landingpage::class.java))
        finish()
    }
}