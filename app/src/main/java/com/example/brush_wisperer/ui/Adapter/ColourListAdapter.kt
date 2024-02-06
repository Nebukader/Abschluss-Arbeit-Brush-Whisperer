package com.example.brush_wisperer.ui.Adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.databinding.ColourItemBinding

class ColourListAdapter (
    private var colourList: List<ColourEntity>

) : RecyclerView.Adapter<ColourListAdapter.ItemViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<ColourEntity>) {
        colourList = list
        notifyDataSetChanged()
    }
    class ItemViewHolder(val binding: ColourItemBinding) :
    RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ColourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = colourList
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexcode)
        val colorStateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList =colorStateList
        binding.colourNameTV.text = colourData[position].colour_name

    }

    override fun getItemCount() = colourList.size
}