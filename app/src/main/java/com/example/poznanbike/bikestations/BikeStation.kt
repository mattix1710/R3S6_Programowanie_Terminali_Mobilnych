package com.example.poznanbike.bikestations


import android.os.Parcel
import android.os.Parcelable
import com.example.poznanbike.Geometry
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BikeStation(
   @Json(name = "geometry")
   val geometry: Geometry,
   @Json(name = "id")
   val id: String,
   @Json(name = "properties")
   val properties: Properties,
   @Json(name = "type")
   val type: String
) : Parcelable {
   constructor(parcel: Parcel) : this(
      parcel.readParcelable(Geometry::class.java.classLoader)!!,
      parcel.readString()!!,
      parcel.readParcelable(Properties::class.java.classLoader)!!,
      parcel.readString()!!
   ) {
   }

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeString(id)
      parcel.writeString(type)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<BikeStation> {
      override fun createFromParcel(parcel: Parcel): BikeStation {
         return BikeStation(parcel)
      }

      override fun newArray(size: Int): Array<BikeStation?> {
         return arrayOfNulls(size)
      }
   }
}