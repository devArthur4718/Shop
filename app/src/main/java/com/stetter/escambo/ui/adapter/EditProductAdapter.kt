package com.stetter.escambo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.databinding.ItemUploadProductBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.ui.core.add.AddProductViewModel


class EditProductAdapter : RecyclerView.Adapter<EditProductAdapter.ViewHolder>(){

    var data = listOf<String>()
        set(value){
            field = value
            notifyDataSetChanged()
        }
    val storage = FirebaseStorage.getInstance()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUrl = data[position]

        holder.bind(imgUrl,storage)
    }

    class ViewHolder private constructor(val binding : ItemUploadProductBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String, storage : FirebaseStorage){

            try{
                if(item.length > 1){
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item}")
                    GlideApp.with(itemView.context)
                        .asDrawable()
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.lvLoadedProduct)
                }

            }catch (e : IndexOutOfBoundsException){

            }



        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemUploadProductBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}