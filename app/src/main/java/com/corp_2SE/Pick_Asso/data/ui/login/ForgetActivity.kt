package com.corp_2SE.Pick_Asso.data.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.corp_2SE.Pick_Asso.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)

        val mail = findViewById<EditText>(R.id.editTextEmailReset)

        val buttonForgetsent = findViewById<Button>(R.id.button_sent)
        buttonForgetsent.setOnClickListener {


            Firebase.auth.sendPasswordResetEmail(mail.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("reset", "Email sent.")
                        Toast.makeText(baseContext, "Email de Réinitilasition envoyée",
                            Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Log.d("reset", "Email not sent")
                        Toast.makeText(baseContext, "Pas de compte existant avec ce mail",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}