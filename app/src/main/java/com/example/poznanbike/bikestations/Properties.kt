package com.example.poznanbike.bikestations


import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Properties(
   @Json(name = "bike_racks")
   val bikeRacks: String,
   @Json(name = "bikes")
   val bikes: String,
   @Json(name = "free_racks")
   val freeRacks: String,
   @Json(name = "label")
   val label: String,
   @Json(name = "updated")
   val updated: String
) : Parcelable {
   constructor(parcel: Parcel) : this(
      parcel.readString()!!,
      parcel.readString()!!,
      parcel.readString()!!,
      parcel.readString()!!,
      parcel.readString()!!
   ) {
   }

   override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeString(bikeRacks)
      parcel.writeString(bikes)
      parcel.writeString(freeRacks)
      parcel.writeString(label)
      parcel.writeString(updated)
   }

   override fun describeContents(): Int {
      return 0
   }

   companion object CREATOR : Parcelable.Creator<Properties> {
      override fun createFromParcel(parcel: Parcel): Properties {
         return Properties(parcel)
      }

      override fun newArray(size: Int): Array<Properties?> {
         return arrayOfNulls(size)
      }
   }
}