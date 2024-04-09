package com.example.lab_a3

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // btn = findViewById( R.id. )

//        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
//            when (checkedId) {
//                R.id.radioButton6 -> {
//                    food = 2.0
//                }
//
//                R.id.radioButton7 -> {
//                    food = -2.0
//                }
//
//                R.id.radioButton8 -> {
//                    food = 0.0
//                }
//            }
//        }


    }

//    fun something(view:View){
//
//    }
}