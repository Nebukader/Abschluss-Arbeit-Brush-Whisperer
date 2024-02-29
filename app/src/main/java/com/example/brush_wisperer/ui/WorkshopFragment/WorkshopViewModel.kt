package com.example.brush_wisperer.ui.WorkshopFragment

import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import android.widget.Adapter
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabaseInstance.getDatabase
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.Data.Model.ProjectsMiniature
import com.example.brush_wisperer.Data.Remote.ColourApi
import com.example.brush_wisperer.Data.RepositoryColours
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.ui.Adapter.WorkshopMiniatureColoursAdapter
import com.example.brush_wisperer.ui.Adapter.WorkshopWishlistAdapter
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


class WorkshopViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val repositoryColours: RepositoryColours = RepositoryColours(database, ColourApi)
    private val repository = RepositoryFirebase()

    val colourList = repositoryColours.colourList
    val imageUri: MutableLiveData<Uri> = MutableLiveData<Uri>()

    val miniatureImage: MutableLiveData<Uri> = MutableLiveData<Uri>()

    val projectName: MutableLiveData<String> = MutableLiveData()

    val miniColourArrayList: ArrayList<FirestoreColour> = ArrayList()

    val wishlistArrayList: ArrayList<FirestoreColour> = ArrayList()

    var wishlistListener : ListenerRegistration? = null

    fun selectProjectName(projectName: String) {
        this.projectName.value = projectName
    }


    //Live Data für das Popup
    val selectedMiniature: MutableLiveData<ProjectsMiniature> = MutableLiveData()

    //Live Data Aktuallisierung der ausgewählten Miniatur
    fun selectMiniature(miniature: ProjectsMiniature) {
        selectedMiniature.value = miniature
    }

    fun firebaseCurrentUserID(): String? {
        return repository.getCurrentUserID()
    }

    fun currentUserDB(): FirebaseFirestore {
        return repository.getDBInstance()
    }

    fun deleteMiniature(userID: String, projectName: String, miniName: String) {
        viewModelScope.launch {
            val db = currentUserDB()
            db.collection("users").document(userID).collection("projects")
                .document(projectName).collection("miniatures")
                .document(miniName)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot sucessfully deleted")
                }
                .addOnFailureListener() { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }
    }

    fun deleteMiniatureStorage(
        userID: String,
        projectName: String,
        miniName: String,
        imageRef: String
    ) {
        viewModelScope.launch {
            val storageRef = repository.getStorageRef()
            val image = storageRef.getReferenceFromUrl(imageRef).toString()
            val imageString = image.substringAfterLast("/")
            Log.d("StorageRef", imageString)
            val imageReference =
                storageRef.reference.child("$userID/projects/$projectName/$miniName/$imageString")
            imageReference.delete().addOnSuccessListener {
                Log.d(ContentValues.TAG, "Image deleted")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "Error deleting image")
            }
        }
    }

    fun deleteProject(userID: String, projectName: String) {
        viewModelScope.launch {
            val db = currentUserDB()
            db.collection("users").document(userID).collection("projects")
                .document(projectName)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot sucessfully deleted")
                }
                .addOnFailureListener() { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }
    }

    fun deleteProjectStorage(userID: String, projectName: String, imageRef: String) {
        viewModelScope.launch {
            val storageRef = repository.getStorageRef()
            val image = storageRef.getReferenceFromUrl(imageRef).toString()
            val imageString = image.substringAfterLast("/")
            Log.d("StorageRef", imageString)
            val imageReference =
                storageRef.reference.child("$userID/projects/$projectName/$imageString")
            imageReference.delete().addOnSuccessListener {
                Log.d(ContentValues.TAG, "Image deleted")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "Error deleting image")
            }
        }
    }

    fun saveMiniColours(
        userID: String, projectName: String, miniName: String, colourId: String, brandName: String,
        colourRange: String, colourPrimary: String, colourName: String, hexCode: String
    ) {
        viewModelScope.launch {
            val db = Firebase.firestore
            val colour = FirestoreColour(
                colourId,
                brandName = brandName,
                colourRange = colourRange,
                colourPrimary = colourPrimary,
                colourName = colourName,
                hexCode = hexCode
            )
            db.collection("users").document(userID).collection("projects")
                .document(projectName).collection("miniatures")
                .document(miniName).collection("colours").document(colourId)
                .set(colour)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot sucessfully written")
                }
                .addOnFailureListener() { e ->
                    Log.w(ContentValues.TAG, "Error adding document", e)
                }
        }
    }

    fun deleteColour(userID: String, projectName: String, miniName: String, colourId: String) {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("users").document(userID).collection("projects")
                .document(projectName).collection("miniatures")
                .document(miniName).collection("colours").document(colourId)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot sucessfully deleted")
                }
                .addOnFailureListener() { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }
    }
    fun getMiniColours(projectName: String, miniName: String, adapter :WorkshopMiniatureColoursAdapter) {
        val currentUserId = firebaseCurrentUserID()
        val db = currentUserDB()
        db.collection("users").document(currentUserId!!).collection("projects").document(projectName)
            .collection("miniatures").document(miniName).collection("colours").addSnapshotListener(object :
                EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        return
                    }
                    miniColourArrayList.clear()
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            miniColourArrayList.add(dc.document.toObject(FirestoreColour::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("Data Check", "ArrayList content: ${miniColourArrayList.toString()}")
                }
            })
    }
    fun getWishlistColours(adapter : WorkshopWishlistAdapter) {
        val currentUserId = firebaseCurrentUserID()
        val db = currentUserDB()
        wishlistListener = db.collection("users").document(currentUserId!!).collection("wishlist").addSnapshotListener(object :
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
                            wishlistArrayList.add(dc.document.toObject(FirestoreColour::class.java))
                        }else if (dc.type == DocumentChange.Type.REMOVED) {
                            wishlistArrayList.remove(dc.document.toObject(FirestoreColour::class.java))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("Data Check", "ArrayList content: ${wishlistArrayList.toString()}")
                }
            })
    }
    fun removeWishlistListener() {
        wishlistListener?.remove()
        wishlistArrayList.clear()
    }
    fun deleteFromWishlist(colourId: String) {
        viewModelScope.launch {
            val userId = firebaseCurrentUserID()
            val db = currentUserDB()
            db.collection("users").document(userId!!).collection("wishlist").document(colourId)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "DocumentSnapshot sucessfully deleted")
                }
                .addOnFailureListener() { e ->
                    Log.w(ContentValues.TAG, "Error deleting document", e)
                }
        }
    }
}
