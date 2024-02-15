package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopCategorieBinding

class Workshop_categorie : Fragment() {

    private var _binding: FragmentWorkshopCategorieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWorkshopCategorieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.colourCollectionCV.setOnClickListener {
            val action =
                Workshop_categorieDirections.actionWorkshopCategorieToWorkshopMyColourCollection()
            findNavController().navigate(action)
        }
    }
}