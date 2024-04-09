package com.example.calculator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import android.view.View
import android.widget.Button
import android.widget.EditText


class MainActivity : AppCompatActivity() {

    private var isNewOp=true
    private var dot=false
    private var operator="X"
    private var oldNumber=""

    private var ShowNumber: EditText? = null
    private var button0: Button? = null
    private var button1: Button? = null
    private var button2: Button? = null
    private var button3: Button? = null
    private var button4: Button? = null
    private var button5: Button? = null
    private var button6: Button? = null
    private var button7: Button? = null
    private var button8: Button? = null
    private var button9: Button? = null
    private var buttonSum: Button? = null
    private var buttonSub: Button? = null
    private var buttonMul: Button? = null
    private var buttonDiv: Button? = null
    private var buttonDot: Button? = null
    private var buttonPlusMinus: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ShowNumber = findViewById<EditText>(R.id.etShowNumber) as EditText
        button0 = findViewById<Button>(R.id.button0) as Button
        button1 = findViewById<Button>(R.id.button1) as Button
        button2 = findViewById<Button>(R.id.button2) as Button
        button3 = findViewById<Button>(R.id.button3) as Button
        button4 = findViewById<Button>(R.id.button4) as Button
        button5 = findViewById<Button>(R.id.button5) as Button
        button6 = findViewById<Button>(R.id.button6) as Button
        button7 = findViewById<Button>(R.id.button7) as Button
        button8 = findViewById<Button>(R.id.button8) as Button
        button9 = findViewById<Button>(R.id.button9) as Button
        buttonSum = findViewById<Button>(R.id.buttonSum) as Button
        buttonSub = findViewById<Button>(R.id.buttonSubstract) as Button
        buttonMul = findViewById<Button>(R.id.buttonMultiply) as Button
        buttonDiv = findViewById<Button>(R.id.buttonDiv) as Button
        buttonDot = findViewById<Button>(R.id.buttonDot) as Button
        buttonPlusMinus = findViewById<Button>(R.id.buttonPlusMinus) as Button
    }

    fun NumberEvent(view: View)
    {
        if(this.isNewOp)
        {
            ShowNumber?.setText("")
        }
        this.isNewOp = false
        val buttonSelect= view as Button
        var buttonClickValue = this.ShowNumber?.text.toString()
        when(buttonSelect.id)
        {
            button0?.id->
            {
                buttonClickValue += "0"
            }
            button1?.id->
            {
                buttonClickValue += "1"
            }
            button2?.id->
            {
                buttonClickValue += "2"
            }
            button3?.id->
            {
                buttonClickValue += "3"
            }
            button4?.id->
            {
                buttonClickValue += "4"
            }
            button5?.id->
            {
                buttonClickValue += "5"
            }
            button6?.id->
            {
                buttonClickValue += "6"
            }
            button7?.id->
            {
                buttonClickValue += "7"
            }
            button8?.id->
            {
                buttonClickValue += "8"
            }
            button9?.id->
            {
                buttonClickValue += "9"
            }
            buttonDot?.id->
            {
                if(dot==false)
                {
                    buttonClickValue += "."
                }
                dot=true
            }
            buttonPlusMinus?.id->
            {
                buttonClickValue= "-$buttonClickValue"
            }
        }
        ShowNumber?.setText(buttonClickValue)
    }

    fun OperatorEvent(view: View)
    {
        val buttonSelect = view as Button
        when(buttonSelect.id)
        {
            buttonMul?.id->
            {
                operator="X"
            }
            buttonDiv?.id->
            {
                operator="÷"
            }
            buttonSub?.id->
            {
                operator="-"
            }
            buttonSum?.id->
            {
                operator="+"
            }
        }
        oldNumber= ShowNumber?.text.toString()
        isNewOp=true
        dot=false
    }

    fun EqualEvent(view: View)
    {
        val newNumber=ShowNumber?.text.toString()
        var finalNumber:Double?=null
        when(operator)
        {
            "X"->
            {
                finalNumber=oldNumber.toDouble() * newNumber.toDouble()
            }
            "÷"->
            {
                finalNumber=oldNumber.toDouble() / newNumber.toDouble()
            }
            "-"->
            {
                finalNumber=oldNumber.toDouble() - newNumber.toDouble()
            }
            "+"->
            {
                finalNumber=oldNumber.toDouble() + newNumber.toDouble()
            }
        }
        ShowNumber?.setText(finalNumber.toString())
        isNewOp=true
    }

    fun buttonPercentEvent(view: View)
    {
        val number=(ShowNumber?.text.toString().toDouble())/100
        ShowNumber?.setText(number.toString())
        isNewOp=true
    }

    fun buttonCleanEvent(view: View)
    {
        ShowNumber?.setText("")
        isNewOp=true
        dot=false
    }
}