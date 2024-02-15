package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.brush_wisperer.Data.Model.firestoreColour
import com.example.brush_wisperer.ui.Adapter.Workshop_colour_collection_adapter
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class WorkshopViewModel : ViewModel() {

    private lateinit var favouriteColoursArrayList: ArrayList<firestoreColour>
    private lateinit var db: FirebaseFirestore

    private fun getFavouriteColours(adapter : Workshop_colour_collection_adapter) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").addSnapshotListener(object : EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if (error != null) {
                    Log.e("Firestore Error",  error.message.toString())
                    return
                }
                for (dc : DocumentChange in value?.documentChanges!!) {

                    if (dc.type == DocumentChange.Type.ADDED) {
                        favouriteColoursArrayList.add(dc.document.toObject(firestoreColour::class.java))
                    }
                }
                adapter.notifyDataSetChanged()
            }
        })
    }
}