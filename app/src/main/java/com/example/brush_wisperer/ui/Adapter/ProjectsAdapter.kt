package com.example.brush_wisperer.ui.Adapter

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Model.Projects
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.WorkshopProjectItemBinding
import com.example.brush_wisperer.ui.MyWorkshopFragment.WorkshopViewModel
import com.example.brush_wisperer.ui.MyWorkshopFragment.Workshop_projectDirections

class ProjectsAdapter(
    private val projects: ArrayList<Projects>,
    private val viewModel : WorkshopViewModel,
    ) : RecyclerView.Adapter<ProjectsAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: WorkshopProjectItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = WorkshopProjectItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val userID = viewModel.firebaseCurrentUserID()
        val db = viewModel.currentUserDB()
        val docRef = db.collection("users").document(userID!!).collection("projects").document(projects[position].title)
        val binding = holder.binding
        val projectName = projects[position].title
        val projectDesc = projects[position].description
        binding.titleTV.text = projects[position].title
        binding.projectImageIV.load(projects[position].image)
        binding.projectCV.setOnClickListener {
            it.findNavController().navigate(Workshop_projectDirections.actionWorkshopNewProjectToWorkshopProjectMiniatures(projectName, projectDesc))
            Log.d("Desc", projectDesc)
        }
        binding.projectCV.setOnLongClickListener(View.OnLongClickListener {view ->
            binding.projectCV.setCardBackgroundColor(
                ContextCompat.getColor
                (view.context, android.R.color.system_error_light))

            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Are you sure you want to Delete the Miniature ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    viewModel.deleteProject(userID, projectName)
                    viewModel.deleteProjectStorage(userID, projectName, projects[position].image)
                    projects.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, projects.size)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.setOnDismissListener {
                binding.projectCV.setCardBackgroundColor(
                    ContextCompat.getColor
                    (view.context, R.color.md_theme_secondaryContainer))
            }
            alert.show()

            true
        })
        }



    override fun getItemCount() = projects.size
}
