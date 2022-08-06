package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.HashMap
import java.util.Map

class Contact : AppCompatActivity() {
    var name: EditText? = null
    var email: EditText? = null
    var message: EditText? = null
    var submit: Button? = null
    var isNameValid = false
    var isEmailValid = false
    var isMessageValid = false
    var fAuth: FirebaseAuth? = null
    var fStore: FirebaseFirestore? = null
    var userID: String? = null
    var nameError: TextInputLayout? = null
    var emailError: TextInputLayout? = null
    var messageError: TextInputLayout? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        name = findViewById(R.id.name) as EditText?
        email = findViewById(R.id.email) as EditText?
        message = findViewById(R.id.message) as EditText?
        submit = findViewById(R.id.submit) as Button?
        nameError = findViewById(R.id.nameError) as TextInputLayout?
        emailError = findViewById(R.id.emailError) as TextInputLayout?
        messageError = findViewById(R.id.messageError) as TextInputLayout?
        fAuth = FirebaseAuth.getInstance()
        fStore = FirebaseFirestore.getInstance()
        submit.setOnClickListener(object : OnClickListener() {
            @Override
            fun onClick(v: View?) {
                SetValidation()
            }
        })
    }

    fun SetValidation() {

        // Check for a valid name.
        if (name.getText().toString().isEmpty()) {
            nameError.setError(getResources().getString(R.string.name_error))
            isNameValid = false
        } else {
            isNameValid = true
            nameError.setErrorEnabled(false)
        }

        // Check for a valid email address.
        if (email.getText().toString().isEmpty()) {
            emailError.setError(getResources().getString(R.string.email_error))
            isEmailValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
            emailError.setError(getResources().getString(R.string.error_invalid_email))
            isEmailValid = false
        } else {
            isEmailValid = true
            emailError.setErrorEnabled(false)
        }

        // Check for a valid phone number.
        if (message.getText().toString().isEmpty()) {
            messageError.setError(getResources().getString(R.string.phone_error))
            isMessageValid = false
        } else {
            isMessageValid = true
            messageError.setErrorEnabled(false)
        }
        if (isNameValid && isEmailValid && isMessageValid) {
            val Name: String = name.getText().toString().trim()
            val Email: String = email.getText().toString().trim()
            val Message: String = message.getText().toString().trim()
            userID = fAuth.getCurrentUser().getUid()
            //DocumentReference documentReference = fStore.collection("donate").document(userID);
            val collectionReference: CollectionReference = fStore.collection("contact data")
            val user: Map<String, Object> = HashMap()
            user.put("timestamp", FieldValue.serverTimestamp())
            user.put("name", Name)
            user.put("email", Email)
            user.put("message", Message)
            user.put("userid", userID)
            collectionReference.add(user)
                .addOnSuccessListener(object : OnSuccessListener<DocumentReference?>() {
                    @Override
                    fun onSuccess(documentReference: DocumentReference?) {
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "Successfully! We will shortly revert you back.")
                        //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        val intent = Intent(this@Contact, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                })
                .addOnFailureListener(object : OnFailureListener() {
                    @Override
                    fun onFailure(@NonNull e: Exception?) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show()
                        Log.w(TAG, "Error!", e)
                    }
                })
        }
    }

    companion object {
        const val TAG = "TAG"
    }
}