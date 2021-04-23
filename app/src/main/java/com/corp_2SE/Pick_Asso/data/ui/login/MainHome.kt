package com.corp_2SE.Pick_Asso.data.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.corp_2SE.Pick_Asso.PersonUser
import com.corp_2SE.Pick_Asso.R
import com.corp_2SE.Pick_Asso.ui.Activity_Message_Adapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_home.*

class Message (var titre: String?="", var contenu : String?="", var sender: String?="")

class MainHome : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)


        Log.d("test","entreé")

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("test")

        getData()

        //setUpRecyclerView()
        //populateRecycler()

    }

 /*   private fun setUpRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
        recyclerView.adapter = adapter
    }
    private fun populateRecycler() {
        val messages = getData()
        adapter.setData(messages)
    }*/

    private fun getData()
    {
        Log.d("test","getdata")
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("test","changedata")
                var list = ArrayList<Message>()
                for (data in snapshot.children)
                {
                    var model = data.getValue(Message::class.java)
                    list.add(model as Message)
                    Log.d("test", model.contenu.toString())
                }
                print(list)
                if (list.size>0)
                {
                    Log.d("test","adapter")
                    var adapter = Activity_Message_Adapter(list)
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
                    recyclerView.adapter = adapter
                }
            }

        })
    }
}

interface Activity_Message_Adapter_Listener{
    fun onUserClicked(name: Message)
}