package com.example.brush_wisperer.Data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.brush_wisperer.Data.Model.User
import com.example.brush_wisperer.utils.FirebaseUtil
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class RepositoryFirebase {

    private val db = FirebaseUtil.getDBInstance()
    private val auth = Firebase.auth

    val _user = MutableLiveData<FirebaseUser?>(auth.currentUser)
    val user: LiveData<FirebaseUser?>
        get() = _user

    private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->

        _user.postValue(auth.currentUser)
        Log.d("TAG", "User logged in: ${user.toString()}")
    }

    fun logOut() {
        auth.signOut()
        _user.postValue(null)
    }

    fun newUser(user: User) {
        db.collection("users")
            .document(user.uid ?: "")
            .set(user)
            .addOnSuccessListener { documentReference ->
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }
    private fun sendVerificationEmail() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "Verification email sent to ${user.email}")
                }else
                    Log.w("TAG", "Error sending verification email", task.exception)
            }
    }


    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                }
            }
        auth.addAuthStateListener(authStateListener)
    }

    fun getCurrentUserID(): String? {
        return FirebaseUtil.getCurrentUserID()
    }
    fun getDBInstance(): FirebaseFirestore {
        return FirebaseUtil.getDBInstance()
    }
    fun getStorageRef(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }
}