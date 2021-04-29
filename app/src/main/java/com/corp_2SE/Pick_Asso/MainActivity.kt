package com.corp_2SE.Pick_Asso


import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


data class User(val username: String, val email: String)

class MainActivity : AppCompatActivity() {

    internal var storage : FirebaseStorage?=null
    val storageRef = storage?.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val imgUserProfile : ImageView = findViewById<ImageView>(R.id.imagetest)
        imgUserProfile.load("https://upload.wikimedia.org/wikipedia/commons/thumb/b/b6/Image_created_with_a_mobile_phone.png/250px-Image_created_with_a_mobile_phone.png") //must be a web link to a image


        val button = findViewById<Button>(R.id.sendData)
        button.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        button.setOnLongClickListener{
            Toast.makeText(this, "Long click detected", Toast.LENGTH_SHORT).show()
            return@setOnLongClickListener true
        }
        val promo = resources.getStringArray(R.array.num_promo)

        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item, promo
            )
            spinner.adapter = adapter
        }

    }
    fun ImageView.load(url: String) {
        Glide.with(context) //the context for the imageview calling it
                .load(url) // the url of the image to display
                .into(this) // this refer to the imageview where to put the loaded file
    }

}