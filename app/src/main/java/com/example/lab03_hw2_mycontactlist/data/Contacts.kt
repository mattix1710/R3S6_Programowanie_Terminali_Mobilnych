package com.example.lab03_hw2_mycontactlist.data

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import com.example.lab03_hw2_mycontactlist.R
import java.util.*
import java.util.concurrent.ThreadLocalRandom

fun makeImage(): Int {
    val images = arrayOf<Int>(
        R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4, R.drawable.avatar_5, R.drawable.avatar_6,
        R.drawable.avatar_7, R.drawable.avatar_8, R.drawable.avatar_9, R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12,
        R.drawable.avatar_13, R.drawable.avatar_14, R.drawable.avatar_15, R.drawable.avatar_16)

    val rand = ThreadLocalRandom.current()
    return images[rand.nextInt(images.size)]
}

/**
 * Helper class for providing sample firstName for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object Contacts {

    /**
     * An array of sample (placeholder) items.
     */
    val ITEMS: MutableList<ContactItem> = ArrayList()

    /**
     * A map of sample (placeholder) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, ContactItem> = HashMap()

    private val COUNT = 5

    fun getCount(): Int {
        return COUNT
    }

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addContact(createPlaceholderItem(i))
        }
    }

    fun addContact(item: ContactItem) {
        ITEMS.add(item)
    }

    private fun createPlaceholderItem(position: Int): ContactItem {
        return ContactItem(position.toString(), makeName(position), makeBirthday(position), makePhoneNumber(position), makeImage())  //makeDetails - returns to Birthday; NOTHING FOR phoneNumber
    }

    private fun makePhoneNumber(position: Int): String {
        var aux: String = "(+48) "
        for(i in 0 until position){
            aux += position.toString()
        }
        return aux
    }

    private fun makeBirthday(position: Int): String {
        var auxDayMonth: Int = 1
        var auxYear: Int = 1990
        var str: String = (auxDayMonth*position).toString().padStart(2, '0') + "/" +
                          (auxDayMonth*position).toString().padStart(2, '0') + "/" +
                          (auxYear+position).toString()
        return str
    }

    private fun makeName(position: Int): String {
        val bigLetter = 65
        val smallLetter = 97
        var str: String = (bigLetter+position-1).toChar().toString()

        for(i in 0 until position-1){
            str += (smallLetter+position-1).toChar().toString()
        }

        str += " "  + (bigLetter+position).toChar().toString()
        for(i in 0 until position-1){
            str += (smallLetter+position).toChar().toString()
        }

        return str
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    fun updateContact(contactToEdit: ContactItem?, newContact: ContactItem) {
        contactToEdit?.let {
            oldContact ->
            //Perform this operations only when contactToEdit is not null
            //Find the index of old contact
            val indexOfOldContact = ITEMS.indexOf(oldContact)
            //Place new contact in place of oldContact
            ITEMS.add(indexOfOldContact, newContact)
            //Remove the oldContact that was moved to the next position in the ITEMS list
            ITEMS.removeAt(indexOfOldContact+1)
        }
    }

    /**
     * A placeholder item representing a piece of firstName.
     */

}

data class ContactItem(val id: String, val name: String, val birthday: String, val phoneNumber: String, val imgId: Int): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()!!
    ) {
    }

    override fun toString(): String = name
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(birthday)
        parcel.writeString(phoneNumber)
        parcel.writeInt(imgId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ContactItem> {
        override fun createFromParcel(parcel: Parcel): ContactItem {
            return ContactItem(parcel)
        }

        override fun newArray(size: Int): Array<ContactItem?> {
            return arrayOfNulls(size)
        }
    }
}