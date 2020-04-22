package com.stetter.escambo.ui.adapter

import android.app.ActivityOptions
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.databinding.ItemProductImageBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.ui.core.explore.detail.DetailProductActivity
import com.stetter.escambo.ui.core.explore.detail.FullSizedImage
import java.lang.IllegalArgumentException

class ProductPhotoAdapter(val clickListener : PhotoListener?) : RecyclerView.Adapter<ProductPhotoAdapter.ViewHolder>() {

    var data = listOf<String>()
        set(value){
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : String = data[position]
        holder.bind(item,clickListener!!)
    }

    class ViewHolder private constructor(val binding : ItemProductImageBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : String, clickListener: PhotoListener){
            //Load User profile
            binding.photoUrl = itemView
            binding.clickListener = clickListener
            binding.executePendingBindings()


            val storage = FirebaseStorage.getInstance()
            if(item.length > 1){
                try{
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item}")
                    GlideApp.with(itemView.context)
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .into(binding.ivDetailProduct)

                }catch (e : IllegalArgumentException){
                    Log.e("UserAdapter", "Error : $e")
                }
            }

            binding.ivDetailProduct.setOnClickListener {
                val intent = Intent(itemView.context, FullSizedImage::class.java)
                val options = ActivityOptions.makeSceneTransitionAnimation(itemView.context as DetailProductActivity,it, "imageTransition")
                intent.putExtra("itemUrl", item)
                itemView.context.startActivity(intent, options.toBundle())
            }

        }

        companion object {
            fun from(parent : ViewGroup) : ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemProductImageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    //Todo : Expand image when clicked
    class PhotoListener(val clickListener: (photoView: View) -> Unit) {
        fun onClick(photo: View) = clickListener(photo)
    }




}
