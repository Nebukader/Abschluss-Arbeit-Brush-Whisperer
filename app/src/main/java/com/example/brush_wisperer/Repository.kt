package com.example.brush_wisperer

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.brush_wisperer.Data.Model.ColourData
import com.example.brush_wisperer.Data.Remote.ColourApi

class Repository {


    // region API Call for Colours
    private val _colourData = MutableLiveData<List<ColourData>>()
    val colourData: MutableLiveData<List<ColourData>>
        get() = _colourData

    suspend fun refreshData() {
        _colourData.value = ColourApi.retrofitService.readAll()
        Log.d("Repository", "refreshData: ${_colourData.value}")
    }
    //endregion
}