package com.example.brush_wisperer.ui.ColourFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentColourRangesAndColourListBinding
import com.example.brush_wisperer.ui.Adapter.ColourListAdapter
import java.util.Locale


class ColourRangesAndColourListFragment : Fragment() {

    private var isDataLoaded = false

    private val viewModel: ColourViewModel by activityViewModels()
    private lateinit var binding: FragmentColourRangesAndColourListBinding
    private val safeargs : ColourRangesAndColourListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentColourRangesAndColourListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ColourListAdapter(viewModel)
        val recyclerView = binding.colourRangesAndColourListRV
        viewModel.filteredList(safeargs.brand)


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

        viewModel.filteredColourList.observe(viewLifecycleOwner) { colourList ->
            if (colourList.isNotEmpty() && !isDataLoaded) {
                adapter.submitList(colourList)
                Log.d("Filter", "FilterFragmentList = $colourList.toString()")
                isDataLoaded = true
            }
        }

        //Start the animation when the first load
        val context = binding.colourRangesAndColourListRV.context
        val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation)

        binding.colourRangesAndColourListRV.layoutAnimation = controller
        binding.colourRangesAndColourListRV.scheduleLayoutAnimation()

        val searchView = binding.searchBar

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val searchList = ArrayList<ColourEntity>()

                if (newText != null) {
                    for (item in viewModel.filteredColourList.value!!) {
                        val lowerCaseNewText = newText.lowercase(Locale.ROOT)
                        if (item.colour_name.lowercase(Locale.ROOT).contains(lowerCaseNewText)) {
                            searchList.add(item)
                        }
                    }
                    if (searchList.isEmpty()) {
                        Toast.makeText(context, "No Colour found", Toast.LENGTH_SHORT).show()
                    } else {
                        adapter.onApplySearch(searchList)
                    }
                }
                return true
            }
        })
    }

}



