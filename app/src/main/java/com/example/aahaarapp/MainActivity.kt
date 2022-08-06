package com.example.aahaarapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    var donate: CardView? = null
    var receive: CardView? = null
    var logout: CardView? = null
    var foodmap: CardView? = null
    var about: CardView? = null
    var contact: CardView? = null
    var mypin: CardView? = null
    var history: CardView? = null
    var fAuth: FirebaseAuth? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        donate = findViewById(R.id.cardDonate)
        receive = findViewById(R.id.cardReceive)
        logout = findViewById(R.id.cardLogout)
        foodmap = findViewById(R.id.cardFoodmap)
        mypin = findViewById(R.id.cardMyPin)
        history = findViewById(R.id.cardHistory)
        about = findViewById(R.id.cardAboutus)
        contact = findViewById(R.id.cardContact)
        fAuth = FirebaseAuth.getInstance()
        if (fAuth.getCurrentUser() == null) {
            val intent = Intent(this@MainActivity, landingpage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        donate.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), Donate::class.java)
                startActivity(intent)
            }
        })
        receive.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), Receive::class.java)
                startActivity(intent)
            }
        })
        foodmap.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), FoodMap::class.java)
                startActivity(intent)
            }
        })
        about.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), About::class.java)
                startActivity(intent)
            }
        })
        mypin.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), MyPin::class.java)
                startActivity(intent)
            }
        })
        history.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), UserdataActivity::class.java)
                startActivity(intent)
            }
        })
        contact.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val intent = Intent(getApplicationContext(), Contact::class.java)
                startActivity(intent)
            }
        })
        logout.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this@MainActivity, landingpage::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        })
    }
}