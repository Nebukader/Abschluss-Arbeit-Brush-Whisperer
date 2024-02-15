package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopColourListBinding
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel

class Workshop_colour_list : Fragment() {

    private val viewModel: ColourViewModel by viewModels()
    private lateinit var binding: FragmentWorkshopColourListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWorkshopColourListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup Spinner
        val spinnerList = arrayOf("The Army Painter", "Games Workshop", "Vallejo","AK Interactive")
        val spinnerAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, spinnerList) }
        binding.brandSpinner.adapter = spinnerAdapter
    }
}