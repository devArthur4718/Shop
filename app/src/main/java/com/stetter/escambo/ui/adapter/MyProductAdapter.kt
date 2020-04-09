package com.stetter.escambo.ui.adapter


import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ItemMyItemBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.SendProduct
import java.lang.IndexOutOfBoundsException


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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : SendProduct = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemMyItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : SendProduct){
            binding.tvMyitemTitle.text = item.product
            binding.tvMyItemValue.text = item.value.toString()

            //Load image with glide - only the first one
            val storage = FirebaseStorage.getInstance()

            try{
                if(item.productUrl[0].length > 1){
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.productUrl[0]}")
                    GlideApp.with(itemView.context)
                        .asDrawable()
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.ivMyItem)

                }else{
                    binding.ivMyItem.setImageDrawable(itemView.context.resources.getDrawable(R.drawable.ic_young))
                }

            }catch (e : IndexOutOfBoundsException){
                Log.e("MyProduct", "Failed fetching product image: $e")
            }
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