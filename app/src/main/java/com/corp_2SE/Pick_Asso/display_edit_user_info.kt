package com.corp_2SE.Pick_Asso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_display_edit_user_info.*

data class User(val surname: String? = null,
                val name: String? = null,
                val promo : String? = null)

class display_edit_user_info : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_edit_user_info)


        auth = FirebaseAuth.getInstance();
        val user = auth.currentUser

        val nom = findViewById<EditText>(R.id.text_surname)
        val prenom = findViewById<EditText>(R.id.text_name)
        val promo = findViewById<Spinner>(R.id.spinner)
        val butt_modify_info = findViewById<Button>(R.id.button_modify)

    }

    private fun getData()
    {
        Log.d("test","getdata")
        val databaseref = database.getReference("User")

        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val nom = snapshot.getValue(String::class.java)
                text_surname.text = nom.toString()
            }

        })

        databaseref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val prenom = snapshot.getValue(String::class.java)
                text_name.text = prenom.toString()
            }

        })

        databaseref.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val promotion = snapshot.getValue(String::class.java)
                text_promo.text = promotion.toString()
            }

        })
    }
}