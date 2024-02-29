package com.example.brush_wisperer.ui.TutorialFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentTutorialAirbrushBinding
import com.example.brush_wisperer.databinding.FragmentTutorialSpeedPaintBinding


class TutorialAirbrush: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial_airbrush, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTutorialAirbrushBinding.bind(view)

        binding.howToAirbrushBeginnerCV.setOnClickListener {
            val videoID = "iWM9H3YcmZ8"
            val action = TutorialAirbrushDirections.actionTutorialAirbrushToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToAirbrushCommonIssuesCV.setOnClickListener {
            val videoID = "98V310pQEaM"
            val action = TutorialAirbrushDirections.actionTutorialAirbrushToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToAirbrushGhostFluoCV.setOnClickListener {
            val videoID = "01R8jN7gxlM"
            val action = TutorialAirbrushDirections.actionTutorialAirbrushToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToAirbrushTerrainCV.setOnClickListener {
            val videoID = "YLUwSOPX99g"
            val action = TutorialAirbrushDirections.actionTutorialAirbrushToTutorialDetail(videoID)
            findNavController().navigate(action)
        }

    }
}