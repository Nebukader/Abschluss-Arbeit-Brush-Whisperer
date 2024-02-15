package com.example.brush_wisperer.Data

import android.util.Log
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabase
import com.example.brush_wisperer.Data.Remote.ColourApi

class RepositoryColours(private val database: ColourDatabase,private val colourApi: ColourApi) {

    val colourList = database.dao.getAllColours()

    suspend fun getDataFromApi(): List<ColourEntity> {
        return colourApi.retrofitService.readAll()
    }

    suspend fun insertAllColours(colourList: List<ColourEntity>) {
        for (colour in colourList) {
            val exitstingColour = database.dao.getColourById(colour.id)
            if (exitstingColour == null) {
                database.dao.insertColour(colour)
            }
            Log.d("Repository", "insertAllColours: $colour")
        }
    }


    suspend fun insertColour(colour: ColourEntity) {
        database.dao.insertColour(colour)
    }

    suspend fun updateFavourite(id: Int, isFavorite: Boolean) {
        database.dao.updateFavourite(id, isFavorite)
    }
}
