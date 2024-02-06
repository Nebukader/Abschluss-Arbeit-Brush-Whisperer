package com.example.brush_wisperer.Data.Local.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity( tableName = "colour_table")
data class ColourEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "brand_name")
    val brand_name: String,
    @ColumnInfo(name = "colour_range")
    val colour_range: String,
    @ColumnInfo(name = "colour_primary")
    val colour_primary: String,
    @ColumnInfo(name = "colour_name")
    val colour_name: String,
    @ColumnInfo(name = "hexcode")
    val hexcode: String,
    @ColumnInfo(name = "picture")
    val picture: String,

)