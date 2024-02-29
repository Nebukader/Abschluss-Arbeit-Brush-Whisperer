package com.example.brush_wisperer.ui.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.databinding.WorkshopWishlistItemBinding
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel

class WorkshopWishlistAdapter(

    private val wishlistColours : ArrayList<FirestoreColour>,
    private val viewModel: WorkshopViewModel

) : RecyclerView.Adapter<WorkshopWishlistAdapter.ItemViewHolder>() {


    class ItemViewHolder(val binding: WorkshopWishlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopWishlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = wishlistColours
        val binding = holder.binding

        val color = Color.parseColor(colourData[position].hexCode)
        val colorSateList = ColorStateList.valueOf(color)
        binding.colourShapeIV.imageTintList = colorSateList
        binding.colourNameTV.text = colourData[position].colourName
        binding.brandTV.text = colourData[position].brandName

        binding.removeBTN.setOnClickListener {
            viewModel.deleteFromWishlist(colourData[position].id)
            removeAt(position)

        }
    }
    fun removeAt(position: Int) {
        wishlistColours.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, wishlistColours.size)
    }

    override fun getItemCount() = wishlistColours.size
}