package com.example.brush_wisperer.ui.ColourFragment

import android.os.Bundle
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
import com.google.android.material.tabs.TabLayout
import java.util.Locale


class ColourRangesAndColourListFragment : Fragment() {

    private var isDataLoaded = false

    private val viewModel: ColourViewModel by activityViewModels()
    private lateinit var binding: FragmentColourRangesAndColourListBinding
    private val safeargs: ColourRangesAndColourListFragmentArgs by navArgs()

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

        if (safeargs.brand != "The Army Painter") {
            binding.tabLayout.visibility = View.GONE
        }

        val adapter = ColourListAdapter(viewModel)
        val recyclerView = binding.colourRangesAndColourListRV
        viewModel.filteredList(safeargs.brand, safeargs.colourRange)
        viewModel.filteredColourList.observe(viewLifecycleOwner) { colourList ->
            if (colourList.isNotEmpty()) {
                adapter.submitList(colourList)
                isDataLoaded = true
            }
        }

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    binding.tabLayout.getTabAt(0)?.position -> viewModel.filteredList(
                        safeargs.brand,
                        "Speed Paint"
                    )

                    binding.tabLayout.getTabAt(1)?.position -> viewModel.filteredList(
                        safeargs.brand,
                        "Warpaints Fanatic"
                    )

                    binding.tabLayout.getTabAt(2)?.position -> viewModel.filteredList(
                        safeargs.brand,
                        "Warpaints Air"
                    )

                    binding.tabLayout.getTabAt(3)?.position -> viewModel.filteredList(
                        safeargs.brand,
                        "Primer"
                    )

                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

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
                        if (item.colour_name.lowercase(Locale.ROOT)
                                .contains(lowerCaseNewText)
                        ) {
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

