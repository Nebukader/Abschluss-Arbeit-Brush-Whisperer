package com.example.brush_wisperer.ui.LoginFragment


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.brush_wisperer.Data.Model.Profile
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginViewModel : ViewModel() {


    val auth = Firebase.auth
    val firestore = Firebase.firestore
    private lateinit var client: GoogleSignInClient



    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser?>
        get() = _user

    lateinit var profile: DocumentReference

    init {}

    fun setupUserEnv() {
        _user.value = auth.currentUser

        auth.currentUser?.let { firebaseUser ->
            profile = firestore.collection("users").document(firebaseUser.uid)
        }
    }

    fun register(username:String ,email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                setupUserEnv()

                val newProfile = Profile()
                profile.set(newProfile)

            } else {
                //Fehler aufgetreten
            }
        }
    }

    fun logOut() {
        auth.signOut()
        setupUserEnv()
    }
}
