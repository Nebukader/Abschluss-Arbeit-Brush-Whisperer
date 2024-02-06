package com.example.brush_wisperer.Data.Remote

import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.example.brush_wisperer.Data.Model.ColourList
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://dockerpi/back/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofit übernimmt die Kommunikation und übersetzt die Antwort
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// Das Interface bestimmt, wie mit dem Server kommuniziert wird
interface ColourApiService {

    /**
     * Diese Funktion spezifiziert die URL und holt die Liste an Informationen
     */
    @GET("readAll.php")
    suspend fun readAll(): List<ColourEntity>

}

// Das Objekt dient als Zugangspunkt für den Rest der App und stellt den API Service zur Verfügung
object ColourApi {
    val retrofitService: ColourApiService by lazy { retrofit.create(ColourApiService::class.java) }
}