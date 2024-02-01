package com.example.brush_wisperer.ui.LoginFragment


import android.app.AlertDialog
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.Data.Model.Profile
import com.example.brush_wisperer.R
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore


class LoginViewModel(application: Application): AndroidViewModel(application) {


    val auth = Firebase.auth
    private val db = Firebase.firestore
    private val userDb = RepositoryFirebase()
    private val nav = LoginFragment().findNavController()

    fun signUpNewUser(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = hashMapOf(
                    "username" to username,
                    "email" to email
                )
                userDb.newUser(user)
            } else {
                Log.w("TAG", "createUserWithEmail:failure", task.exception)
            }
        }
    }

    fun loginDialog(context: View) {
        val builder = AlertDialog.Builder(context.context)
        builder.setTitle("Login")

        val viewInflated: View =
            LayoutInflater.from(context.context).inflate(R.layout.login_popup_dialog, null)
        val inputEmail = viewInflated.findViewById(R.id.username) as EditText
        val inputPassword = viewInflated.findViewById(R.id.password) as EditText

        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
            val enteredEmail = inputEmail.text.toString()
            val enteredPassword = inputPassword.text.toString()

            userDb.loginUser(enteredEmail, enteredPassword)
            if (userDb.user != null) {
                nav.navigate(R.id.action_loginFragment_to_homeFragment)
                Log.d("TAG", "loginDialog: ${auth.currentUser}")
            } else {
                Log.d("TAG", "loginDialog: ${auth.currentUser}")
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()

    }


    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: MutableLiveData<FirebaseUser?>
        get() = _user

    lateinit var profile: DocumentReference

    init {}

    fun setupUserEnv() {
        _user.value = auth.currentUser

        auth.currentUser?.let { firebaseUser ->
            profile = db.collection("users").document(firebaseUser.uid)
        }
    }

    fun register(username: String, email: String, password: String) {

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
