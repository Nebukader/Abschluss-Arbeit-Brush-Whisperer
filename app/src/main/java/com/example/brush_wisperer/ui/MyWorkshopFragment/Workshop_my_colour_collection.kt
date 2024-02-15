package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopMyColourCollectionBinding

class Workshop_my_colour_collection : Fragment() {

    private lateinit var viewModel: WorkshopViewModel
    private lateinit var binding: FragmentWorkshopMyColourCollectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWorkshopMyColourCollectionBinding.inflate(inflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addColourBtn.setOnClickListener {
            // Navigate to the add colour fragment
            findNavController().popBackStack()
            findNavController().navigate(Workshop_my_colour_collectionDirections.actionWorkshopMyColourCollectionToWorkshopColourList())
        }

        // Setup Spinner
        val spinnerList = arrayOf("The Army Painter", "Games Workshop", "Vallejo","AK Interactive")
        val spinnerAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, spinnerList) }
        binding.brandSpinner.adapter = spinnerAdapter

    }

}