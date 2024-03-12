package com.example.brush_wisperer.Data.Remote

import com.example.brush_wisperer.Data.Local.Model.ColourEntity
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "http://brushwhisperer.ddns.net:9580/back/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ColourApiService {

    @GET("readAll.php")
    suspend fun readAll(): List<ColourEntity>

}

object ColourApi {
    val retrofitService: ColourApiService by lazy { retrofit.create(ColourApiService::class.java) }
}