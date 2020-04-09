package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemProductBinding
import com.stetter.escambo.net.models.Product

class ItemProductAdapter() : RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {

    var data = listOf<Product>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : Product = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Product){
                item.title
        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}