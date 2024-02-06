package com.example.brush_wisperer.ui.LoginFragment


import android.app.AlertDialog
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.Data.Model.Profile
import com.example.brush_wisperer.R
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.MainViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore


class LoginViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    val auth = Firebase.auth
    private val db = Firebase.firestore
    private val userDb = RepositoryFirebase()
    private val mainViewModel = MainViewModel(application)

    val user = userDb.user

    fun signUpNewUserWithEmail(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = hashMapOf(
                    "username" to username,
                    "email" to email
                )
                userDb.newUser(user)

                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        mainViewModel.showToastAtTop(getApplication(), "Verification email sent to $email")
                    } else {
                        Log.w("TAG", "Error sending email verification", emailTask.exception)
                    }
                }
            }else {
                Log.w("TAG", "createUserWithEmail:failed", task.exception)
            }
        }
    }

    fun loginDialog(enteredEmail: String, enteredPassword: String) {

        userDb.loginUser(enteredEmail, enteredPassword)
        if (userDb.user != null) {
            Log.d("TAG", "loginDialog: ${auth.currentUser}")
        } else {
            Log.d("TAG", "loginDialog: ${auth.currentUser}")
        }
    }

    fun signUpDialog(view: View) {
        val builder = AlertDialog.Builder(view.context)
        builder.setTitle("Register")

        val viewInflated: View =
            LayoutInflater.from(view.context).inflate(R.layout.signup_login, null)

        val inputName = viewInflated.findViewById(R.id.username) as EditText
        val inputPassword = viewInflated.findViewById(R.id.password) as EditText
        val inputPasswordConfirm = viewInflated.findViewById(R.id.passwordConfirm) as EditText
        val inputEmail = viewInflated.findViewById(R.id.email) as EditText
        val inputEmailConfirm = viewInflated.findViewById(R.id.emailConfirm) as EditText

        builder.setView(viewInflated)

        builder.setPositiveButton(R.string.Confirm) { dialog, _ ->
            dialog.dismiss()
            val enteredUserName = inputName.text.toString()
            val enteredPassword = inputPassword.text.toString()
            val enteredPasswordConfirm = inputPasswordConfirm.text.toString()
            val enteredEmail = inputEmail.text.toString()
            val enteredEmailConfirm = inputEmailConfirm.text.toString()

            if (enteredUserName == enteredUserName && enteredPassword == enteredPasswordConfirm && enteredEmail == enteredEmailConfirm) {

                signUpNewUserWithEmail(enteredUserName, enteredEmail, enteredPassword)

            } else {
                Toast.makeText(view.context, android.R.string.ok, Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun signInAnonymously() {
        userDb.authSignInAnonymously()
    }

    fun updateCurrentUser() {
        userDb._user.postValue(auth.currentUser)
    }
}
