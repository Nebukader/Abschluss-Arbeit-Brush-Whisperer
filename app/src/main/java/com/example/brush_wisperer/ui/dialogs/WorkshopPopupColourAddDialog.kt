package com.example.brush_wisperer.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import coil.load
import com.example.brush_wisperer.Data.Model.ProjectsMiniature
import com.example.brush_wisperer.databinding.WorkshopMiniaturePopupBinding


class WorkshopPopupColourAddDialog : DialogFragment() {

    private lateinit var dialogBinding: WorkshopMiniaturePopupBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            dialogBinding = WorkshopMiniaturePopupBinding.inflate(LayoutInflater.from(it))
            builder.setView(dialogBinding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}