package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.databinding.FragmentWorkshopCategorieBinding
class WorkshopCategorie : Fragment() {

    private lateinit var binding: FragmentWorkshopCategorieBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopCategorieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.colourCollectionCV.setOnClickListener {
            val action = WorkshopCategorieDirections.actionWorkshopCategorieToWorkshopMyColourCollection()
            findNavController().navigate(action)
        }
        binding.projectsCV.setOnClickListener {
            val action = WorkshopCategorieDirections.actionWorkshopCategorieToWorkshopNewProject()
            findNavController().navigate(action)
        }
        binding.wishlistCV.setOnClickListener {
            val action = WorkshopCategorieDirections.actionWorkshopCategorieToWorkshopWishlist()
            findNavController().navigate(action)
        }
    }
}