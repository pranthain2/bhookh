package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap
import java.util.Map

class Signup : AppCompatActivity() {
    var mFullName: EditText? = null
    var mEmail: EditText? = null
    var mPassword: EditText? = null
    var mPhone: EditText? = null
    var mRegisterBtn: Button? = null
    var mLoginBtn: TextView? = null
    var fAuth: FirebaseAuth? = null
    var fStore: FirebaseFirestore? = null
    var userID: String? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        mFullName = findViewById(R.id.name)
        mEmail = findViewById(R.id.email)
        mPassword = findViewById(R.id.password)
        mPhone = findViewById(R.id.phone)
        mRegisterBtn = findViewById(R.id.register)
        mLoginBtn = findViewById(R.id.login)
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        if (fAuth.getCurrentUser() != null) {
            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            //finish();
            val intent = Intent(this@Signup, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        mRegisterBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                val email: String = mEmail.getText().toString().trim()
                val password: String = mPassword.getText().toString().trim()
                val name: String = mFullName.getText().toString().trim()
                val phone: String = mPhone.getText().toString().trim()
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
                fAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(object : OnCompleteListener<AuthResult?>() {
                        @Override
                        fun onComplete(@NonNull task: Task<AuthResult?>) {
                            if (task.isSuccessful()) {
                                Toast.makeText(this@Signup, "User Created.", Toast.LENGTH_SHORT)
                                    .show()
                                userID = fAuth.getCurrentUser().getUid()
                                val documentReference: DocumentReference =
                                    fStore.collection("users").document(userID)
                                val user: Map<String, Object> = HashMap()
                                user.put("name", name)
                                user.put("email", email)
                                user.put("phone", phone)
                                documentReference.set(user)
                                    .addOnSuccessListener(object : OnSuccessListener<Void?>() {
                                        @Override
                                        fun onSuccess(aVoid: Void?) {
                                            Log.d(
                                                TAG,
                                                "onSuccess: user Profile is created for $userID"
                                            )
                                            Toast.makeText(
                                                this@Signup,
                                                "Registered Successfully.",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    })
                                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                val intent = Intent(this@Signup, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@Signup,
                                    "Error!" + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
            }
        })
        mLoginBtn.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                startActivity(Intent(getApplicationContext(), Logup::class.java))
            }
        })
    }

    companion object {
        const val TAG = "TAG"
    }
}