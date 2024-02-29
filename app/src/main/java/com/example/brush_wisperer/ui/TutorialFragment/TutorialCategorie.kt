package com.example.brush_wisperer.ui.TutorialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentTutorialCategorieBinding


class TutorialCategorie : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial_categorie, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTutorialCategorieBinding.bind(view)

        binding.beginnerCardView.setOnClickListener {
            val action = TutorialCategorieDirections.actionTutorialCategorieToTutorialBeginner()
            findNavController().navigate(action)
        }
        binding.advancedCV.setOnClickListener {
            val action = TutorialCategorieDirections.actionTutorialCategorieToTutorialAdvanced()
            findNavController().navigate(action)
        }
        binding.speedpaintCV.setOnClickListener {
            val action = TutorialCategorieDirections.actionTutorialCategorieToTutorialSpeedPaint()
            findNavController().navigate(action)
        }
        binding.airbrushCV.setOnClickListener {
            val action = TutorialCategorieDirections.actionTutorialCategorieToTutorialAirbrush()
            findNavController().navigate(action)
        }

    }
}