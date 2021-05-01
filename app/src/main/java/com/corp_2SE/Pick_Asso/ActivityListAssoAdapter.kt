package com.corp_2SE.Pick_Asso

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage


class ActivityListAssoAdapter (private val listener: AssoListListener) : RecyclerView.Adapter<ActivityListAssoAdapter.ViewHolder>() {

    internal var storage: FirebaseStorage? = null

    private var list: ArrayList<Asso> = ArrayList()

    fun setData(list: ArrayList<Asso>) {
        this.list =list
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val pict_asso = view.findViewById<ImageView>(R.id.picture_asso)
        val tv_asso= view.findViewById<TextView>(R.id.textNom)
        val tv_descr = view.findViewById<TextView>(R.id.text_Descrip)
        val but_fav= view.findViewById<Button>(R.id.button_fav)

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
        holder.tv_asso.text = asso.acronyme

        holder.but_fav.setOnClickListener{
        }

        holder.tv_descr.text=asso.description

        storage = FirebaseStorage.getInstance()
        val storageRef = storage?.reference
        val path = "images/profil/" + asso.id
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
