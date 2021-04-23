package com.corp_2SE.Pick_Asso.data.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.corp_2SE.Pick_Asso.PersonUser
import com.corp_2SE.Pick_Asso.R
import com.corp_2SE.Pick_Asso.ui.Activity_Message_Adapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main_home.*

class Message (var titre: String?="", var contenu : String?="", var sender: String?="")

data class Asso(val username: String? = null, val description: String? = null, val bureau: String? = null)

class MainHome : AppCompatActivity(), ConversationListener, AssoListener {

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReference2: DatabaseReference

    private val adapter2 = ActivityListAssoAdapter(this)
    private val adapter = Activity_Message_Adapter(this)




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)


        Log.d("test","entreé")

        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("test")
        databaseReference2 = database.getReference("Asso")

        getData2()
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
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerview)
                    recyclerView.adapter = adapter
                    adapter.setData(list)
                }
            }

        })
    }

    private fun getData2()
    {
        Log.d("test","getdata2")
        databaseReference2.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Asso","changedata2")
                var list = ArrayList<Asso>()
                for (data in snapshot.children)
                {
                    var model = data.getValue(Asso::class.java)
                    list.add(model as Asso)
                    //test à enlever
                    list.add(model as Asso)
                    list.add(model as Asso)
                }
                print(list)
                if (list.size>0)
                {
                    Log.d("test","adapter")
                    val recyclerView2: RecyclerView = findViewById(R.id.recyclerview2)
                    recyclerView2.adapter= adapter2
                    adapter2.setData(list)
                }
            }

        })
    }

    override fun onUserClicked(message: Message) {
        Toast.makeText(this, "You cliked on : ${message.titre}", Toast.LENGTH_LONG).show()
    }

    override fun onUserClickedAsso(asso: Asso) {
        Toast.makeText(this, "You cliked on : ${asso.username}", Toast.LENGTH_LONG).show()
    }

}

interface ConversationListener{
    fun onUserClicked(message: Message)
}

interface AssoListener{
    fun onUserClickedAsso(asso: Asso)
}