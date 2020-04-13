package com.stetter.escambo.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.stetter.escambo.databinding.ItemUploadProductBinding
import com.stetter.escambo.ui.core.add.AddProductViewModel

class UploadItemAdapter() : RecyclerView.Adapter<UploadItemAdapter.ViewHolder>(){
    var data = listOf<ProductCard>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    lateinit var viewModel : AddProductViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item  : ProductCard =  data[position]
        holder.bind(item,viewModel, position)
    }


    class ViewHolder private constructor(val binding : ItemUploadProductBinding)
        : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ProductCard, vm : AddProductViewModel, position : Int){

            binding.ivPickPhoto.setOnClickListener {
                vm.pickPhoto()
            }

            item.bitmap?.let {
                binding.lvLoadedProduct.setImageBitmap(it)
                binding.groupPickPhoto.visibility = View.INVISIBLE
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

