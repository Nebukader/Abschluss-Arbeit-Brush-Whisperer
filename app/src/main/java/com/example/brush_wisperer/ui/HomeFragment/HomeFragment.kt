package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            // Verwenden Sie hier die Benutzerdaten
            binding.testTV.text = "Hallo $name"
        }
    }
}