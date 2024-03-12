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
import com.example.brush_wisperer.R
import com.example.brush_wisperer.UtilViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val auth = Firebase.auth
    private val userDb = RepositoryFirebase()
    private val utilViewModel = UtilViewModel(application)
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
                        utilViewModel.showToastAtTop(getApplication(), "Verification email sent to $email")
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

        val inputName: EditText = viewInflated.findViewById(R.id.usernameET)
        val inputPassword: EditText = viewInflated.findViewById(R.id.passwordET)
        val inputPasswordConfirm: EditText = viewInflated.findViewById(R.id.passwordConfirmET)
        val inputEmail: EditText = viewInflated.findViewById(R.id.emailET)
        val inputEmailConfirm: EditText = viewInflated.findViewById(R.id.emailConfirmET)

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

    fun forgottPassword(email: String) {
            auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utilViewModel.showToastAtTop(getApplication(), "Email sent to $email")
                } else {
                    Log.w("TAG", "Error sending email", task.exception)
                }
            }

    }
}
