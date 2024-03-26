package com.example.lab1

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout

class MainActivity : AppCompatActivity() {
    private var context: Context? = null
    private val duration = Toast.LENGTH_SHORT

    private var txtColorSelected: EditText? = null
    private var chosenColor: String? = null

    private var btn3: Button? = null
    private var myScreen: ConstraintLayout? = null

    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //display the main screen
        setContentView(R.layout.activity_main)

        txtColorSelected = findViewById<View>(R.id.editText) as EditText
        btn3 = findViewById<View>(R.id.button3) as Button
        myScreen = findViewById<View>(R.id.main) as ConstraintLayout

        findViewById<Button>(R.id.button1).setOnClickListener {
            finish()
        }

        txtColorSelected!!.addTextChangedListener(/* watcher = */ object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                chosenColor = s.toString().lowercase()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // nothing TODO, needed by interface
            }

            override fun afterTextChanged(s: Editable) {
                // nothing TODO, needed by interface
            }
        })

        btn3!!.setOnClickListener {
            Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 33")
            setBackgroundColor(chosenColor, myScreen)
            findViewById<View>(R.id.editText).visibility = View.INVISIBLE
            findViewById<Button>(R.id.button2).visibility = View.VISIBLE

        }
        context = applicationContext
        Toast.makeText(context, "onCreate", duration).show()

    }

    fun Click_button2(view: View){
        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 22")
        findViewById<Button>(R.id.button2).visibility = View.INVISIBLE
        findViewById<View>(R.id.editText).visibility = View.VISIBLE
    }

    fun Click_button4(view: View){
        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 44")
        findViewById<Button>(R.id.button4).visibility = View.INVISIBLE
        findViewById<Button>(R.id.button5).visibility = View.VISIBLE
    }

    fun Click_button5(view: View){
        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 55")
        findViewById<Button>(R.id.button4).visibility = View.VISIBLE
        findViewById<Button>(R.id.button5).visibility = View.INVISIBLE
    }

    /////////////////////////////////////////////////////////////////////
    private fun setBackgroundColor(chosenColor: String?, myScreen: ConstraintLayout?) {
        if (chosenColor!!.contains("black")) myScreen!!.setBackgroundColor(Color.BLACK)
        if (chosenColor.contains("white")) myScreen!!.setBackgroundColor(Color.WHITE)
        if (chosenColor.contains("red")) myScreen!!.setBackgroundColor(Color.RED)
        if (chosenColor.contains("green")) myScreen!!.setBackgroundColor(Color.GREEN)
        if (chosenColor.contains("blue")) myScreen!!.setBackgroundColor(Color.BLUE)
        if (chosenColor.contains("yellow")) myScreen!!.setBackgroundColor(Color.YELLOW)
        if (chosenColor.contains("gray")) myScreen!!.setBackgroundColor(Color.GRAY)
        if (chosenColor.contains("magenta")) myScreen!!.setBackgroundColor(Color.MAGENTA)
    } //setBackgroundColor

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(context, "onDestroy", duration).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(context, "onPause", duration).show()
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(context, "onRestart", duration).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(context, "onResume", duration).show()
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(context, "onStart", duration).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(context, "onStop", duration).show()
    }
}