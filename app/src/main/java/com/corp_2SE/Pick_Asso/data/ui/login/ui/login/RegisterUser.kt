package com.corp_2SE.Pick_Asso.data.ui.login.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import com.corp_2SE.Pick_Asso.R

class RegisterUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        val promo = resources.getStringArray(R.array.num_promo)


        /*val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, promo
            )
            spinner.adapter = adapter
        }*/

        val sw1 = findViewById<Switch>(R.id.switch_mode)

        sw1.setOnCheckedChangeListener { _ , isChecked ->

            val intent = Intent(this, Register::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        }
    }
}