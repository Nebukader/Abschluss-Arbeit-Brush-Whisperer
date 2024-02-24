package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.brush_wisperer.databinding.FragmentWorkshopPopupColourAddBinding

class Workshop_popup_colour_add : Fragment() {

    private lateinit var binding: FragmentWorkshopPopupColourAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWorkshopPopupColourAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

