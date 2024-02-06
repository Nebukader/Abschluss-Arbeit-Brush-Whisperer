package com.example.brush_wisperer.ui.ColourFragment

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabaseInstance.getDatabase
import com.example.brush_wisperer.Data.Remote.ColourApi
import com.example.brush_wisperer.Data.RepositoryColours
import kotlinx.coroutines.launch

enum class ApiStatus { LOADING, ERROR, DONE }
class ColourViewModel(application: Application) : AndroidViewModel(application){

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
}