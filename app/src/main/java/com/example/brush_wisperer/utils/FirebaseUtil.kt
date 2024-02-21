package com.example.brush_wisperer.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseUtil {
    private var currentUserID: String? = null

    fun getCurrentUserID(): String? {
        if (currentUserID == null) {
            currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        }
        return currentUserID
    }

    fun getDBInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}