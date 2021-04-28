package com.corp_2SE.Pick_Asso

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.corp_2SE.Pick_Asso.data.ui.login.AssoListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class ActivityListAssoAdapter (private val listener: AssoListener) : RecyclerView.Adapter<ActivityListAssoAdapter.ViewHolder>() {

    private lateinit var auth: FirebaseAuth
    internal var storage: FirebaseStorage? = null

    private var list: ArrayList<Asso> = ArrayList()

    fun setData(list: ArrayList<Asso>) {
        this.list =list
        notifyDataSetChanged()
    }
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pict_asso = view.findViewById<ImageView>(R.id.picture_asso)
        val tv_asso= view.findViewById<TextView>(R.id.nom_asso)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_list_asso_fragment, parent, false)
        return ViewHolder(view)
        //return ViewHolder(
        //        LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false)
        //)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val asso = list[position]
        Log.d("adapter","adapter")
        holder.tv_asso.text = asso.username

        //var sender = asso.

        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance()
        val storageRef = storage?.reference
        //val path = "images/profil/" + sender
        val path = "images/profil/DSUqG24PttQ0gEah1TXmEfrZfjh1"
        Log.i("path_calc", path)
        storageRef?.child(path)?.downloadUrl?.addOnSuccessListener {
            Log.i("download", it.toString())
            holder.pict_asso.load(it.toString())
        }
        holder.itemView.setOnClickListener { listener.onUserClickedAsso(asso) }
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