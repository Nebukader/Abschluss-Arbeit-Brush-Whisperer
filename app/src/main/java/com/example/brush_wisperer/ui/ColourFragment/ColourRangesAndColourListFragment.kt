package com.example.brush_wisperer.ui.ColourFragment

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentColourRangesAndColourListBinding
import com.example.brush_wisperer.ui.Adapter.ColourListAdapter
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.*
import androidx.lifecycle.Observer
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Model.ColourList
import java.util.Locale


class ColourRangesAndColourListFragment : Fragment() {

    private val colourItemList = ArrayList<ColourList>()

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

        val adapter = ColourListAdapter(emptyList(), viewModel)
        val recyclerView = binding.colourRangesAndColourListRV

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

            val searchView = binding.searchBar

            searchView.setOnQueryTextListener(object : OnQueryTextListener {
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
                        if (searchList.isEmpty()){
                            Toast.makeText(context, "No Colour found", Toast.LENGTH_SHORT).show()
                        }else{
                            adapter.onApplySearch(searchList)
                        }
                    }
                    return true
                }
            })
        }

    }

}

