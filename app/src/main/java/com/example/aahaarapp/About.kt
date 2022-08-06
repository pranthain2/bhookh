package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View

class About : AppCompatActivity() {
    var instagram: CardView? = null
    var facebook: CardView? = null
    var twitter: CardView? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        instagram = findViewById(R.id.instagram)
        facebook = findViewById(R.id.facebook)
        twitter = findViewById(R.id.twitter)
        instagram.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val myWebLink = Intent(android.content.Intent.ACTION_VIEW)
                myWebLink.setData(Uri.parse("http://www.instagram.com"))
                startActivity(myWebLink)
            }
        })
        facebook.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val myWebLink = Intent(android.content.Intent.ACTION_VIEW)
                myWebLink.setData(Uri.parse("http://www.facebook.com"))
                startActivity(myWebLink)
            }
        })
        twitter.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val myWebLink = Intent(android.content.Intent.ACTION_VIEW)
                myWebLink.setData(Uri.parse("http://www.twitter.com"))
                startActivity(myWebLink)
            }
        })
    }
}