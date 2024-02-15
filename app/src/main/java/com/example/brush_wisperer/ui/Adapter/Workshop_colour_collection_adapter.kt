package com.example.brush_wisperer.ui.Adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.databinding.WorkshopMyCollectionColourItemBinding

class Workshop_colour_collection_adapter(

    private var colourCollection: List<ColourEntity>

) : RecyclerView.Adapter<Workshop_colour_collection_adapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ColourEntity>) {
        colourCollection = list
        notifyDataSetChanged()
    }

    class ItemViewHolder(val binding: WorkshopMyCollectionColourItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopMyCollectionColourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = colourCollection
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexcode)
        val colorSateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList = colorSateList
        binding.colourNameTV.text = colourData[position].colour_name
        binding.colourRangeTV.text = colourData[position].colour_range



    }
    fun onApplySearch(colourCollection: List<ColourEntity>){

        this.colourCollection = colourCollection
        notifyDataSetChanged()
    }

    override fun getItemCount() = colourCollection.size
}