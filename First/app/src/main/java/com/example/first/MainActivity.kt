package com.example.first

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast


class MainActivity : Activity() {
    //class variables
    private var context: Context? = null
    private val duration = Toast.LENGTH_SHORT
    private var chosenColor: String? = null

    //Matching GUI controls to Java objects
    private var btnApply: Button? = null
    private var btnExit: Button? = null
    private var txtColorSelected: EditText? = null
    private var txtHelloBox: TextView? = null
    private var txtSpyBox: TextView? = null
    private var myScreen: LinearLayout? = null
    private val PREFNAME = "myPrefFile1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //display the main screen
        setContentView(R.layout.activity_main)

        //wiring GUI controls and matching Java objects
        txtColorSelected = findViewById<View>(R.id.editText) as EditText
        btnApply = findViewById<View>(R.id.apply_button) as Button
        btnExit = findViewById<View>(R.id.home_button) as Button
        txtHelloBox = findViewById<View>(R.id.text_hello_view) as TextView
        txtSpyBox = findViewById<View>(R.id.text_space_view) as TextView
        myScreen = findViewById<View>(R.id.main) as LinearLayout

        //set GUI listeners, watchers,...
        btnExit!!.setOnClickListener { finish() }

        //observe (text) changes made to EditText box (color selection)
        txtColorSelected!!.addTextChangedListener(/* watcher = */ object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // nothing TODO, needed by interface
                chosenColor = s.toString().lowercase()
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // nothing TODO, needed by interface
            }

            override fun afterTextChanged(s: Editable) {
                //set background to selected color
                // val chosenColor = s.toString().lowercase()
                // txtSpyBox!!.text = chosenColor
                // setBackgroundColor(chosenColor, myScreen)
            }
        })

        // Apply button click listener
        btnApply!!.setOnClickListener {
            setBackgroundColor(chosenColor, myScreen)
        }

        //show the current state's name
        context = applicationContext
        Toast.makeText(context, "onCreate", duration).show()
    } //onCreate

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(context, "onDestroy", duration).show()
    }

    override fun onPause() {
        super.onPause()
        //save state data (background color) for future use
        val chosenColor = txtSpyBox!!.getText().toString()
        //??? saveStateData(chosenColor);
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
        //if appropriate, change background color to chosen value
        //??? updateMeUsingSavedStateData();
        Toast.makeText(context, "onStart", duration).show()
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(context, "onStop", duration).show()
    }

    // ///////////////////////////////////////////////////////////////////
    private fun setBackgroundColor(chosenColor: String?, myScreen: LinearLayout?) {
        if (chosenColor!!.contains("black")) myScreen!!.setBackgroundColor(Color.BLACK)
        if (chosenColor.contains("white")) myScreen!!.setBackgroundColor(Color.WHITE)
        if (chosenColor.contains("red")) myScreen!!.setBackgroundColor(Color.RED)
        if (chosenColor.contains("green")) myScreen!!.setBackgroundColor(Color.GREEN)
        if (chosenColor.contains("blue")) myScreen!!.setBackgroundColor(Color.BLUE)
        if (chosenColor.contains("yellow")) myScreen!!.setBackgroundColor(Color.YELLOW)
        if (chosenColor.contains("gray")) myScreen!!.setBackgroundColor(Color.GRAY)
        if (chosenColor.contains("magenta")) myScreen!!.setBackgroundColor(Color.MAGENTA)
    } //setBackgroundColor

    private fun saveStateData(chosenColor: String) {
        //this is a little <key,value> table permanently kept in memory
        val myPrefContainer = getSharedPreferences(PREFNAME, MODE_PRIVATE)
        //pair <key,value> to be stored represents our 'important' data
        val myPrefEditor = myPrefContainer.edit()
        val key = "chosenBackgroundColor"
        val value = txtSpyBox!!.getText().toString()
        myPrefEditor.putString(key, value)
        myPrefEditor.commit()
    } //saveStateData

    private fun updateMeUsingSavedStateData() {
        // (in case it exists) use saved data telling backg color
        val myPrefContainer = getSharedPreferences(PREFNAME, MODE_PRIVATE)
        val key = "chosenBackgroundColor"
        val defaultValue = "white"
        if (myPrefContainer != null &&
            myPrefContainer.contains(key)
        ) {
            val color = myPrefContainer.getString(key, defaultValue)
            setBackgroundColor(color, myScreen)
        }
    } //updateMeUsingSavedStateData
} //Activity

