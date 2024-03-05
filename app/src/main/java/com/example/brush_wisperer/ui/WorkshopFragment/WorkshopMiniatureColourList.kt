package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopPopupColourAddBinding
import com.example.brush_wisperer.ui.Adapter.WorkshopColourListAdapter
import com.example.brush_wisperer.ui.ColourFragment.ApiStatus
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel
import java.util.Locale

class WorkshopMiniatureColourList : Fragment() {
    private var isDataLoaded = false
    private lateinit var binding: FragmentWorkshopPopupColourAddBinding
    private val viewModel: WorkshopViewModel by activityViewModels()
    private lateinit var colourViewModel: ColourViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWorkshopPopupColourAddBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        colourViewModel = ColourViewModel(application = requireActivity().application)
        val adapter = WorkshopColourListAdapter(viewModel)
        val recyclerView = binding.colourRangesAndColourListRV
        recyclerView.adapter = adapter

        colourViewModel.loading.observe(viewLifecycleOwner) {
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
        colourViewModel.colourList.observe(viewLifecycleOwner) {
            if (!isDataLoaded) {
                adapter.submitList(it)
                isDataLoaded = true
            }
        }
        val context = binding.colourRangesAndColourListRV.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        binding.colourRangesAndColourListRV.layoutAnimation = controller
        binding.colourRangesAndColourListRV.scheduleLayoutAnimation()

        val searchView = binding.searchBar

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchList = ArrayList<ColourEntity>()

                if (newText != null) {
                    for (item in viewModel.colourList.value!!) {
                        val lowerCaseNewText = newText.lowercase(Locale.ROOT)
                        if (item.colour_name.lowercase(Locale.ROOT).contains(lowerCaseNewText)) {
                            searchList.add(item)
                        }
                    }
                    if (searchList.isEmpty()) {
                        Toast.makeText(context, "No Colour found", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.submitList(searchList)
                    }
                }
                return true
            }
        })


    }
}

