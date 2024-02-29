package com.example.brush_wisperer.ui.TutorialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentTutorialBeginnerBinding

class TutorialBeginner : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial_beginner, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTutorialBeginnerBinding.bind(view)

        binding.howToPaintAMiniCV.setOnClickListener {
            val videoID = "iWM9H3YcmZ8"
            val action = TutorialBeginnerDirections.actionTutorialBeginnerToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPrimeAminiCV.setOnClickListener {
            val videoID = "vuDDcQc9zrU"
            val action = TutorialBeginnerDirections.actionTutorialBeginnerToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPaintSkinCV.setOnClickListener {
            val videoID = "dARXzkBZnNU"
            val action = TutorialBeginnerDirections.actionTutorialBeginnerToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToBuildYourFirstMiniCV.setOnClickListener {
            val videoID = "LLJwh4ClZ6U"
            val action = TutorialBeginnerDirections.actionTutorialBeginnerToTutorialDetail(videoID)
            findNavController().navigate(action)
        }


    }
}