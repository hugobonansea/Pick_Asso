package com.corp_2SE.Pick_Asso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class Register_info: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val imgUserProfile : ImageView = findViewById<ImageView>(R.id.imageProfil)

        val name_asso=intent.getStringExtra("Name_asso")
        val user = auth.currentUser


        imgUserProfile.load("gs://se-b6bdf.appspot.com/images/profil/"+ (user!!.uid))



    }

    fun ImageView.load(url: String) {
        Glide.with(context) //the context for the imageview calling it
                .load(url) // the url of the image to display
                .into(this) // this refer to the imageview where to put the loaded file
    }
}