package com.corp_2SE.Pick_Asso


import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ktx.Firebase


data class User(val username: String, val email: String)

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val button = findViewById<Button>(R.id.sendData)
        button.setOnClickListener {
            val user = User("user_test", "email@test.fr")
            Log.i("test" , user.username)
            val editText = findViewById<EditText>(R.id.input_text)
            val tap =editText.text
            println("test")
            println("try")
            println(tap)
        }

    }
}