package com.corp_2SE.Pick_Asso

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage


class Register_info: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    internal var storage: FirebaseStorage? = null

    val database = FirebaseDatabase.getInstance()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_info)

        val imgUserProfile : ImageView = findViewById<ImageView>(R.id.imageProfil)
        val edittextAsso = findViewById<EditText>(R.id.TextNomAsso)
        val textdescription = findViewById<EditText>(R.id.editTextDescription)
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
            if(edittextAsso.text.toString()=="" || textdescription.text.toString()==""){
                val builder= MaterialAlertDialogBuilder(this)
                with(builder){
                    setTitle("Création compte association")
                    setMessage("Vous n'avez pas renseigné toutes les informations.")
                    setPositiveButton("Continuer",  { dialogInterface, i ->
                        addinfo()
                    })
                    setNegativeButton("Annuler", { dialogInterface, i ->

                    })
                    show()
                }
            }
            else{
                addinfo()
            }
        }


    }

    private fun addinfo() {

        val textAsso = findViewById<EditText>(R.id.TextNomAsso)
        val textdescription = findViewById<EditText>(R.id.editTextDescription)
        val acronyme = findViewById<EditText>(R.id.editTextAcronyme)
        val bureau = findViewById<EditText>(R.id.editTextBureau)
        val campus = findViewById<EditText>(R.id.editTextCampus)

        val user = auth.currentUser

        val databaseref = database.getReference("Asso")


        val Asso_create = Asso(user.uid.toString(),textAsso.text.toString(),acronyme.text.toString(),textdescription.text.toString(),bureau.text.toString(),campus.text.toString())

        databaseref.child(user.uid).setValue(Asso_create)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun ImageView.load(url: String) {
        Glide.with(context) //the context for the imageview calling it
                .load(url) // the url of the image to display
                .into(this) // this refer to the imageview where to put the loaded file
    }

    override fun onBackPressed() {
    }


}