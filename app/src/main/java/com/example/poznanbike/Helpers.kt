package com.example.poznanbike

import android.content.Context
import android.os.Build
import com.example.poznanbike.bikestations.BikeStation
import com.example.poznanbike.bikestations.Geometry
import com.example.poznanbike.bikestations.Properties
import com.example.poznanbike.database.BikeStationDB


// Set of helper functions that are common for entire project. A singleton pattern is used here,
// so we don't need to create an instance of a Helper class
object Helpers {
    @Suppress("DEPRECATION")
    fun getColor(context: Context, colorId: Int): Int {
        // Custom implementation of the getColor method that disables the deprecation warning
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(colorId)
        } else {
            context.resources.getColor(colorId) // for older devices (older than API 23) to get the
                                                // color we first had to access resources
        }
    }

    // Function that converts the BikeStation (Retrofit version) to BikeStationDB (database version)
    fun createBikeStationDB(bikeStation: BikeStation): BikeStationDB {
        return BikeStationDB(
            bikeStation.properties.bikes.toInt(),
            bikeStation.properties.freeRacks.toInt(),
            bikeStation.properties.label,
            bikeStation.properties.updated,
            bikeStation.geometry.coordinates[0],
            bikeStation.geometry.coordinates[1]
        )
    }
    // Function that converts the BikeStationSB  (database version)  to BikeStation (Retrofit version)
    fun createBikeStation(bikeStationDB: BikeStationDB): BikeStation {

        val coors = listOf<Double>(bikeStationDB.longitude, bikeStationDB.latitude)
        return BikeStation(
            Geometry(coors, "Point"),
            "id",
            Properties(
                "Unknown",
                bikeStationDB.bikes.toString(),
                bikeStationDB.freeRacks.toString(),
                bikeStationDB.label,
                bikeStationDB.updated
            ),
            "Feature"
        )
    }

    fun createBikeStationList(bikeStationsDB: List<BikeStationDB>): ArrayList<BikeStation> {
        val arrayList = ArrayList<BikeStation>()
        for (bikeStationDB in bikeStationsDB) {
            arrayList.add(createBikeStation(bikeStationDB))
        }
        return arrayList
    }



}



