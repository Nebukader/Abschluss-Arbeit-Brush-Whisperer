package com.example.brush_wisperer.ui.Adapter

import android.app.AlertDialog
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.brush_wisperer.Data.Model.ProjectsMiniature
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.WorkshopMiniatureItemBinding
import com.example.brush_wisperer.ui.MyWorkshopFragment.WorkshopViewModel


class ProjectsMiniatureAdapter(
    private val projectsMiniature: ArrayList<ProjectsMiniature>,
    private val projectName: String,
    private val viewModel: WorkshopViewModel

) : RecyclerView.Adapter<ProjectsMiniatureAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: WorkshopMiniatureItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            WorkshopMiniatureItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val binding = holder.binding
        val userID = viewModel.firebaseCurrentUserID()
        val db = viewModel.currentUserDB()
        val docRef = db.collection("users").document(userID!!).collection("projects")
            .document(projectName).collection("miniatures")
            .document(projectsMiniature[position].miniName)
        binding.minatureTitleTV.text = projectsMiniature[position].miniName
        binding.miniatureImageIV.load(projectsMiniature[position].miniImage)
        binding.counterTV.text = projectsMiniature[position].currentAmmount.toString()
        binding.counterTotalTV.text = projectsMiniature[position].miniAmmount.toString()

        binding.counterPlusBTN.setOnClickListener {
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val currentAmmount = document.data?.get("currentAmmount").toString().toInt()
                        val newAmmount = currentAmmount + 1
                        docRef.update("currentAmmount", newAmmount)
                            .addOnSuccessListener {
                                binding.counterTV.text = newAmmount.toString()
                                binding.progressBar.progress = newAmmount
                                Log.d("Adapter","DocumentSnapshot successfully updated!")
                            }
                            .addOnFailureListener { e ->
                                Log.d("Adapter","Error updating document")
                            }
                    }
                }
        }
        binding.progressBar.progress = projectsMiniature[position].currentAmmount
        binding.progressBar.max = projectsMiniature[position].miniAmmount

        //region Delete Miniature
        //Löschen einer Miniatur aus dem Projekt
        binding.miniatureCV.setOnLongClickListener { view ->
            binding.miniatureCV.setCardBackgroundColor(ContextCompat.getColor
                (view.context, android.R.color.system_error_light
            ))
            
            val builder = AlertDialog.Builder(view.context)
            builder.setMessage("Are you sure you want to Delete the Miniature ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    viewModel.deleteMiniature(userID, projectName, projectsMiniature[position].miniName)
                    viewModel.deleteMiniatureStorage(userID, projectName, projectsMiniature[position].miniName, projectsMiniature[position].miniImage)
                    projectsMiniature.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, projectsMiniature.size)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.setOnDismissListener {
                binding.miniatureCV.setCardBackgroundColor(ContextCompat.getColor
                    (view.context, R.color.md_theme_secondaryContainer))
            }
                alert.show()

            true
        }
        //endregion

    }
    override fun getItemCount() = projectsMiniature.size
}