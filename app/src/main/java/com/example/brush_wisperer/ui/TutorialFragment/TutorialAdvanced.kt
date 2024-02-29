package com.example.brush_wisperer.ui.TutorialFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentTutorialAdvancedBinding

class TutorialAdvanced : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial_advanced, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTutorialAdvancedBinding.bind(view)

        binding.howToPaintNonMetallicMetalCV.setOnClickListener {
            val videoID = "tkcC7O94X2Q"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPaintTrueMetallicMetalCV.setOnClickListener {
            val videoID = "PB0NeOA4bpU"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howtoPaintOrcWarriorCV.setOnClickListener {
            val videoID = "5Fs0K-CQ0l4"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPaintTerminatorCV.setOnClickListener {
            val videoID = "VokOTQDsgUw"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howtoPaintChaosSpacemarineCV.setOnClickListener {
            val videoID = "KQbeU5betOg"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPaintVictoriaCV.setOnClickListener {
            val videoID = "vNPQKO-xWfs"
            val action = TutorialAdvancedDirections.actionTutorialAdvancedToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
    }
}