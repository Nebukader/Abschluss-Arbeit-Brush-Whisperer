package com.example.brush_wisperer.Data.Model

data class ColourData (
    val id: Int,
    val brand_name: String,
    val colour_range: String,
    val colour_primary: String, // Neues Feld hinzugefügt
    val colour_name: String,
    val hexcode: String, // Feldname von 'hexecode' zu 'hexcode' geändert
    val price: String, // Datentyp von 'Double' zu 'String' geändert
    val picture: String,
)