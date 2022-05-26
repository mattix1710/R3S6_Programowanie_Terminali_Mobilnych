package com.example.labfirebase

// data class for firebase - by specifying default values in the constructor
// we also provide a no-argument constructor required by firebase
data class SensorValue (val timestamp: Long = 0L, val value: Float = 0f){

    override fun toString(): String = value.toString()
}