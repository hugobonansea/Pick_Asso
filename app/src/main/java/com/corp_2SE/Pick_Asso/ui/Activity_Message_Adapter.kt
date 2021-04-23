package com.corp_2SE.Pick_Asso.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.corp_2SE.Pick_Asso.R
import com.corp_2SE.Pick_Asso.data.ui.login.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import kotlin.collections.ArrayList

class Activity_Message_Adapter(var list : ArrayList<Message>) : RecyclerView.Adapter<Activity_Message_Adapter.ViewHolder>() {



    private lateinit var auth: FirebaseAuth
    internal var storage: FirebaseStorage? = null

    fun setData(list: ArrayList<Message>) {
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Name_Sender = view.findViewById<TextView>(R.id.nomSender)
        val tv_titre= view.findViewById<TextView>(R.id.Titre)
        val tv_contenu= view.findViewById<TextView>(R.id.textContenu)
        val imgProfile= view.findViewById<ImageView>(R.id.picture_sender)
        val imgMess = view.findViewById<ImageView>(R.id.picture_illustrate)

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
        holder.Name_Sender.text = message.titre
        holder.tv_contenu.text = message.contenu

        var sender = message.sender

        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance()
        val storageRef = storage?.reference
        val path = "images/profil/" + sender
        Log.i("path_calc", path)
        storageRef?.child(path)?.downloadUrl?.addOnSuccessListener {
            Log.i("download", it.toString())
            holder.imgProfile.load(it.toString())
        }
        //holder.itemView.setOnClickListener { listener.onUserClicked(message) }
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