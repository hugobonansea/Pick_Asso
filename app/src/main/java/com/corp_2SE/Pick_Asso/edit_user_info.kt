package com.corp_2SE.Pick_Asso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.corp_2SE.Pick_Asso.data.ui.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

data class PersonUser(val surname: String? = null,val name: String? = null, val promo : String)

class RegisterUserInfo : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user_info)

        auth = FirebaseAuth.getInstance();

        val nom = findViewById<EditText>(R.id.text_surname)
        val prenom = findViewById<EditText>(R.id.text_name)
        val promo = findViewById<Spinner>(R.id.spinner)
        val butt_add_info = findViewById<Button>(R.id.button_save)

        butt_add_info.setOnClickListener {
            if (nom.text.toString()=="" || prenom.text.toString()=="")
            {
                val builder= MaterialAlertDialogBuilder(this)
                with(builder){
                    setTitle("Création compte utilisateur")
                    setMessage("Vous n'avez pas renseigné votre nom et/ou votre prénom.")
                    setPositiveButton("Continuer",  { dialogInterface, i ->
                        addinfouser()
                    })
                    setNegativeButton("Annuler", { dialogInterface, i ->

                    })
                    show()
                }
            }
            else{
                addinfouser()
            }
        }
    }

    private fun addinfouser() {

        val nom = findViewById<EditText>(R.id.text_surname)
        val prenom = findViewById<EditText>(R.id.text_name)
        val promo = findViewById<Spinner>(R.id.spinner)


        val user = auth.currentUser

        val databaseref = database.getReference("User")


        val Person_create = PersonUser( nom.text.toString(), prenom.text.toString(), promo.getSelectedItem().toString())

        databaseref.child(user.uid).setValue(Person_create)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}