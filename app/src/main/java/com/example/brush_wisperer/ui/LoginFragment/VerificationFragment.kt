package com.example.brush_wisperer.ui.LoginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Erstellen Sie einen Timer, der alle 5 Sekunden die E-Mail-Bestätigung überprüft
        timer = Timer()
        timer.schedule(object : TimerTask() {
            override fun run() {
                val user = auth.currentUser
                user?.reload()?.addOnSuccessListener {
                    if (user.isEmailVerified) {
                        // Benutzer hat seine E-Mail bestätigt, navigieren Sie zum Home-Bildschirm
                        findNavController().navigate(R.id.action_verificationFragment_to_homeFragment)
                        timer.cancel() // Stoppen Sie den Timer, wenn die E-Mail-Bestätigung erkannt wird
                    }
                }
            }
            // Starten Sie sofort und wiederholen Sie alle 5 Sekunden
        }, 0, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stopp den Timer, wenn die Ansicht zerstört wird
        timer.cancel()
    }
}