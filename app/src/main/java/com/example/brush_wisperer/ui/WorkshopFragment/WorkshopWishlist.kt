package com.example.brush_wisperer.ui.WorkshopFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.brush_wisperer.databinding.FragmentWorkshopWishlistBinding
import com.example.brush_wisperer.ui.Adapter.WorkshopWishlistAdapter
import com.google.firebase.firestore.ListenerRegistration

class WorkshopWishlist : Fragment() {

    private lateinit var binding: FragmentWorkshopWishlistBinding
    private val viewModel: WorkshopViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWorkshopWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val wishlistData = viewModel.wishlistArrayList
        val recyclerView = binding.workshopColourListRV
        val adapter = WorkshopWishlistAdapter(wishlistData, viewModel)
        viewModel.getWishlistColours(adapter)
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter

    }

    override fun onDestroyView() {
        Log.d("WorkshopWishlist", "onDestroyView called")
        super.onDestroyView()
        viewModel.removeWishlistListener()
        Log.d("WorkshopWishlist", viewModel.wishlistListener.toString())
    }
}
