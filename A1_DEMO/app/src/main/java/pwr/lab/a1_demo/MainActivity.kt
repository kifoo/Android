package pwr.lab.a1_demo

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // przygotowanie UI / wczytanie wskazanego pliku XML: "activity_main.xml"
        setContentView(R.layout.activity_main)

        // najprostsza wersja polecenia ustawiajacego OnClickListener w kodzie / tzw. lambda
        findViewById<Button>(R.id.button1).setOnClickListener {
            println("Przykladowy wydruk kontrolny X")
        }

        findViewById<Button>(R.id.button4).setOnClickListener {
            println("Przykladowy wydruk kontrolny 4")
            Click_button4();
        }

        /*
        // pelniejszy zapis tej samej operacji co powyżej
        val przycisk1:Button = findViewById<Button>(R.id.button1)
        if( przycisk1 != null) {
            przycisk2.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    println("Przykladowy wydruk kontrolny XXX")
                }
            })
        }
        */
    }

    // akcja przypisana do przycisku (2) w pliku XML, za pomoca atrybutu "android:onClick"
    fun kliknietoPrzycisk2(view: View) {
        println("Przykladowy wydruk kontrolny 2")

        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 22")

        Toast.makeText(applicationContext, "Przykladowy wydruk kontrolny 222", Toast.LENGTH_SHORT).show()
    }


    // akcja przypisana do przycisku (3) w pliku XML, za pomoca atrybutu "android:onClick"
    fun kliknietoPrzycisk3(view: View) {
        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 3")

        val podloze: View = findViewById(R.id.main)
        if( podloze.background is ColorDrawable ) {
            val kolor = (podloze.background as ColorDrawable).color
            if( kolor != Color.BLUE ) {
                podloze.setBackgroundColor(Color.BLUE)       //np. Color.parseColor("#770000ff") )
                findViewById<TextView>(R.id.textViewTytul).text = "niebieski kolor tła"
            } else {
                podloze.setBackgroundColor(Color.YELLOW)
                findViewById<TextView>(R.id.textViewTytul).text = "żółty kolor tła"
            }
        } else {
            podloze.setBackgroundColor(Color.LTGRAY)
        }
    }

    fun Click_button4(){

        findViewById<Button>(R.id.button4).visibility = View.INVISIBLE
        findViewById<Button>(R.id.button5).visibility = View.VISIBLE
    }

    fun Click_button5(){
        Log.i("MojeWydruki", "Przykladowy wydruk kontrolny 4")

        findViewById<Button>(R.id.button4).visibility = View.VISIBLE
        findViewById<Button>(R.id.button5).visibility = View.INVISIBLE
     }

}