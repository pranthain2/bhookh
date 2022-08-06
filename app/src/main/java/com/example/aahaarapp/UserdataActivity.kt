package com.example.aahaarapp

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.os.Bundle
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.util.ArrayList
import java.util.List

class UserdataActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var datalist: ArrayList<model>? = null
    var db: FirebaseFirestore? = null
    var adapter: myadapter? = null
    var fAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var userID: String = fAuth.getCurrentUser().getUid()
    @Override
    protected fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.userdata)
        recyclerView = findViewById(R.id.rec_view) as RecyclerView?
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        datalist = ArrayList()
        adapter = myadapter(datalist)
        recyclerView.setAdapter(adapter)
        db = FirebaseFirestore.getInstance()
        db.collection("user data").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener(object : OnSuccessListener<QuerySnapshot?>() {
                @Override
                fun onSuccess(queryDocumentSnapshots: QuerySnapshot) {
                    val list: List<DocumentSnapshot> = queryDocumentSnapshots.getDocuments()
                    for (d in list) {
                        val obj: model = d.toObject(model::class.java)
                        //datalist.add(obj);
                        val Userid = d.get("userid") as String
                        if (Userid.equals(userID)) {
                            datalist.add(obj)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }
}