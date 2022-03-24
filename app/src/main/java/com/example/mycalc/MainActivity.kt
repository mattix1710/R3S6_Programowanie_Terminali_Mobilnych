package com.example.mycalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class MainActivity : AppCompatActivity() {
    private var currentOperation: String = " "
    var result: Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spinner: Spinner = findViewById(R.id.operationSpinner)
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                //find the operation buttons
                val op1Button = findViewById<Button>(R.id.firstOperationButton)
                val op2Button = findViewById<Button>(R.id.secondOperationButton)

                //based on the selected position change the text of the buttons
                when(position){
                    0 -> {
                        op1Button.text = "+"
                        op2Button.text = "-"
                    }
                    1 -> {
                        op1Button.text = "*"
                        op2Button.text = "/"
                    }
                    else -> {
                        op1Button.text = "+"
                        op2Button.text = "-"
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            val calcButton: Button = findViewById(R.id.calcButton)
            calcButton.setOnClickListener( this )
        }
    }

    fun selectOperation(view: View) {       //the view passed as an argument correspond to a clicked view
        currentOperation = (view as Button).text.toString() //get the current operation string from
                                                            //the button text. Cast with as keyword is performed
        updateOperation()                   //update the text in the operatorSymbol TextView
    }

    private fun updateOperation() {
        val operationTxt = findViewById<TextView>(R.id.operatorSymbol)  //find the TextView
        operationTxt.text = currentOperation    //update its text
    }

    override fun onClick(p0: View?){
        //Retrieve the numbers from EditText views. The string from the text attribute
        //is converted to a floating point value with toFloatOrNull() that can return a null value,
        //hence the use of the elvis operator. The null value would be returned if the edit text
        //contents were empty or not a number;
        val firstNum =
            (findViewById<EditText>(R.id.firstNumber).text).toString().toFloatOrNull() ?: 0f
        val secondNum =
            (findViewById<EditText>(R.id.secondNumber).text).toString().toFloatOrNull() ?: 0f

        //pass the retrieved numbers to getResult method that returns a string representing the outcome
        val resultStr: String = getResult(firstNum, secondNum)

        //update the result TextView
        updateResult(resultStr)
    }
}