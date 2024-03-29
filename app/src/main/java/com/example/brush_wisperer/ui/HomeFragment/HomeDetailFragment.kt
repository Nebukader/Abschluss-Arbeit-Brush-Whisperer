package com.example.brush_wisperer.ui.HomeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentHomeDetailBinding

class HomeDetailFragment : Fragment() {
    private val args: HomeDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_detail, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentHomeDetailBinding.bind(view)
        binding.webView.loadUrl(args.postLink)
    }

}