package com.example.brush_wisperer.ui.TutorialFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentTutorialSpeedPaintBinding

class TutorialSpeedPaint : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tutorial_speed_paint, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentTutorialSpeedPaintBinding.bind(view)

        binding.howToStartSpeedPaintCV.setOnClickListener {
            val videoID = "ea3CKZyhDRE"
            val action = TutorialSpeedPaintDirections.actionTutorialSpeedPaintToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToSpeedPaintSkinCV.setOnClickListener {
            val videoID = "FvZEtSVsiN0"
            val action = TutorialSpeedPaintDirections.actionTutorialSpeedPaintToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToPaintNecronsCV.setOnClickListener {
            val videoID = "rifXzXTqoUA"
            val action = TutorialSpeedPaintDirections.actionTutorialSpeedPaintToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
        binding.howToSpeedPaintTippsCV.setOnClickListener {
            val videoID = "GzPS9kTo16o"
            val action = TutorialSpeedPaintDirections.actionTutorialSpeedPaintToTutorialDetail(videoID)
            findNavController().navigate(action)
        }
    }
}