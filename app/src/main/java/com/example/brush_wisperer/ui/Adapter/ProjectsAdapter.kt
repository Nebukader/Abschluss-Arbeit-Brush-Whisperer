package com.example.brush_wisperer.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Model.Projects
import com.example.brush_wisperer.databinding.WorkshopProjectItemBinding
import com.example.brush_wisperer.ui.MyWorkshopFragment.Workshop_projectDirections

class ProjectsAdapter(
    private val projects: ArrayList<Projects>,
    ) : RecyclerView.Adapter<ProjectsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: WorkshopProjectItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val binding = holder.binding
        val projectName = projects[position].title
        binding.titleTV.text = projects[position].title
        binding.projectImageIV.load(projects[position].image)
        binding.projectCardView.setOnClickListener {
            it.findNavController().navigate(Workshop_projectDirections.actionWorkshopNewProjectToWorkshopProjectMiniatures(projectName))
        }
    }


    override fun getItemCount() = projects.size
}
