package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.app.AlertDialog
import android.content.ContentValues
import android.graphics.Color
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
import coil.load
import com.example.brush_wisperer.Data.Model.Projects
import com.example.brush_wisperer.R
import com.example.brush_wisperer.databinding.FragmentWorkshopNewProjectBinding
import com.example.brush_wisperer.databinding.WorkshopDialogChooseImageBinding
import com.example.brush_wisperer.databinding.WorkshopNewProjectDialogBinding
import com.example.brush_wisperer.ui.Adapter.ProjectsAdapter
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

class Workshop_project : Fragment() {

    private lateinit var binding: FragmentWorkshopNewProjectBinding
    private var selectedImage: Uri? = null
    private var photoUri: Uri? = null
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var singlePhotoLauncher: ActivityResultLauncher<PickVisualMediaRequest>
    private var imageTaken = false
    private val viewModel: WorkshopViewModel by activityViewModels()
    private lateinit var projectsArrayList: ArrayList<Projects>
    private lateinit var adapter: ProjectsAdapter



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
                viewModel.imageUri.value = selectedImage
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
                viewModel.imageUri.value = selectedImage
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
        binding = FragmentWorkshopNewProjectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        projectsArrayList = ArrayList<Projects>()
        adapter = ProjectsAdapter(projectsArrayList, viewModel)
        getProjects()

        //region: Add new project button

        binding.addNewProjectBtn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(view.context)
            val dialogBinding =
                WorkshopNewProjectDialogBinding.inflate(LayoutInflater.from(view.context))
            val positiveButton = dialogBinding.createBtn
            val totalMiniatures = dialogBinding.numberPicker.apply {
                minValue = 1
                maxValue = 120
            }.value
            val photo = dialogBinding.addPhotoIv

            viewModel.imageUri.observe(viewLifecycleOwner) { uri ->
                if (uri != null) {
                    photo.load(uri)
                }
                else {
                    photo.load(R.drawable.orc_workshop_gallery_bg)
                }
            }

            photo.setOnClickListener {
                val imageDialog = dialogBuilder.create()
                imageDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            positiveButton.setOnClickListener {
                val projectName = dialogBinding.projectNameET.editText?.text.toString()
                val description = dialogBinding.projectDescriptionET.editText?.text.toString()
                val currentUser = FirebaseAuth.getInstance().currentUser?.uid
                val db = FirebaseFirestore.getInstance()
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val timestamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFileName = "JPEG_${timestamp}_"
                val projectRef =
                    storageRef.child("${currentUser}/projects/${projectName}/${imageFileName}")
                val uploadTask = projectRef.putFile(selectedImage!!)
                uploadTask.addOnSuccessListener {
                    projectRef.downloadUrl.addOnSuccessListener { uri ->

                        val project = Projects(
                            projectName,
                            description,
                            totalMiniatures,
                            uri.toString()
                        )
                        db.collection("users").document(currentUser!!).collection("projects").document(projectName)
                            .set(project)
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
            dialog.show() // Dialog sofort anzeigen
            viewModel.imageUri.value = null
        }
        //endregion
    }

    private fun getProjects() {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(currentUserId).collection("projects")
            .addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    for (dc: DocumentChange in value?.documentChanges!!) {

                        if (dc.type == DocumentChange.Type.ADDED) {
                            projectsArrayList.add(dc.document.toObject(Projects::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    if (projectsArrayList.isNotEmpty()) {
                        binding.myProjectCollectionRV.adapter = adapter
                    }
                }
            })
    }
}