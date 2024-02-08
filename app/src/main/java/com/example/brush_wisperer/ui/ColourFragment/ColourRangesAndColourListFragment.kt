package com.example.brush_wisperer.ui.ColourFragment

import android.graphics.BitmapFactory
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentColourRangesAndColourListBinding
import com.example.brush_wisperer.ui.Adapter.ColourListAdapter


class ColourRangesAndColourListFragment : Fragment() {

    private val viewModel: ColourViewModel by viewModels()
    private lateinit var binding: FragmentColourRangesAndColourListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentColourRangesAndColourListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ColourListAdapter(emptyList())
        val recyclerView = binding.colourRangesAndColourListRV

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_purple_dust)
        val drawable = BitmapDrawable(resources, bitmap)
        drawable.gravity = Gravity.CENTER
        drawable.setTileModeXY(Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        binding.root.background = drawable


        recyclerView.adapter = adapter


        viewModel.loading.observe(viewLifecycleOwner) {
            when (it) {
                ApiStatus.LOADING -> binding.progressBar.visibility = View.VISIBLE
                ApiStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.noDataIV.visibility = View.VISIBLE
                }

                else -> {
                    binding.progressBar.visibility = View.GONE
                    binding.noDataIV.visibility = View.GONE
                }
            }
        }
        viewModel.colourList.observe(viewLifecycleOwner) {
            adapter.submitList(it)

            //Starten der Animation beim ersten Laden
            val context = binding.colourRangesAndColourListRV.context
            val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

            binding.colourRangesAndColourListRV.layoutAnimation = controller
            binding.colourRangesAndColourListRV.scheduleLayoutAnimation()
        }
    }

}

