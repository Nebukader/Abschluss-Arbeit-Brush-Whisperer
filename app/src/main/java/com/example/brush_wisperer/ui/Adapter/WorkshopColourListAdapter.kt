package com.example.brush_wisperer.ui.Adapter

import android.content.ContentValues.TAG
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.WorkshopPopupColourlistAddItemBinding
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel
import com.google.firebase.auth.FirebaseAuth

class WorkshopColourListAdapter(
    private val viewModel: WorkshopViewModel
) : ListAdapter<ColourEntity, WorkshopColourListAdapter.ItemViewHolder>(WorkshopColourDiffCallback()) {

    class ItemViewHolder(val binding: WorkshopPopupColourlistAddItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopPopupColourlistAddItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val colourData = getItem(position)
        val binding = holder.binding

        val color = Color.parseColor(colourData.hexcode)
        val colorStateList = ColorStateList.valueOf(color)

        binding.colourShapeIV.imageTintList = colorStateList
        binding.colourNameTV.text = colourData.colour_name

        if (colourData.isFavorite) {
            binding.colourAddToMiniBTN.setImageResource(R.drawable.checkmark_icon)
        } else {
            binding.colourAddToMiniBTN.setImageResource(R.drawable.add_icon)
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        val documentid = currentUser?.uid.toString()
        Log.d("TAG", "Adapter: ${currentUser?.uid}")
        val projectName = viewModel.projectName.value.toString()
        val miniatureName = viewModel.selectedMiniature.value?.miniName.toString()
        // Firestore-Daten abrufen
        val db = viewModel.currentUserDB()
        val docRef = db.collection("users").document(documentid).collection("projects")
            .document(projectName).collection("miniatures")
            .document(miniatureName).collection("colours").document(colourData.id.toString())

        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                binding.colourAddToMiniBTN.setImageResource(R.drawable.checkmark_icon)
            } else {
                binding.colourAddToMiniBTN.setImageResource(R.drawable.add_icon)
            }
            binding.colourAddToMiniBTN.setOnClickListener{
                if (snapshot != null && snapshot.exists()) {
                    viewModel.deleteColour(documentid, projectName, miniatureName, colourData.id.toString())
                } else {
                    viewModel.saveMiniColours(
                        documentid, projectName,
                        miniatureName, colourData.id.toString(),
                        colourData.brand_name, colourData.colour_range,
                        colourData.colour_primary, colourData.colour_name,
                        colourData.hexcode)
                }
            }
        }


        fun onApplySearch(colourList: List<ColourEntity>) {
            submitList(colourList)
        }
    }

    class WorkshopColourDiffCallback : DiffUtil.ItemCallback<ColourEntity>() {
        override fun areItemsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ColourEntity, newItem: ColourEntity): Boolean {
            return oldItem == newItem
        }
    }
}