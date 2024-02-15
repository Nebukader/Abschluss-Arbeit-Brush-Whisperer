package com.example.brush_wisperer.ui.ColourFragment

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDao
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabaseInstance.getDatabase
import com.example.brush_wisperer.Data.Model.firestoreColour
import com.example.brush_wisperer.Data.Remote.ColourApi
import com.example.brush_wisperer.Data.RepositoryColours
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
class ColourViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = RepositoryColours(database, ColourApi)

    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    val colourList = repository.colourList

    init {
    loadData()
   }

    private fun loadData() {
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                val colours = repository.getDataFromApi()
                Log.d(TAG, "Colours: $colours")
                repository.insertAllColours(colours)
                _loading.value = ApiStatus.DONE
            } catch (e: Exception) {
                Log.e(TAG, "Error loading Data $e")
                if (colourList.value.isNullOrEmpty()) {
                    _loading.value = ApiStatus.ERROR
                } else {
                    _loading.value = ApiStatus.DONE
                }
            }
        }
    }

    fun saveColour(documentid:String,id:String, brandName: String, colourRange: String, colourPrimary: String, colourName: String, hexCode: String) {
        viewModelScope.launch {
            val db = Firebase.firestore
            val colour = firestoreColour(
                id= id,
                brandName = brandName,
                colourRange = colourRange,
                colourPrimary = colourPrimary,
                colourName = colourName,
                hexCode = hexCode
            )
            db.collection("users").document(documentid).collection("colours")
                .document(id)
                .set(colour)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot sucessfully written")
                }
                .addOnFailureListener() { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }
    }
    fun deleteColour(documentid: String, id: String) {
        viewModelScope.launch {
            val db = Firebase.firestore
            db.collection("users").document(documentid).collection("colours")
                .document(id)
                .delete()
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot sucessfully deleted")
                }
                .addOnFailureListener() { e ->
                    Log.w(TAG, "Error deleting document", e)
                }
        }
    }
    fun updateFavourite(id: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.updateFavourite(id, isFavorite)
        }
    }

}