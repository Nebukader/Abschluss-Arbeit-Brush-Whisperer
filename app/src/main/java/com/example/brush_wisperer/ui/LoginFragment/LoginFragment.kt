package com.example.brush_wisperer.ui.LoginFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
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


    //Login
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //verify if user email is verified
        viewModel.navigateToVerification.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigate(R.id.action_loginFragment_to_verificationFragment)
                viewModel.navigateToVerification.value = false
            }
        })

        viewModel.user.observe(viewLifecycleOwner, Observer {
                user ->
            if (user != null){
                findNavController().navigate(R.id.homeFragment)
                val toolbar = (activity as MainActivity).showToolBar(View.VISIBLE)
            }
        })


        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }


        //Register
        binding.signUp.setOnClickListener {
            viewModel.signUpDialog(view)
        }

        // Google Login with Firebase
        val option = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        client = GoogleSignIn.getClient(requireContext(), option)

        binding.signInWithGoogle.setOnClickListener {
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
                        viewModel.updateCurrentUser()
                    }else{
                        Toast.makeText(requireContext(),"${getString(R.string.login_failed)}", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}
