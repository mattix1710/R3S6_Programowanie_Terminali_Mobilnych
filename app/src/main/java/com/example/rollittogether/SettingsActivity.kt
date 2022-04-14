package com.example.rollittogether

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.GridView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text
import java.lang.NumberFormatException


class SettingsActivity : AppCompatActivity() {
    private lateinit var gridView: GridView
    private lateinit var editPlayer1: EditText
    private lateinit var editPlayer2: EditText
    private lateinit var editThrowCount: EditText
    private lateinit var editCubeCount: EditText

    //sharedPreferences use - allows to save data on device
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES, MODE_PRIVATE)

        gridView = findViewById(R.id.cubePreview)
        gridView.adapter = CubeAdapter(this)

        gridView.setOnItemClickListener(OnItemClickListener { parent, v, position, id ->
            val tag: Int = v.getTag() as Int
            val bgId: Int = R.drawable.border
            if(tag != null && tag == bgId) {
                v.setBackgroundResource(R.drawable.border_inactive)
                v.setTag(R.drawable.border_inactive)
            }
            else{
                v.setBackgroundResource(R.drawable.border)
                v.setTag(R.drawable.border)
            }
        })

        readValues()

        saveValues()
    }

    private fun saveValues(){
        //create Editor to save data
        val editor: SharedPreferences.Editor = sharedPreferences.edit ()
        //set addTextChangedListener to all editText resources

        editPlayer1 = findViewById(R.id.editPlayer1Name)
        editPlayer2 = findViewById(R.id.editPlayer2Name)
        editThrowCount = findViewById(R.id.editThrowCount)
        editCubeCount = findViewById(R.id.editCubeCount)

        editPlayer1.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                editor.putString(MainActivity.player1Name, p0.toString())
                val name: TextView = findViewById(R.id.player1Name)
                name.setText(p0.toString())
                if(name.text.isEmpty()) {
                    name.setText(resources.getString(R.string.player1Display))
                    editor.putString(MainActivity.player1Name, resources.getString(R.string.player1Display))
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
                        resources.getString(R.string.player2Display)
                    )
                    editor.apply()
                }
            }
        })

        editThrowCount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    var aux: Int = Integer.parseInt(p0.toString())
                    if(aux > 10){
                        p0!!.replace(0, p0.length, "10", 0, 2)
                    }else if(aux < 3){
                        p0!!.replace(0, p0.length, "3", 0, 1)
                    }
                } catch(ex: NumberFormatException){
                    //Do something
                }

                editor.putInt(MainActivity.throwQuantity, Integer.parseInt(p0.toString()))
                editor.apply()
            }

        })

        editCubeCount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //TODO("Not yet implemented")
            }

            override fun afterTextChanged(p0: Editable?) {
                try {
                    var aux: Int = Integer.parseInt(p0.toString())
                    if(aux > 10){
                        p0!!.replace(0, p0.length, "10", 0, 2)
                    }else if(aux < 2){
                        p0!!.replace(0, p0.length, "2", 0, 2)
                    }
                }catch (ex: NumberFormatException){
                    //Do something
                }
                if(!p0.isNullOrEmpty()) {           //checks if inserted quantity of cubes is not null or empty
                    editor.putInt(MainActivity.cubeQuantity, Integer.parseInt(p0.toString()))
                    editor.apply()
                }
            }

        })



        //editThrowCount.addTextChangedListener(object: TextWatcher)
        }

    private fun readValues(){
        editPlayer2 = findViewById(R.id.editPlayer2Name)
        if(sharedPreferences.contains(MainActivity.player2Name))
            if(!sharedPreferences.getString(MainActivity.player2Name, null).equals(resources.getString(R.string.player2Display)))
                editPlayer2.setText(sharedPreferences.getString(MainActivity.player2Name, null))
    }

    private fun setDefaults(){

    }
}