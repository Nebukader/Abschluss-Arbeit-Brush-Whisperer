package com.example.brush_wisperer.ui.LoginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.MainActivity
import com.example.brush_wisperer.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.Timer
import java.util.TimerTask

class VerificationFragment : Fragment() {

    private val auth = Firebase.auth
    private lateinit var timer: Timer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Erstellt einen Timer, der alle 5 Sekunden die E-Mail-Bestätigung überprüft
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val user = auth.currentUser
                user?.reload()?.addOnSuccessListener {
                    if (user.isEmailVerified) {
                        // Benutzer hat seine E-Mail bestätigt, navigieren Sie zum Home-Bildschirm
                        findNavController().navigate(R.id.action_verificationFragment_to_homeFragment)
                        val toolbar = (activity as MainActivity).showToolBar(View.VISIBLE)
                        timer.cancel() // Stoppen Sie den Timer, wenn die E-Mail-Bestätigung erkannt wird
                    }
                }
            }
            // Schaut alle 5 sekunden ob der User seine Email bestätigt hat
        }, 0, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stopp den Timer, wenn das Fragment zerstört wird
        timer.cancel()
    }
}