package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemUserAvatarBinding
import com.stetter.escambo.net.models.TopUser

class TopUserAdapter() : RecyclerView.Adapter<TopUserAdapter.ViewHolder>() {

    var data = listOf<TopUser>()
        set(value){
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : TopUser = data[position]
        holder.bind(item)
    }



    class ViewHolder private constructor(val binding : ItemUserAvatarBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : TopUser){

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