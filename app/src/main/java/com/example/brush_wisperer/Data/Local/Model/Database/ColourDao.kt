package com.example.brush_wisperer.Data.Local.Model.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brush_wisperer.Data.Local.Model.ColourEntity

@Dao
interface ColourDao {
    @Query("SELECT * FROM colour_table")
    fun getAllColours(): LiveData<List<ColourEntity>>

    @Query("DELETE FROM colour_table")
    suspend fun deleteAllColours()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertColour(colourEntity: ColourEntity)

    @Query("SELECT * FROM colour_table WHERE id = :id")
    suspend fun getColourById(id: Int): ColourEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllColours(colourList: List<ColourEntity>)

    @Query("UPDATE colour_table SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavourite(id: Int, isFavorite: Boolean)

    @Query("SELECT * FROM colour_table WHERE brand_name = :brandName")
    fun getBrandColours(brandName: String): LiveData<List<ColourEntity>>

}
