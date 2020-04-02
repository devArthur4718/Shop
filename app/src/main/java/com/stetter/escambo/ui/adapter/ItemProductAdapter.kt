package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemProductBinding
import com.stetter.escambo.net.models.Product

class ItemProductAdapter() : RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var data = listOf<Product>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder private constructor(val binding : ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Product){

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