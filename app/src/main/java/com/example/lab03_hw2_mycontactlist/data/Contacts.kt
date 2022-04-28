package com.example.lab03_hw2_mycontactlist.data

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList
import java.util.HashMap

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
        return ContactItem(position.toString(), makeName(position), makeBirthday(position), makePhoneNumber(position))  //makeDetails - returns to Birthday; NOTHING FOR phoneNumber
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

data class ContactItem(val id: String, val name: String, val birthday: String, val phoneNumber: String): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun toString(): String = name
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(birthday)
        parcel.writeString(phoneNumber)
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