package com.example.rollittogether

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)

        var textViewPlayer: TextView = findViewById(R.id.player1)
        textViewPlayer.setText(sharedPreferences.getString(player1Name, null))

        textViewPlayer = findViewById(R.id.player2)
        if(sharedPreferences.getString(player2Name, null)?.isBlank() == true)
            textViewPlayer.setText(R.string.player_2)
        else
            textViewPlayer.setText(sharedPreferences.getString(player2Name, null))

        textViewPlayer = findViewById(R.id.throwQuantity)
        if(sharedPreferences.contains(throwQuantity))
            textViewPlayer.setText(sharedPreferences.getInt(throwQuantity, 3))
        else
            textViewPlayer.setText(R.integer.defaultThrowCount.toInt())

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Create a menu with menuInflater and R.menu.menu resource
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //define the actions linked to each menu entry
        when(item.itemId){
            R.id.settingsItem -> startSettingsActivity()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private val launchSettingsActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
        if(result.resultCode == RESULT_OK){
            // Get the result from settings activity
        }
    }

    private fun startSettingsActivity(){
        //create an Intent to start SettingsActivity
        val intent: Intent = Intent(this, SettingsActivity::class.java)

        launchSettingsActivity.launch(intent)
    }

    companion object {          //setting constant val used globally
        const val MyPREFERENCES = "saveFile"
        const val player1Name = "player1NameKey"
        const val player2Name = "player2NameKey"
        const val throwQuantity = "throwQuantityKey"
        const val cubeQuantity = "cubeQuantityKey"
    }
}