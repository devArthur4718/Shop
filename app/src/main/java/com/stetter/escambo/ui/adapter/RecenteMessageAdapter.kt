package com.stetter.escambo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.databinding.ItemProductMessageBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.ProductInterest


class RecenteMessageAdapter() : RecyclerView.Adapter<RecenteMessageAdapter.ViewHolder>() {

    var data = listOf<ProductInterest>()
        set(value){
            field = value
            notifyDataSetChanged()
        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: RecenteMessageAdapter.ViewHolder, position: Int) {
        val item : ProductInterest = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemProductMessageBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ProductInterest){
            binding.tvInterestProductName.text = item.productName
            val storage = FirebaseStorage.getInstance()
            if(item.productPhoto.length > 1) {
                try{
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.productPhoto}")
                    GlideApp.with(itemView.context)
                        .asDrawable()
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.ivInterestImage)


                }catch (e : IllegalArgumentException){
                    Log.e("Recent Post", "Error $e")
                }catch (e : IndexOutOfBoundsException){
                    Log.e("Recent Post", "Error $e")
                }

            }

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