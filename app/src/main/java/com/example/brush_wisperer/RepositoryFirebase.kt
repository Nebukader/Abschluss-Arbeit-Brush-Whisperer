package com.example.brush_wisperer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class RepositoryFirebase {

    private val db = Firebase.firestore
    private val auth = Firebase.auth

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser?>
        get() = _user


    fun newUser(user: HashMap<String, String>){
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    fun loginUser(email: String , password:String) {
        auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithEmail:success")
                    _user.postValue(auth.currentUser)
                } else {
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                }
            }
    }
}