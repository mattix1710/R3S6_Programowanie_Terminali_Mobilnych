package com.example.rollittogether

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    DicesViewAdapter.DiceInteractionListener{
    lateinit var sharedPreferences: SharedPreferences
    private lateinit var diceGroup: DiceGroup
    private lateinit var remainingRolls: RemainingRolls
    private lateinit var fab: FloatingActionButton
    private var auxSum: Int = 0
    private var currentPlayer: Int = PLAYER.PLAYER_1.ordinal

    companion object {          //setting constant val used globally
        const val MyPREFERENCES = "saveFile"
        const val player1Name = "player1NameKey"
        const val player2Name = "player2NameKey"
        const val throwNumMax = "throwQuantityKey"
        const val cubeQuantity = "cubeQuantityKey"
        const val player1Score = "player1ScoreKey"
        const val player2Score = "player2ScoreKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE)

        if(!sharedPreferences.contains(player1Score)) {
            Log.i("new", "create new saveFile")
            setDefaults()
        }

        var dicesNum = sharedPreferences.getInt(cubeQuantity, 2)
        diceGroup = DiceGroup((1 until dicesNum+1).map {DicesViewAdapter.makeDice()})

        //TextView handling...
        var textViewPlayer: TextView = findViewById(R.id.player1)
        textViewPlayer.setText(sharedPreferences.getString(player1Name, null))

        textViewPlayer = findViewById(R.id.player2)
        if(sharedPreferences.getString(player2Name, null)?.isBlank() == true)
            textViewPlayer.setText(R.string.player_2)
        else
            textViewPlayer.setText(sharedPreferences.getString(player2Name, null))

        textViewPlayer = findViewById(R.id.throwQuantity)
        if(sharedPreferences.contains(throwNumMax))
            textViewPlayer.setText(sharedPreferences.getInt(throwNumMax, 3).toString())
        else
            textViewPlayer.setText(R.integer.defaultThrowCount.toInt())
        /////////////////////////////

        remainingRolls = RemainingRollsImpl(throwQuantity, sharedPreferences)
        fab = findViewById(R.id.fab)

        dice_list.let{
            //it.layoutManager = LinearLayoutManager(it.context, LinearLayoutManager.HORIZONTAL, false)
            it.adapter = DicesViewAdapter(diceGroup.dices, this)
            it.layoutManager = GridLayoutManager(it.context, 3, GridLayoutManager.VERTICAL, false)
            //it.layoutManager
        }

        fab.setOnClickListener{
            onThrowDicesClicked()
        }

        begin.setOnClickListener(){
            fab.visibility = View.VISIBLE
            begin.visibility = View.GONE
            beginDescription.visibility = View.GONE
            end.visibility = View.VISIBLE

            player1.text = sharedPreferences.getString(player1Name, null)
                ?.let { it1 -> setBold(it1) }
        }

        end.setOnClickListener(){
            val editor: SharedPreferences.Editor = sharedPreferences.edit ()
            if(currentPlayer == PLAYER.PLAYER_1.ordinal){
                editor.putInt(player1Score, auxSum)
                editor.apply()
                player1_points.text = auxSum.toString()
                player1.text = sharedPreferences.getString(player1Name, null)
                    ?.let { it1 -> setNormal(it1) }
                currentPlayer = PLAYER.PLAYER_2.ordinal
                player2.text = sharedPreferences.getString(player2Name, null)
                    ?.let { it1 -> setBold(it1) }

                restoreCubes()
            }
            else {
                editor.putInt(player2Score, auxSum)
                editor.apply()
                player2_points.text = auxSum.toString()


                estPoints.visibility = View.INVISIBLE
                dice_list.visibility = View.INVISIBLE

                var p1 = sharedPreferences.getInt(player1Score, 0)
                var p2 = sharedPreferences.getInt(player2Score, 0)
                val win: TextView = findViewById(R.id.winner)

                if (p1 > p2)
                    win.text =
                        getString(R.string.winner, sharedPreferences.getString(player1Name, null))
                else if (p2 > p1)
                    win.text =
                        getString(R.string.winner, sharedPreferences.getString(player2Name, null))
                else
                    win.text = "Remis!" as CharSequence


                win.visibility = View.VISIBLE
                end.visibility = View.GONE
                repeat.visibility = View.VISIBLE
                fab.visibility = View.INVISIBLE
            }
        }

        repeat.setOnClickListener(){
            restoreCubes()
            begin.visibility = View.VISIBLE
            end.visibility = View.GONE
            winner.visibility = View.GONE
            estPoints.visibility = View.VISIBLE
            dice_list.visibility = View.VISIBLE
            fab.visibility = View.VISIBLE
            repeat.visibility = View.GONE

            player1.text = sharedPreferences.getString(player1Name, null)
                ?.let { it1 -> setNormal(it1) }
            player2.text = sharedPreferences.getString(player2Name, null)
                ?.let { it1 -> setNormal(it1) }

            currentPlayer = PLAYER.PLAYER_1.ordinal
            player1_points.text = getString(R.string.player1_points)
            player2_points.text = getString(R.string.player2_points)
            estPoints.text = getString(R.string.points)
        }
    }

    fun restoreCubes(){
        diceGroup.deselectAll()
        remainingRolls.restore()
    }

    fun setBold(str: String): SpannableString{
        var ss: SpannableString = SpannableString(str)
        val boldSpan: StyleSpan = StyleSpan(Typeface.BOLD)
        ss.setSpan(boldSpan, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    fun setNormal(str: String): SpannableString{
        var ss: SpannableString = SpannableString(str)
        val boldSpan: StyleSpan = StyleSpan(Typeface.NORMAL)
        ss.setSpan(boldSpan, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
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
            R.id.defaultSettings -> restoreDefaults()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun restoreDefaults() {
        //set default values
        setDefaults()

        //and restart main activity
        finish()
        startActivity(getIntent())
    }

    private fun setDefaults(){
        val editor: SharedPreferences.Editor = sharedPreferences.edit ()
        editor.putString(player1Name, resources.getString(R.string.player_1))
        editor.apply()
        editor.putString(player2Name, resources.getString(R.string.player_2))
        editor.apply()
        editor.putInt(throwNumMax, 3)
        editor.apply()
        editor.putInt(cubeQuantity, 2)
        editor.apply()
        editor.putInt(player1Score, 0)
        editor.apply()
        editor.putInt(player2Score, 0)
        editor.apply()
    }

    private fun startSettingsActivity(){
        //create an Intent to start SettingsActivity
        val intent: Intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    override fun onDiceClicked(item: Dice) {
        Toast.makeText(applicationContext, if(item.isSelected()) "Zablokowano" else "Odblokowano", Toast.LENGTH_SHORT).show()
    }

    fun onThrowDicesClicked(){
        auxSum = 0
        diceGroup.run {
            rollDices{
                remainingRolls.decrement()
                for(element in diceGroup.asCounts()){
                    Log.i(element.key.toString(), element.value.toString())
                    auxSum += element.key*element.value
                    Log.i("currSum", auxSum.toString())
                }
                dispCurrentSum()
                if(remainingRolls.canRoll()) fab.show() else end
            }
        }
    }

    fun dispCurrentSum(){
        estPoints.text = auxSum.toString()
    }

    class RemainingRollsImpl(val textView: TextView, val prefs: SharedPreferences) : RemainingRolls {

        var counter: Int = prefs.getInt(MainActivity.throwNumMax, 3)

        override fun decrement() {
            counter = (counter - 1)
            textView.text = counter.toString()
        }

        override fun restore() {
            counter = prefs.getInt(MainActivity.throwNumMax, 3)
            textView.text = counter.toString()
        }

        override fun canRoll() = counter > 0
    }
}

enum class PLAYER{
    PLAYER_1,
    PLAYER_2
}