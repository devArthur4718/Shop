package com.stetter.escambo.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ItemRecentBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.Product
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class RecentProductAdapter () : RecyclerView.Adapter<RecentProductAdapter.ViewHolder>()  {

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

    class ViewHolder private constructor(val binding : ItemRecentBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Product){
            binding.tvRecentItemTitle.text = item.product
            binding.tvRecentUserName.text = item.username
            var moneytext = item.value.toString().replaceRange(item.value.toString().length  -2, item.value.toString().length, "")

            try{
                var symbols = DecimalFormatSymbols()
                symbols.decimalSeparator = ','
                var moneyFormat = DecimalFormat("R$ ###,###,###,###", symbols)
                binding.tvValueRecent.text = moneyFormat.format(moneytext.toDouble()).toString().replace(".", ",")
            }catch (e : Exception){
                Log.d("ProductAdapter", "Error: $e")
            }

            //Load image with glide - only the first one
            val storage = FirebaseStorage.getInstance()

            try{
                if(item.userPhoto.length > 0){

                    val gsReferencePhoto = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.userPhoto}")
                    GlideApp.with(itemView.context)
                        .asDrawable()
                        .load(gsReferencePhoto)
                        .placeholder(itemView.context?.CircularProgress())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.ivUserTop)

                }
                else{
                    binding.ivUserTop.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_young))
                }
                if(item.productUrl[0].length > 1){
                    try{
                        val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.productUrl[0]}")
                        GlideApp.with(itemView.context)
                            .asDrawable()
                            .load(gsReference)
                            .placeholder(itemView.context?.CircularProgress())
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .into(binding.ivProductRecent)


                    }catch (e : IllegalArgumentException){
                        Log.e("Recent Post", "Error $e")
                    }
                }
            }catch (e : IndexOutOfBoundsException){
                Log.e("MyProduct", "Failed fetching product image: $e")
            }

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