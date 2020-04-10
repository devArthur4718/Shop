package com.stetter.escambo.ui.adapter

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
import com.stetter.escambo.net.models.Product
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols


class MyProductAdapter () : RecyclerView.Adapter<MyProductAdapter.ViewHolder>(){

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

    class ViewHolder private constructor(val binding : ItemMyItemBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Product){
            binding.tvMyitemTitle.text = item.product
            var moneytext = item.value.toString().replaceRange(item.value.toString().length  -2, item.value.toString().length, "")

            try{
                var symbols = DecimalFormatSymbols()
                symbols.decimalSeparator = ','
                var moneyFormat = DecimalFormat("R$ ###,###,###,###", symbols)
                binding.tvMyItemValue.text = moneyFormat.format(moneytext.toDouble()).toString().replace(".", ",")
            }catch (e : Exception){
                Log.d("ProductAdapter", "Error: $e")
            }

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