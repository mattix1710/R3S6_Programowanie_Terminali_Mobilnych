package com.example.rollittogether

interface RemainingRolls {
    fun decrement()
    fun restore()
    fun canRoll(): Boolean
}