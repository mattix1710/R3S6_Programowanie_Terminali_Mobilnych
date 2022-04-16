package com.example.rollittogether

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.GridView
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class SettingsActivity : AppCompatActivity() {
    private lateinit var editPlayer1: EditText
    private lateinit var editPlayer2: EditText
    private lateinit var numberPickerThrows: NumberPicker
    private lateinit var numberPickerCubes: NumberPicker

    //sharedPreferences use - allows to save data on device
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE)

        numberPickerThrows = findViewById(R.id.editThrowCount)
        numberPickerThrows.minValue = 3
        numberPickerThrows.maxValue = 10

        numberPickerCubes = findViewById(R.id.editCubeCount)
        numberPickerCubes.minValue = 2
        numberPickerCubes.maxValue = 10

        readValues()

        saveValues()
    }

    private fun saveValues(){
        //create Editor to save data
        val editor: SharedPreferences.Editor = sharedPreferences.edit ()
        //set addTextChangedListener to all editText resources

        editPlayer1 = findViewById(R.id.editPlayer1Name)
        editPlayer2 = findViewById(R.id.editPlayer2Name)

        editPlayer1.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editor.putString(MainActivity.player1Name, p0.toString())
                editor.apply()
            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.isNullOrBlank()) {
                    editor.putString(
                        MainActivity.player1Name,
                        resources.getString(R.string.player_1)
                    )
                    editor.apply()
                }
            }
        })

        editPlayer2.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                editor.putString(MainActivity.player2Name, p0.toString())
                editor.apply()
            }

            override fun afterTextChanged(p0: Editable?) {
                if(p0.isNullOrBlank()) {
                    editor.putString(
                        MainActivity.player2Name,
                        resources.getString(R.string.player_2)
                    )
                    editor.apply()
                }
            }
        })

        numberPickerThrows.setOnValueChangedListener(OnValueChangeListener { picker, oldVal, newVal ->
            editor.putInt(MainActivity.throwNumMax, newVal)
            editor.apply()
        })

        numberPickerCubes.setOnValueChangedListener(OnValueChangeListener { picker, oldVal, newVal ->
            editor.putInt(MainActivity.cubeQuantity, newVal)
            editor.apply()
        })
        }

    private fun readValues(){
        editPlayer1 = findViewById(R.id.editPlayer1Name)
        if(sharedPreferences.contains(MainActivity.player1Name))
            if(!sharedPreferences.getString(MainActivity.player1Name, null).equals(resources.getString(R.string.player_1)))
                editPlayer1.setText(sharedPreferences.getString(MainActivity.player1Name, null))

        editPlayer2 = findViewById(R.id.editPlayer2Name)
        if(sharedPreferences.contains(MainActivity.player2Name))
            if(!sharedPreferences.getString(MainActivity.player2Name, null).equals(resources.getString(R.string.player_2)))
                editPlayer2.setText(sharedPreferences.getString(MainActivity.player2Name, null))

        if(sharedPreferences.contains(MainActivity.throwNumMax))
            numberPickerThrows.value = sharedPreferences.getInt(MainActivity.throwNumMax, 3)

        if(sharedPreferences.contains(MainActivity.cubeQuantity))
            numberPickerCubes.value = sharedPreferences.getInt(MainActivity.cubeQuantity, 2)
    }
}