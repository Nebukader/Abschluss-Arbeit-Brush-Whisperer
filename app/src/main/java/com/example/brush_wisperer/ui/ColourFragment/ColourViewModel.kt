package com.example.brush_wisperer.ui.ColourFragment

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabaseInstance.getDatabase
import com.example.brush_wisperer.Data.Model.FirestoreColour
import com.example.brush_wisperer.Data.Remote.ColourApi
import com.example.brush_wisperer.Data.RepositoryColours
import com.example.brush_wisperer.Data.RepositoryFirebase
import com.example.brush_wisperer.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
class ColourViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val repository = RepositoryColours(database, ColourApi)
    private val repoFirebase = RepositoryFirebase()

    private val _loading = MutableLiveData<ApiStatus>()
    val loading: LiveData<ApiStatus>
        get() = _loading

    private val _colourList = repository.colourList
    val colourList: LiveData<List<ColourEntity>>
        get() = _colourList

    private var _filteredColourList = MutableLiveData<List<ColourEntity>>(listOf())

    val filteredColourList: LiveData<List<ColourEntity>>
        get() = _filteredColourList

    fun filteredList(brandName: String) {
        viewModelScope.launch {
            repository.getBrandColours(brandName).observeForever { colours ->
                _filteredColourList.value = colours
                Log.d("Filter", "FilterViewModel = ${_filteredColourList.value.toString()}")
            }
        }
    }

    fun firebaseCurrentUserID(): String? {
        return repoFirebase.getCurrentUserID()
    }

    fun currentUserDB(): FirebaseFirestore {
        return repoFirebase.getDBInstance()
    }

    fun loadColourData() {
        viewModelScope.launch {
            _loading.value = ApiStatus.LOADING
            try {
                val colours = repository.getDataFromApi()
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

    fun saveColour(
        documentid: String,
        id: String,
        brandName: String,
        colourRange: String,
        colourPrimary: String,
        colourName: String,
        hexCode: String
    ) {
        viewModelScope.launch {
            val db = Firebase.firestore
            val colour = FirestoreColour(
                id = id,
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

    fun saveWishlist(
        colourId: String,
        brandName: String,
        colourRange: String,
        colourPrimary: String,
        colourName: String,
        hexcode: String
    ) {
        viewModelScope.launch {
            val userID = firebaseCurrentUserID().toString()
            val db = currentUserDB()
            val colour = FirestoreColour(
                colourId,
                brandName,
                colourRange,
                colourPrimary,
                colourName,
                hexcode
            )
            db.collection("users").document(userID).collection("wishlist").document(colourId)
                .set(colour)
                .addOnSuccessListener { documentReference ->
                }
                .addOnFailureListener() { e ->
                    Toast.makeText(
                        getApplication(),
                        getApplication<Application>().getString(R.string.error_adding_colour_to_wishlist),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

}