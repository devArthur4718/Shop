package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemRecentBinding
import com.stetter.escambo.net.models.RecentPost

class RecentProductAdapter () : RecyclerView.Adapter<RecentProductAdapter.ViewHolder>()  {

    var data = listOf<RecentPost>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : RecentPost = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemRecentBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : RecentPost){

        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRecentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}