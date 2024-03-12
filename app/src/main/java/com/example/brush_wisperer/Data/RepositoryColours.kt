package com.example.brush_wisperer.Data

import androidx.lifecycle.LiveData
import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Local.Model.Database.ColourDatabase
import com.example.brush_wisperer.Data.Remote.ColourApi

class RepositoryColours(val database: ColourDatabase, private val colourApi: ColourApi) {

    val colourList = database.dao.getAllColours()


    suspend fun getDataFromApi(): List<ColourEntity> {
        return colourApi.retrofitService.readAll()
    }
    suspend fun getBrandAndRangeColours(brandName: String, colourRange: String) : LiveData<List<ColourEntity>> {
        return database.dao.getBrandAndRange(brandName,colourRange)
    }

    suspend fun insertAllColours(colourList: List<ColourEntity>) {
        for (colour in colourList) {
            val exitstingColour = database.dao.getColourById(colour.id)
            if (exitstingColour == null) {
                database.dao.insertColour(colour)
            }
        }
    }


    suspend fun insertColour(colour: ColourEntity) {
        database.dao.insertColour(colour)
    }

    suspend fun updateFavourite(id: Int, isFavorite: Boolean) {
        database.dao.updateFavourite(id, isFavorite)
    }
}
