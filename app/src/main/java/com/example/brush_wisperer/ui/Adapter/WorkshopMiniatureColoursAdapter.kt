package com.example.brush_wisperer.ui.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.databinding.WorkshopMiniaturePopupColourItemBinding

class WorkshopMiniatureColoursAdapter(
    private val miniColourList : ArrayList<FirestoreColour>
) : RecyclerView.Adapter<WorkshopMiniatureColoursAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: WorkshopMiniaturePopupColourItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopMiniaturePopupColourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        val colourData = miniColourList
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexCode)
        val colorSateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList = colorSateList
        binding.colourNameTV.text = colourData[position].colourName
    }

    override fun getItemCount() = miniColourList.size
}