package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth

class landingpage : AppCompatActivity() {
    var login: CardView? = null
    var register: CardView? = null
    var about: CardView? = null
    var fAuth: FirebaseAuth? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landingpage)
        login = findViewById(R.id.cardLogin)
        register = findViewById(R.id.cardRegister)
        about = findViewById(R.id.cardAboutus)
        fAuth = FirebaseAuth.getInstance()
        if (fAuth.getCurrentUser() != null) {
            val intent = Intent(this@landingpage, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        login.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                startActivity(Intent(getApplicationContext(), Logup::class.java))
            }
        })
        register.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                startActivity(Intent(getApplicationContext(), Signup::class.java))
            }
        })
        about.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                startActivity(Intent(getApplicationContext(), About::class.java))
            }
        })
    }
}