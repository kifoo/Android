package com.example.lab_a3

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity(){
    private lateinit var etLocationX: EditText
    private lateinit var etLocationY: EditText
    private lateinit var btnSettings: Button
    private lateinit var btnPhoto: Button
    private lateinit var imageView: ImageView
    private lateinit var btnMap: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        etLocationX = findViewById(R.id.etLocationX)
        etLocationY = findViewById(R.id.etLocationY)
        btnSettings = findViewById(R.id.btnSettings)
        btnMap = findViewById(R.id.btnMap)
        btnPhoto = findViewById(R.id.btnPhoto)
        imageView = findViewById(R.id.imageView)

        if(!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            btnPhoto.visibility = View.INVISIBLE
            imageView.visibility = View.INVISIBLE
        }
    }
    fun buttonGoToSettings(view: View){
        val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
        try{
            startActivity(intent)
        }
        catch(e: ActivityNotFoundException){
            Toast.makeText(this, "Imposible to open settings", Toast.LENGTH_SHORT).show()
        }
    }

    fun showMap(view: View) {
        if(etLocationX.text.isEmpty() or etLocationY.text.isEmpty()){
            Toast.makeText(this, "Provide parameters for latitude and longitude", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            val x = etLocationX.text.toString()
            val y = etLocationY.text.toString()

            val geoLocation = Uri.parse("geo:$x,$y")
            val intent = Intent(Intent.ACTION_VIEW, geoLocation)
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Impossible to open maps", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private val REQUEST_IMAGE_CAPTURE = 1

    fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager)!= null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        }
    }

    fun onBtnPhotoClickAction(view: View) {
        dispatchTakePictureIntent()
    }
}
