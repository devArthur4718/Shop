package com.stetter.escambo.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.storage.FirebaseStorage
import com.stetter.escambo.R
import com.stetter.escambo.databinding.ItemProductBinding
import com.stetter.escambo.extension.CircularProgress
import com.stetter.escambo.glide.GlideApp
import com.stetter.escambo.net.models.ProductByLocation
import com.stetter.escambo.ui.core.explore.detail.DetailProductActivity
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ItemProductNextToMeAdapter : RecyclerView.Adapter<ItemProductNextToMeAdapter.ViewHolder>() {

    var data = listOf<ProductByLocation>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item : ProductByLocation = data[position]
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ItemProductBinding)
        : RecyclerView.ViewHolder(binding.root){

        fun bind(item : ProductByLocation){

            binding.tvNextProductAuthor.text = item.username
            binding.tvNextProduct.text =  item.distance.toString()
            var moneytext = item.value.toString().replaceRange(item.value.toString().length  -2, item.value.toString().length, "")

            try{
                var symbols = DecimalFormatSymbols()
                symbols.decimalSeparator = ','
                var moneyFormat = DecimalFormat("R$ ###,###,###,###", symbols)
                binding.tvNextProductValue.text = moneyFormat.format(moneytext.toDouble()).toString().replace(".", ",")
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
                        .into(binding.ivAuthorImage)

                }
                else{
                    binding.ivAuthorImage.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_young))
                }
            }catch (e : IllegalArgumentException){
                Log.e("NextProduct", "Error : ${e}")
                binding.ivAuthorImage.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_young))
            }

            if(item.productUrl[0].length > 1) {
                try{
                    val gsReference = storage.getReferenceFromUrl("gs://escambo-1b51d.appspot.com/${item.productUrl[0]}")
                    GlideApp.with(itemView.context)
                        .asDrawable()
                        .load(gsReference)
                        .placeholder(itemView.context?.CircularProgress())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(binding.ivNextProductImage)


                }catch (e : IllegalArgumentException){
                    Log.e("Recent Post", "Error $e")
                }catch (e : IndexOutOfBoundsException){
                    Log.e("Recent Post", "Error $e")
                }

            }

            binding.ivNextProductImage.setOnClickListener {
                val intent = Intent(itemView.context, DetailProductActivity::class.java)
                intent.putExtra("product", item)
                itemView.context.startActivity(intent)
            }
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