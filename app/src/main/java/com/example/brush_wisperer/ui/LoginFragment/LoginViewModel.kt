package com.example.brush_wisperer.ui.LoginFragment


import android.app.AlertDialog
import android.app.Application
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.brush_wisperer.Data.Model.User
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.MainViewModel
import com.example.brush_wisperer.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = Firebase.auth
    private val userDb = RepositoryFirebase()
    private val mainViewModel = MainViewModel(application)
    val navigateToVerification = MutableLiveData<Boolean>()

    val user: LiveData<FirebaseUser?> = userDb.user

    fun signUpNewUserWithEmail(username: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.let {
                val userId = user.uid
                val user = User(
                    uid = userId.toString(),
                    username,
                    email
                )
                userDb.newUser(user)

                auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                    if (emailTask.isSuccessful) {
                        mainViewModel.showToastAtTop(getApplication(), "Verification email sent to $email")
                        navigateToVerification.value = true
                    } else {
                        Log.w("TAG", "Error sending email verification", emailTask.exception)
                    }
                }
                }
            }else {
                Log.w("TAG", "createUserWithEmail:failed", task.exception)
            }
        }
    }

    fun login(enteredEmail: String, enteredPassword: String) {
        userDb.loginUser(enteredEmail, enteredPassword)
    }

    // TODO Dialog zurück ins Fragment verschieben
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

            if (enteredPassword == enteredPasswordConfirm && enteredEmail == enteredEmailConfirm) {

                signUpNewUserWithEmail(enteredUserName, enteredEmail, enteredPassword)

            } else {
                Toast.makeText(view.context, android.R.string.ok, Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun updateCurrentUser() {
        userDb._user.postValue(auth.currentUser)
    }

    fun logOut() {
        userDb.logOut()
    }
}
