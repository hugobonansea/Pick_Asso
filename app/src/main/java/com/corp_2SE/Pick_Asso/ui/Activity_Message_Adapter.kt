package com.corp_2SE.Pick_Asso.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.corp_2SE.Pick_Asso.Asso
import com.corp_2SE.Pick_Asso.Message
import com.corp_2SE.Pick_Asso.R
import com.corp_2SE.Pick_Asso.data.ui.login.ConversationListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*
import kotlin.collections.ArrayList



class Activity_Message_Adapter(private val listener: ConversationListener) : RecyclerView.Adapter<Activity_Message_Adapter.ViewHolder>() {




    private lateinit var auth: FirebaseAuth
    internal var storage: FirebaseStorage? = null
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    private var list: ArrayList<Message> = ArrayList()

    fun setData(list: ArrayList<Message>) {
        this.list =list
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Name_Sender = view.findViewById<TextView>(R.id.nomSender)
        val tv_titre= view.findViewById<TextView>(R.id.Publi_Titre)
        val tv_contenu= view.findViewById<TextView>(R.id.textContenu)
        val imgProfile= view.findViewById<ImageView>(R.id.picture_sender)
        val imgMess = view.findViewById<ImageView>(R.id.picture_illustrate)
        val tv_date= view.findViewById<TextView>(R.id.textDate)
        val tv_heure= view.findViewById<TextView>(R.id.textTime)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_message_fragment, parent, false)
        return ViewHolder(view)
        //return ViewHolder(
        //        LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        //)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = list[position]
        Log.d("adapter","adapter")


        //init Firebase
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("Asso")


        holder.tv_contenu.text = message.contenu
        holder.tv_date.text=message.date
        holder.tv_heure.text=message.heure


        holder.tv_titre.text = message.titre
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Log.e("cancel",error.message)
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children)
                {
                    var model = data.getValue((Asso::class.java))
                    if (model != null) {
                        if (model.id==message.sender){
                            holder.Name_Sender.text=model.username
                            return
                        }
                    }
                }
            }
        })

        var sender = message.sender

        val storageRef = storage?.reference
        val path = "images/profil/" + sender
        Log.i("path_calc", path)
        storageRef?.child(path)?.downloadUrl?.addOnSuccessListener {
            Log.i("download", it.toString())
            holder.imgProfile.load(it.toString())
        }

        val storageRefIma = storage?.reference
        val pathIma = "images/message/" + message.id
        Log.i("path_calc", pathIma)
        storageRefIma?.child(pathIma)?.downloadUrl?.addOnSuccessListener {
            Log.i("download", it.toString())
            holder.imgMess.load(it.toString())
        }

        holder.imgProfile.setOnClickListener {listener.onUserClickedMessageImage(message)}
        holder.itemView.setOnClickListener { listener.onUserClickedMessage(message) }
    }
    override fun getItemCount(): Int {
        return list.size
    }
}

fun ImageView.load(url: String) {
    Glide.with(context) //the context for the imageview calling it
        .load(url) // the url of the image to display
        .into(this) // this refer to the imageview where to put the loaded file
}