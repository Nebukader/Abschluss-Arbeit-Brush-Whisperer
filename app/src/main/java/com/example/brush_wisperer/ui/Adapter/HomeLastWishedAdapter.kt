package com.example.brush_wisperer.ui.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.databinding.HomeLastWishedItemBinding
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel

class HomeLastWishedAdapter(

    private val wishlistColours : List<FirestoreColour>,
    ) : RecyclerView.Adapter<HomeLastWishedAdapter.ItemViewHolder>() {


    class ItemViewHolder(val binding: HomeLastWishedItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = HomeLastWishedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = wishlistColours
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexCode)
        val colorSateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList = colorSateList
    }
    override fun getItemCount() = wishlistColours.size
}