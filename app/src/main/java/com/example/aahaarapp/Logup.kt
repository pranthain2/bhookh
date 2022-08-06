package com.example.aahaarapp

import android.content.Intent
import android.text.TextUtils
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Logup : AppCompatActivity() {
    var mEmail: EditText? = null
    var mPassword: EditText? = null
    var mLoginBtn: Button? = null
    var mRegisterBtn: TextView? = null
    var fAuth: FirebaseAuth? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logup)
        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)
        mRegisterBtn = findViewById(R.id.register)
        mLoginBtn = findViewById(R.id.login)
        fAuth = FirebaseAuth.getInstance()
        if (fAuth.getCurrentUser() != null) {
            val intent = Intent(this@Logup, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        mLoginBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val email: String = mEmail.getText().toString().trim()
                val password: String = mPassword.getText().toString().trim()
                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is Required.")
                    return
                }
                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is Required.")
                    return
                }
                if (password.length() < 6) {
                    mPassword.setError("Password Must be >=6 Characters")
                    return
                }

                //authenticate the user
                fAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult?>() {
                        @Override
                        fun onComplete(@NonNull task: Task<AuthResult?>) {
                            if (task.isSuccessful()) {
                                Toast.makeText(
                                    this@Logup,
                                    "Logged in Successfully.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this@Logup, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@Logup,
                                    "Error! " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        })
        mRegisterBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                // redirect to RegisterActivity
                val intent = Intent(getApplicationContext(), Signup::class.java)
                startActivity(intent)
            }
        })
    }
}