package com.example.aahaarapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

class myadapter(datalist: ArrayList<model?>) : RecyclerView.Adapter<myadapter.myviewholder?>() {
    var datalist: ArrayList<model>
    var fAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var userID: String = fAuth.getCurrentUser().getUid()
    var uid: String? = null
    @NonNull
    @Override
    fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): myviewholder {
        val view: View =
            LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow, parent, false)
        return myviewholder(view)
    }

    @Override
    fun onBindViewHolder(@NonNull holder: myviewholder, position: Int) {
        holder.tname.setText(datalist.get(position).getName())
        holder.ttype.setText(datalist.get(position).getType())
        holder.tdescription.setText(datalist.get(position).getDescription())
    }

    fun deleteItem(position: Int) {
        //getSnapshots().getSnapshot(position).getReference().delete();
        //notifyDataSetChanged();
    }

    @get:Override
    val itemCount: Int
        get() = datalist.size()

    internal inner class myviewholder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tname: TextView
        var ttype: TextView
        var tdescription: TextView

        init {
            tname = itemView.findViewById(R.id.name)
            ttype = itemView.findViewById(R.id.type)
            tdescription = itemView.findViewById(R.id.description)
        }
    }

    init {
        this.datalist = datalist
    }
}