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
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel
import com.google.firebase.auth.FirebaseAuth

class ColourListAdapter(
    private val viewModel: ColourViewModel
) : ListAdapter<ColourEntity, ColourListAdapter.ItemViewHolder>(ColourDiffCallback()) {

    class ItemViewHolder(val binding: ColourItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ColourItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = getItem(position)
        val binding = holder.binding

        val color = Color.parseColor(colourData.hexcode)
        val colorStateList = ColorStateList.valueOf(color)

        binding.colourShapeIV.imageTintList = colorStateList
        binding.colourNameTV.text = colourData.colour_name
        val image = "http://brushwhisperer.ddns.net:9580/back/${colourData.picture}"
        binding.colourItemIV.load(image)

        if (colourData.isFavorite) {
            binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_selected)
        } else {
            binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_not_selected)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val documentid = currentUser?.uid.toString()
        Log.d("TAG", "Adapter: ${currentUser?.uid}")

        binding.colourAddToCollectionBtn.setOnClickListener {
            if (!colourData.isFavorite) {
                binding.colourAddToCollectionBtn.setImageResource(R.drawable.favorit_selected)
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
        binding.wishlistBTN.setOnClickListener{
            viewModel.saveWishlist(
                colourData.id.toString(),
                colourData.brand_name,
                colourData.colour_range,
                colourData.colour_primary,
                colourData.colour_name,
                colourData.hexcode
            )

        }

    }

    fun onApplySearch(colourList: List<ColourEntity>) {
        submitList(colourList)
    }
}

class ColourDiffCallback : DiffUtil.ItemCallback<ColourEntity>() {
    override fun areItemsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
        return oldItem == newItem
    }
}