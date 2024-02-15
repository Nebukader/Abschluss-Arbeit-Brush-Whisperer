package com.example.brush_wisperer.ui.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Model.firestoreColour
import com.example.brush_wisperer.databinding.WorkshopMyCollectionColourItemBinding

class Workshop_colour_collection_adapter(

    private val favoritColourList : ArrayList<firestoreColour>

) : RecyclerView.Adapter<Workshop_colour_collection_adapter.ItemViewHolder>() {


    class ItemViewHolder(val binding: WorkshopMyCollectionColourItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopMyCollectionColourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = favoritColourList
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexCode)
        val colorSateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList = colorSateList
        binding.colourNameTV.text = colourData[position].colourName
        binding.colourRangeTV.text = colourData[position].colourRange



    }

    override fun getItemCount() = favoritColourList.size
}