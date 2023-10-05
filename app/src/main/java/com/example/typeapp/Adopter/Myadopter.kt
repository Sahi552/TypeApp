package com.example.typeapp.Adopter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.typeapp.R
import com.example.typeapp.models.User

class Myadopter (private val userlist : ArrayList<User>) : RecyclerView.Adapter<Myadopter.Myviewholder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myviewholder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view,
            parent,false)

        return  Myviewholder(itemview)
    }

    override fun getItemCount(): Int {

        return userlist.size
    }

    override fun onBindViewHolder(holder: Myviewholder, position: Int) {

        val currentitem = userlist[position]

        holder.Text.text = currentitem.name
        Glide.with(holder.itemView)
           .load(currentitem.imageUrl)
           .into(holder.Image)
    }

    class Myviewholder(itemview: View) : RecyclerView.ViewHolder(itemview){
        val Image : ImageView = itemview.findViewById(R.id.userprofile)
        val Text : TextView = itemview.findViewById(R.id.textname)
    }

}