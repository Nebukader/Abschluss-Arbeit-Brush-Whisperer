package com.example.brush_wisperer.ui.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.ColourItemBinding
import com.example.brush_wisperer.databinding.WorkshopPopupColourlistAddItemBinding
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel
import com.google.firebase.auth.FirebaseAuth

class WorkshopColourListAdapter(
    private val viewModel: WorkshopViewModel
) : ListAdapter<ColourEntity, WorkshopColourListAdapter.ItemViewHolder>(ColourDiffCallBack()) {

        class ItemViewHolder(val binding: WorkshopPopupColourlistAddItemBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val binding = WorkshopPopupColourlistAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return ItemViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            val binding = holder.binding
            val colourData = Fir

            val color = Color.parseColor(colourData.hexcode)
            val colorStateList = ColorStateList.valueOf(color)

            binding.colourShapeIV.imageTintList = colorStateList
            binding.colourNameTV.text =


            if (colourData.isFavorite) {
                binding.colourAddToMiniBTN.setImageResource(R.drawable.favorit_selected)
            } else {
                binding.colourAddToMiniBTN.setImageResource(R.drawable.favorit_not_selected)
            }

            val currentUser = viewModel.firebaseCurrentUserID()
            val documentid = currentUser.toString()

            binding.colourAddToMiniBTN.setOnClickListener {
                if (!colourData.isFavorite) {
                    binding.colourAddToMiniBTN.setImageResource(R.drawable.favorit_selected)
                    viewModel.updateFavourite(colourData.id, true)
                    viewModel.saveColour(
                        documentid,
                        colourData.id.toString(),
                        colourData.brand_name,
                        colourData.colour_range,
                        colourData.colour_primary,
                        colourData.colour_name,
                        colourData.hexcode
                    )
                } else {
                    binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_not_selected)
                    viewModel.updateFavourite(colourData.id, false)
                    viewModel.deleteColour(
                        documentid,
                        colourData.id.toString()
                    )
                }
            }

        }

    fun onApplySearch(colourList: List<ColourEntity>) {
        submitList(colourList)
    }
}

// Only one declaration of ColourDiffCallback class
class ColourDiffCallBack : DiffUtil.ItemCallback<ColourEntity>() {
    override fun areItemsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
        return oldItem == newItem
    }
}