package com.example.poznanbike.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BikeStationDB::class], version = 1, exportSchema = false)
abstract class BikeStationDatabase: RoomDatabase() {
    abstract val bikeStationDatabaseDao: BikeStationDAO

    companion object{
        @Volatile
        private var INSTANCE: BikeStationDatabase? = null
        fun getInstance(context: Context): BikeStationDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BikeStationDatabase::class.java,
                        "saved_bike_stations"
                    ).fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }

    enum class QUERYID{
        GET_STATIONS_WITH_BIKES,
        GET_STATIONS_WITH_FREE_RACKS,
        GET_ALL,
        CLEAR
    }
}