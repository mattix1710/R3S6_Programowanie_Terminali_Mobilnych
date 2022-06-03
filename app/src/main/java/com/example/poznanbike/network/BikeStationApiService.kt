package com.example.poznanbike.network

import com.example.poznanbike.bikestations.BikeStations
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://www.poznan.pl/mim/plan/"
private const val STATIONS_ENDPOINT = "map_service.html?mtype=pub_transport&co=stacje_rowerowe"
private const val QUERY_ENDPOINT = "map_service.html"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BikeStationApiService {
    @GET(STATIONS_ENDPOINT)
    suspend fun getBikeStations(): BikeStations

    @GET(QUERY_ENDPOINT)
    suspend fun getBikeStations(@Query("mtype")mType: String, @Query("co") co:String): BikeStations
}

object BikeStationApi{
    val retrofitService: BikeStationApiService by lazy {retrofit.create(BikeStationApiService::class.java)}
}