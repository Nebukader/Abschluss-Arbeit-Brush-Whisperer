package com.example.brush_wisperer.ui.WorkshopFragment

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.Data.Model.ProjectsMiniature
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopProjectMiniaturesBinding
import com.example.brush_wisperer.databinding.WorkshopDialogAddMiniatureBinding
import com.example.brush_wisperer.databinding.WorkshopDialogChooseImageBinding
import com.example.brush_wisperer.databinding.WorkshopMiniaturePopupBinding
import com.example.brush_wisperer.ui.Adapter.ProjectsMiniatureAdapter
import com.example.brush_wisperer.ui.Adapter.WorkshopColourCollectionAdapter
import com.example.brush_wisperer.ui.Adapter.WorkshopMiniatureColoursAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkshopProjectMiniatures : Fragment() {

    private lateinit var binding: FragmentWorkshopProjectMiniaturesBinding
    private var selectedImage: Uri? = null
    private var photoUri: Uri? = null
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var singlePhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageTaken = false
    private val viewModel: WorkshopViewModel by activityViewModels()
    private lateinit var miniatureArrayList: ArrayList<ProjectsMiniature>
    private lateinit var miniColourAdapter: WorkshopMiniatureColoursAdapter
    private lateinit var adapter: ProjectsMiniatureAdapter
    private val safeArgs: WorkshopProjectMiniaturesArgs by navArgs()
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoUri = activity?.contentResolver?.let {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "project_image.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
            }
            it.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        }
        takePictureLauncher = registerForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success: Boolean ->
            if (success) {
                selectedImage = photoUri
                imageTaken = true
                viewModel.miniatureImage.value = selectedImage
            } else {
                Toast.makeText(context, "Failed to take picture", Toast.LENGTH_SHORT).show()
            }
        }
        singlePhotoLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                selectedImage = uri
                imageTaken = true
                viewModel.miniatureImage.value = selectedImage
            } else {
                Toast.makeText(context, "Failed to pick image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        miniatureArrayList = ArrayList()
        adapter = ProjectsMiniatureAdapter(miniatureArrayList, safeArgs.projectName, viewModel)
        binding = FragmentWorkshopProjectMiniaturesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMiniatures()
        binding.myMiniaturesRV.adapter = adapter

        //im viewmodel
        viewModel.projectName.value = safeArgs.projectName

        binding.titleTV.text = safeArgs.projectName
        binding.descritionTV.text = safeArgs.projectDesc
        viewModel.selectedMiniature.observe(viewLifecycleOwner) {
            if (it != null) {
                val dialogBuilder = AlertDialog.Builder(requireContext())
                val dialogBinding = WorkshopMiniaturePopupBinding.inflate(layoutInflater)
                val recyclerView = dialogBinding.recyclerView
                val miniName = it.miniName
                val miniColours = viewModel.miniColourArrayList
                val adapter = WorkshopMiniatureColoursAdapter(miniColours)
                viewModel.getMiniColours(safeArgs.projectName,miniName,adapter)
                adapter.notifyDataSetChanged()
                recyclerView.adapter = adapter

                dialogBinding.minatureTitleTV.text = it.miniName
                dialogBinding.miniatureImageIV.load(it.miniImage)


                dialogBinding.addColoursBTN.setOnClickListener {
                    val action =
                        WorkshopProjectMiniaturesDirections.actionWorkshopProjectMiniaturesToWorkshopPopupColourAdd()
                    findNavController().navigate(action)
                }
                dialogBuilder.setView(dialogBinding.root)
                val dialog = dialogBuilder.create()
                dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                dialog.show()
            }
        }
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val dialogBinding = WorkshopDialogAddMiniatureBinding.inflate(layoutInflater)
        val positiveButton = dialogBinding.addMiniatureBTN
        val miniImage = dialogBinding.miniImageIV
        val miniUnits =
            dialogBinding.numberPicker.apply { minValue = 1; maxValue = 120 }.value
        binding.addNewProjectBtn.setOnClickListener {

            viewModel.miniatureImage.observe(viewLifecycleOwner) { uri ->
                if (uri != null) {
                    miniImage.load(uri)
                } else {
                    miniImage.load(R.drawable.orc_workshop_gallery_bg)
                }
            }
            miniImage.setOnClickListener {
                val imageDialog = dialogBuilder.create()
                imageDialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
                val dialogBinding =
                    WorkshopDialogChooseImageBinding.inflate(LayoutInflater.from(view.context))
                val takePhoto = dialogBinding.cameraBtn
                val choosePhoto = dialogBinding.galleryBtn
                takePhoto.setOnClickListener {
                    takePictureLauncher.launch(photoUri)
                    imageDialog.dismiss()
                }
                choosePhoto.setOnClickListener {
                    singlePhotoLauncher.launch(PickVisualMediaRequest())
                    imageDialog.dismiss()
                }
                imageDialog.setView(dialogBinding.root)
                imageDialog.show()
            }
            dialogBuilder.setView(dialogBinding.root)
            val dialog = dialogBuilder.create()
            dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

            positiveButton.setOnClickListener {
                val miniName = dialogBinding.miniaturetNameET.editText?.text.toString()
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                val db = FirebaseFirestore.getInstance()
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val timestamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "JPEG_${timestamp}_"
                val totalMinis = dialogBinding.numberPicker.value
                val projectRef =
                    storageRef.child("${currentUser}/projects/${safeArgs.projectName}/${miniName}/${imageFileName}")
                val uploadTask = projectRef.putFile(selectedImage!!)
                uploadTask.addOnSuccessListener {
                    projectRef.downloadUrl.addOnSuccessListener { miniImage ->

                        val project = ProjectsMiniature(
                            miniName,
                            miniImage.toString(),
                            1,
                            totalMinis,
                        )
                        db.collection("users").document(currentUser!!).collection("projects")
                            .document(safeArgs.projectName).collection("miniatures")
                            .document(miniName).set(project)
                            .addOnSuccessListener { documentReference ->
                                Toast.makeText(
                                    view.context,
                                    "New Project added",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                dialog.dismiss()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    view.context,
                                    "Error adding new Project",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                    }
                }
            }
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            dialog.show() // Dialog sofort anzeigen
            viewModel.miniatureImage.value = null
        }
    }

    fun getMiniatures() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentUserId).collection("projects")
            .document(safeArgs.projectName).collection("miniatures")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    miniatureArrayList.clear()
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            miniatureArrayList.add(dc.document.toObject(ProjectsMiniature::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
            })
    }
}







