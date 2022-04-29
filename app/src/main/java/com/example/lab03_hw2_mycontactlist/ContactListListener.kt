package com.example.lab03_hw2_mycontactlist

interface ContactListListener {
    fun onContactClick(position: Int)
    fun onContactLongClick(position: Int)
    fun onDeleteButtonClick(position: Int)
}