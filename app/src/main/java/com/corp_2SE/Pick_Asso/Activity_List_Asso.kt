package com.corp_2SE.Pick_Asso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class Activity_List_Asso : AppCompatActivity() , AssoListListener{

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference2: DatabaseReference

    private val adapter2 = ActivityListAssoAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_asso)

        val mBottomNavigationView=findViewById<BottomNavigationView>(R.id.nav_view)
        mBottomNavigationView.menu.findItem(R.id.navigation_Asso).setChecked(true)


        val mBottomNavigationItemSelectedListener=  BottomNavigationView.OnNavigationItemSelectedListener{ item ->
            when (item.itemId){
                R.id.navigation_Asso ->{
                    mBottomNavigationView.menu.findItem(R.id.navigation_Asso).setChecked(true)
                    Log.d("navigationbar","Asso click")
                    true
                }
                R.id.navigation_calendrier ->{
                    mBottomNavigationView.menu.findItem(R.id.navigation_calendrier).setChecked(true)
                    Log.d("navigationbar","Calendrier click")
                    true
                }
                R.id.navigation_publication ->{
                    mBottomNavigationView.menu.findItem(R.id.navigation_publication).setChecked(true)
                    val intent = Intent(this, MainHome::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    Log.d("navigationbar","Publication click")
                    true
                }
                else -> false
            }}

        mBottomNavigationView.setOnNavigationItemSelectedListener(mBottomNavigationItemSelectedListener)

        database = FirebaseDatabase.getInstance()
        databaseReference2 = database.getReference("Asso")

        getDataAsso()

    }

    private fun getDataAsso()
    {
        Log.d("test","getdata2")
        databaseReference2.addValueEventListener(object : ValueEventListener {
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
                }
                print(list)
                if (list.size>0)
                {
                    Log.d("test","adapter")
                    val recyclerView: RecyclerView = findViewById(R.id.recyclerviewmessage)
                    recyclerView.adapter= adapter2
                    adapter2.setData(list)
                }
            }

        })
    }

    override fun onUserClickedAsso(asso: Asso) {
        Toast.makeText(this, "You cliked on : ${asso.id}", Toast.LENGTH_LONG).show()
    }
}

interface AssoListListener{
    fun onUserClickedAsso(asso: Asso)
}