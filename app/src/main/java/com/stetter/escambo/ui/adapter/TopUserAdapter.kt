package com.stetter.escambo.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.databinding.ItemUserAvatarBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.RegisterUser
import com.stetter.escambo.ui.core.profile.OtherUser
import java.lang.IllegalArgumentException

class TopUserAdapter() : RecyclerView.Adapter<TopUserAdapter.ViewHolder>() {

    var data = listOf<RegisterUser>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : RegisterUser = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemUserAvatarBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RegisterUser){

            //Load User profile
            val storage = FirebaseStorage.getInstance()

            if(item.photoUrl.length > 1){
                try{
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.photoUrl}")
                    GlideApp.with(itemView.context)
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .into(binding.ivUserPhoto)

                }catch (e : IllegalArgumentException){
                    Log.e("UserAdapter", "Error : $e")
                }
            }

            binding.ivUserPhoto.setOnClickListener {
                var intent = Intent(itemView.context, OtherUser::class.java)
                intent.putExtra("user", item)
                itemView.context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUserAvatarBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}