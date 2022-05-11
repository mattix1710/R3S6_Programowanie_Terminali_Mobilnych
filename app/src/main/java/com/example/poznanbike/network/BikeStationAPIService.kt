package com.example.poznanbike.network

import com.example.poznanbike.bikestations.BikeStations
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

//Constants for service endpoints
private const val BASE_URL = "https://www.poznan.pl/mim/plan/"
private const val STATIONS_ENDPOINT = "map_service.html?mtype=pub_transport&co=stacje_rowerowe"
private const val QUERY_ENDPOINT = "map_service.html"

//Constants used to specify the URL for network operations
//
//used to convert the JSON response to the classes in "bikestations" package
//Moshi has a built-in support for Kotlin-JSON conversion
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//That object will be used to create the retrofit service. We specify the
//converter for the network response in the "addConverterFactory" method where
//we use the "moshi" variable to create the converter. The base URL of the service is set
//with the "baseURL" method of the "Retrofit.Builder"
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface BikeStationAPIService {
    @GET(STATIONS_ENDPOINT)
    suspend fun getBikeStations(): BikeStations

    @GET(QUERY_ENDPOINT)
    suspend fun getBikeStations(@Query("mtype") mType: String, @Query("co") co: String): BikeStations
}

object BikeStationApi{
    val retrofitService: BikeStationAPIService by lazy { retrofit.create(BikeStationAPIService::class.java)}
}