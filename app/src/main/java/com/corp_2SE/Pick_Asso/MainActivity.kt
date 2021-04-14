package com.corp_2SE.Pick_Asso


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.corp_2SE.Pick_Asso.data.ui.login.LoginActivity
import com.google.firebase.ktx.Firebase


data class User(val username: String, val email: String)

class MainActivity : AppCompatActivity() {


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
    }
    fun ImageView.load(url: String) {
        Glide.with(context) //the context for the imageview calling it
                .load(url) // the url of the image to display
                .into(this) // this refer to the imageview where to put the loaded file
    }

}