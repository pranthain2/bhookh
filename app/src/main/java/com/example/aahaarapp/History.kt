package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity

class History : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val notebookref: CollectionReference = db.collection("user data")
    private var textViewData: TextView? = null
    var fAuth: FirebaseAuth? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        fAuth = FirebaseAuth.getInstance()
        textViewData = findViewById(R.id.data)
        loadNotes()
    }

    fun loadNotes() {
        notebookref.get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot?>() {
                @Override
                fun onComplete(@NonNull task: Task<QuerySnapshot?>) {
                    if (task.isSuccessful()) {
                        var data = ""
                        for (document in task.getResult()) {
                            Log.d(TAG, document.getId().toString() + " => " + document.getData()) //
                            if (document.contains("name") && document.contains("description") && document.contains(
                                    "user type"
                                ) && document.contains("userid")
                            ) {
                                val name = document.get("name") as String
                                val type = document.get("user type") as String
                                val description = document.get("description") as String
                                val phone = document.get("phone") as String
                                val Userid = document.get("userid") as String
                                val userID: String = fAuth.getCurrentUser().getUid()
                                val ts: Timestamp = document.get("timestamp") as Timestamp
                                val dateandtime: String = String.valueOf(ts.toDate())
                                if (Userid.equals(userID)) {
                                    data += "Name: $name\nUser Type: $type\nDescription: $description\n Contact: $phone\nDate & Time: $dateandtime\n"
                                }
                                textViewData.setText(data)
                            }
                        }
                        //textViewData.setText(data);
                    } else {
                        Log.d(TAG, "Error fetching data: ", task.getException())
                    }
                }
            })
    }

    companion object {
        const val TAG = "TAG"
    }
}