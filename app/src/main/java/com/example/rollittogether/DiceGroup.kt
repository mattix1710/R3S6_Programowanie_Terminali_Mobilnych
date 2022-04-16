package com.example.rollittogether

import android.util.Log

interface Dice {
    fun rollDice(callback: () -> Unit)
    fun resetDice()
    fun isSelected(): Boolean
    fun toggleSelection()
    fun getValue(): Int
}

class DiceGroup(val dices: List<Dice> = emptyList()) {
    fun rollDices(onRoll: (List<Dice>) -> Unit = {}){
        val filteredDices = dices.filter { !it.isSelected() }
        val finalCallback = {onRoll(filteredDices)}
        val nextInQueue = filteredDices.foldRight(finalCallback){dice, callback ->
            {dice.rollDice(callback)}
        }
        nextInQueue()
    }

    fun asCounts() = dices.fold(mutableMapOf<Int, Int>()){map, dice ->
        map[dice.getValue()] = map[dice.getValue()]?.plus(1) ?: 1
        map
    }

    fun getSum(): Int{
        var sum: Int = 0
        for(element in dices){
            sum += element.getValue()
        }

        return sum
    }

    fun deselectAll(){
        dices.forEach{it.resetDice()}
    }

    fun hasBeenThrown() = dices.all{it.getValue() > 0}
}