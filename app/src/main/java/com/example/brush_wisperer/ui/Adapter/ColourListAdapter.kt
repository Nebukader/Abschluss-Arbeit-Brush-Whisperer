package com.example.brush_wisperer.ui.Adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.ColourItemBinding
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel
import com.google.firebase.auth.FirebaseAuth

class ColourListAdapter (

    private var colourList: List<ColourEntity>,
    private val viewModel: ColourViewModel

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

        binding.colourShapeIV.imageTintList = colorStateList
        binding.colourNameTV.text = colourData[position].colour_name
        val image = "http://dockerpi/back/${colourData[position].picture}"
        binding.colourItemIV.load(image)

        if (colourData[position].isFavorite) {
            binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_selected)
        } else {
            binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_not_selected)
        }

        val documentid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        binding.colourAddToCollectionBtn.setOnClickListener {
            if (colourData[position].isFavorite) {
                binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_selected)
                viewModel.updateFavourite(colourData[position].id, true)
                viewModel.saveColour(
                    documentid,
                    colourData[position].id.toString(),
                    colourData[position].brand_name,
                    colourData[position].colour_range,
                    colourData[position].colour_primary,
                    colourData[position].colour_name,
                    colourData[position].hexcode
                )
            } else {
                binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_not_selected)
                viewModel.updateFavourite(colourData[position].id, false)
                viewModel.deleteColour(
                    documentid,
                    colourData[position].id.toString()
                )
            }
        }

    }
    fun onApplySearch(colourList: List<ColourEntity>){

        this.colourList = colourList
        notifyDataSetChanged()
    }


    override fun getItemCount() = colourList.size
}