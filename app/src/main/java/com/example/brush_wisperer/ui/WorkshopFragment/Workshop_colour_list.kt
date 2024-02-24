package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopColourListBinding
import com.example.brush_wisperer.ui.Adapter.ColourListAdapter
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel

class Workshop_colour_list : Fragment() {

    private val viewModel: ColourViewModel by viewModels()
    private lateinit var binding: FragmentWorkshopColourListBinding
    private var isDataLoaded = false


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

        val adapter = ColourListAdapter(viewModel)
        val recyclerView = binding.workshopColourListRV

        recyclerView.adapter = adapter

        viewModel.colourList.observe(viewLifecycleOwner) {
            if (!isDataLoaded){
                adapter.submitList(it)
                isDataLoaded = true
            }
        }

        val context = binding.workshopColourListRV.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)
        binding.workshopColourListRV.scheduleLayoutAnimation()

        // Setup Spinner
        val spinnerList = arrayOf("The Army Painter", "Games Workshop", "Vallejo","AK Interactive")
        val spinnerAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, spinnerList) }
        binding.brandSpinner.adapter = spinnerAdapter
    }
}