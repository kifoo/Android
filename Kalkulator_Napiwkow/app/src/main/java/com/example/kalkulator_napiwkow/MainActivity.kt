package com.example.kalkulator_napiwkow

import android.icu.text.NumberFormat
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private var costs = ""
    var percent = 5.0
    var food = 0.0
    var kelner = 0.0
    var own_percent = false

    private var etCosts: EditText? = null
    private var ratingBar: RatingBar? = null
    private var radioButton6: RadioButton? = null
    private var radioButton7: RadioButton? = null
    private var radioButton8: RadioButton? = null
    private var radioGroup: RadioGroup? = null
    private var percentRadioGroup: RadioGroup? = null
    private var ownPercentText: EditText? = null
    private var textNapiwek: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etCosts = findViewById(R.id.etCosts)
        ratingBar = findViewById(R.id.ratingBar)
        radioButton6 = findViewById(R.id.radioButton6)
        radioButton7 = findViewById(R.id.radioButton7)
        radioButton8 = findViewById(R.id.radioButton8)
        radioGroup = findViewById( R.id.foodGroup)
        percentRadioGroup = findViewById(R.id.percentRadioGroup)
        ownPercentText = findViewById(R.id.ownPercentText)
        textNapiwek = findViewById(R.id.textNapiwek)

        etCosts!!.addTextChangedListener(/* watcher = */ object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // nothing TODO, needed by interface
                costs = s.toString()

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

        this.ratingBar!!.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            // Handle the user's input here
            val rate = rating.toDouble()
            kelner = rate - 3
        }

        radioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButton6 -> {
                    food = 2.0
                }
                R.id.radioButton7 -> {
                    food = -2.0
                }
                R.id.radioButton8 -> {
                    food = 0.0
                }
            }
        }

        percentRadioGroup!!.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.percentButton5 -> {
                    percent = 5.0
                    ownPercentText?.visibility = View.INVISIBLE
                    own_percent = false
                }
                R.id.percentButton10 -> {
                    percent = 10.0
                    ownPercentText?.visibility = View.INVISIBLE
                    own_percent = false
                }
                R.id.percentButton15 -> {
                    percent = 15.0
                    ownPercentText?.visibility = View.INVISIBLE
                    own_percent = false
                }
                R.id.ownPercent -> {
                    ownPercentText?.visibility = View.VISIBLE
                    own_percent = true
                }
            }
        }

    }

    fun onClickCalculate(view: View){
        if (own_percent){
            if(ownPercentText?.text.isNullOrBlank()) {
                Toast.makeText(this, "Proszę wprowadź stawkę podstawową", Toast.LENGTH_SHORT).show()
                return
            }
            else{
                percent = ownPercentText?.text.toString().toDouble()
            }
        }
        if(costs.isEmpty()){
            Toast.makeText(this, "Proszę wprowadź cenę", Toast.LENGTH_SHORT).show()
        }
        else{
            val df = NumberFormat.getNumberInstance()
            df.maximumFractionDigits = 2
            val cost = costs.toDouble()
            val fullTip = cost*(percent + food*percent/5 + kelner*percent/5)/100
            val per = (percent + food*percent/5 + kelner*percent/5)
            val fullText = "Wartość napiwku \nProcent : $per%\nPełna wartość : $fullTip\nKwota do zapłacenia : ${df.format(fullTip)}"
            textNapiwek?.text = fullText
        }
    }
}
