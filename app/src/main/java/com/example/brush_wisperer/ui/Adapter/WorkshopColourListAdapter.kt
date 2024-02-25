package com.example.brush_wisperer.ui.Adapter

import android.app.AlertDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Model.Projects
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.ColourItemBinding
import com.example.brush_wisperer.databinding.WorkshopPopupColourlistAddItemBinding
import com.example.brush_wisperer.databinding.WorkshopProjectItemBinding
import com.example.brush_wisperer.ui.ColourFragment.ColourViewModel
import com.example.brush_wisperer.ui.WorkshopFragment.WorkshopViewModel
import com.example.brush_wisperer.ui.WorkshopFragment.Workshop_projectDirections
import com.google.firebase.auth.FirebaseAuth

class WorkshopColourListAdapter(
    private val miniatureColour: ArrayList<ColourEntity>,
    private val viewModel : WorkshopViewModel,
) : RecyclerView.Adapter<WorkshopColourListAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: WorkshopPopupColourlistAddItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopPopupColourlistAddItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val userID = viewModel.firebaseCurrentUserID()
        val db = viewModel.currentUserDB()
        // Richtigen Pfad setzten
        val docRef = db.collection("users").document(userID!!).collection("projects").document(miniatureColour[position].colour_name)
        val binding = holder.binding
        val colourName = miniatureColour[position].colour_name
        val colourHex = miniatureColour[position].hexcode
        binding.colourNameTV.text = miniatureColour[position].colour_name
        binding.colourShapeIV

    }



    override fun getItemCount() = miniatureColour.size
}

