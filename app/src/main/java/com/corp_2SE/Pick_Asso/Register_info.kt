package com.corp_2SE.Pick_Asso

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.corp_2SE.Pick_Asso.data.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class Register_info: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    internal var storage: FirebaseStorage? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val imgUserProfile : ImageView = findViewById<ImageView>(R.id.imageProfil)
        val edittextAsso = findViewById<EditText>(R.id.TextNomAsso)
        val textnamePres = findViewById<EditText>(R.id.editTextPresName)
        val textnameSec = findViewById<EditText>(R.id.editTextSecName)
        val textnameVice = findViewById<EditText>(R.id.editTextViceName)
        val textnameTres = findViewById<EditText>(R.id.editTextTresName)
        val textdescription = findViewById<EditText>(R.id.editTextTextMultiLine)
        val add_info = findViewById<Button>(R.id.buttonEnregistrer)


        val nom_asso_prec=intent.getStringExtra("Name_asso")

        if (nom_asso_prec!=null){
            edittextAsso.setText(nom_asso_prec)
        }


        auth = FirebaseAuth.getInstance();
        val user = auth.currentUser

        storage = FirebaseStorage.getInstance()
        val storageRef = storage?.reference
        val path = "images/profil/"+ (user!!.uid)
        Log.i("path_calc",path)
        storageRef?.child(path)?.downloadUrl?.addOnSuccessListener {
            Log.i("download",it.toString())
            imgUserProfile.load(it.toString())
        }?.addOnFailureListener {
            // Handle any errors
        }

        add_info.setOnClickListener {
            addinfo()
        }


    }

    private fun addinfo() {
        val textAsso = findViewById<EditText>(R.id.name_asso)
        val textnamePres = findViewById<EditText>(R.id.editTextPresName)
        val textnameSec = findViewById<EditText>(R.id.editTextSecName)
        val textnameVice = findViewById<EditText>(R.id.editTextViceName)
        val textnameTres = findViewById<EditText>(R.id.editTextTresName)
        val textdescription = findViewById<EditText>(R.id.editTextTextMultiLine)

        //applocation sur firebase

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun ImageView.load(url: String) {
        Glide.with(context) //the context for the imageview calling it
                .load(url) // the url of the image to display
                .into(this) // this refer to the imageview where to put the loaded file
    }

    override fun onBackPressed() {
    }


}