package com.example.lab03_mytodolist

interface ToDoListListener {
    fun onItemClick(position: Int)
    fun onItemLongClick(position: Int)
}