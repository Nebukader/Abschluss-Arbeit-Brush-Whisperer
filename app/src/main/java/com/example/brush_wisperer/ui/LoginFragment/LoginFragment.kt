package com.example.brush_wisperer.ui.LoginFragment

import android.app.AlertDialog
import android.content.Intent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.MainActivity

import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()
    private lateinit var binding: FragmentLoginBinding
    private lateinit var client: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginBtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Login")

            val viewInflated: View =
                LayoutInflater.from(context).inflate(R.layout.login_popup_dialog, null)
            val inputEmail = viewInflated.findViewById(R.id.username) as EditText
            val inputPassword = viewInflated.findViewById(R.id.password) as EditText

            builder.setView(viewInflated)

            builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
                dialog.dismiss()
                val enteredEmail = inputEmail.text.toString()
                val enteredPassword = inputPassword.text.toString()

                viewModel.register(enteredEmail, enteredPassword)
            }

            builder.setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }

            builder.show()
        }

        // Google Login with Firebase

        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(requireContext(), option)

        binding.signInGoogleBtn.setOnClickListener {
            val intent = client.signInIntent
            startActivityForResult(intent, 10001)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 10001){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        val user = FirebaseAuth.getInstance().currentUser
                        val email = user?.email
                        Toast.makeText(requireContext(), "${getString(R.string.login_success)} $email", Toast.LENGTH_SHORT).show()
                        val nav = findNavController()
                        nav.navigate(R.id.action_loginFragment_to_homeFragment)
                    }else{
                        Toast.makeText(requireContext(),"${getString(R.string.login_failed)}", Toast.LENGTH_SHORT).show()
                    }

                }
        }
    }

}