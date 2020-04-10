package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemProductMessageBinding
import com.stetter.escambo.net.models.ProductMock

class RecenteMessageAdapter() : RecyclerView.Adapter<RecenteMessageAdapter.ViewHolder>() {

    var data = listOf<ProductMock>()
        set(value){
            field = value
            notifyDataSetChanged()
        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: RecenteMessageAdapter.ViewHolder, position: Int) {
        val item : ProductMock = data[position]
        holder.bind(item)
    }




    class ViewHolder private constructor(val binding : ItemProductMessageBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ProductMock){

        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductMessageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}