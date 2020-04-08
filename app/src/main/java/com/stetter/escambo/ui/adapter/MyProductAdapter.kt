package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemMyItemBinding
import com.stetter.escambo.net.models.SendProduct


class MyProductAdapter () : RecyclerView.Adapter<MyProductAdapter.ViewHolder>(){

    var data = listOf<SendProduct>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: MyProductAdapter.ViewHolder, position: Int) {
        val item : SendProduct = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemMyItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SendProduct){
            binding.tvMyitemTitle.text = item.product

        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemMyItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}