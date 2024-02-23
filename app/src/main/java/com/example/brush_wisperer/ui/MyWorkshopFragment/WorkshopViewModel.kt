package com.example.brush_wisperer.ui.MyWorkshopFragment

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch


class WorkshopViewModel : ViewModel() {
    private val repository = RepositoryFirebase()

    val imageUri : MutableLiveData<Uri> = MutableLiveData<Uri>()

    val miniatureImage : MutableLiveData<Uri> = MutableLiveData<Uri>()

    fun firebaseCurrentUserID():String? {
        return repository.getCurrentUserID()
    }
    fun currentUserDB() : FirebaseFirestore {
        return repository.getDBInstance()
    }
    fun deleteMiniature(userID : String ,projectName: String, miniName: String) {
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
    fun deleteMiniatureStorage(userID: String, projectName: String, miniName: String, imageRef: String) {
        viewModelScope.launch {
            val storageRef = repository.getStorageRef()
            val image = storageRef.getReferenceFromUrl(imageRef).toString()
            val imageString = image.substringAfterLast("/")
            Log.d("StorageRef", imageString)
            val imageReference = storageRef.reference.child("$userID/projects/$projectName/$miniName/$imageString")
            imageReference.delete().addOnSuccessListener {
                Log.d(ContentValues.TAG, "Image deleted")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "Error deleting image")
            }
        }
    }
    fun deleteProject(userID : String ,projectName: String) {
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
            val imageReference = storageRef.reference.child("$userID/projects/$projectName/$imageString")
            imageReference.delete().addOnSuccessListener {
                Log.d(ContentValues.TAG, "Image deleted")
            }.addOnFailureListener {
                Log.d(ContentValues.TAG, "Error deleting image")
            }
        }
    }

}
