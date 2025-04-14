package com.ludianseller.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.databinding.ItemHomeBinding
import com.ludianseller.models.PropertyModel


class HomeAdapter (
    private val mContext: Context,
                          private var arrayList: ArrayList<PropertyModel.Property>?,
                          private var listener : OnPropertyListener
) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemHomeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_home, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = arrayList!![position].title
        holder.binding.tvAddress.text = arrayList!![position].address
        holder.binding.tvPrice.text = "$" + arrayList!![position].price + ",7"

        Glide.with(mContext)
            .load(arrayList!![position].propertyImages[0].image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
            .error(com.denzcoskun.imageslider.R.drawable.default_error)
            .centerInside()
            .into(holder.binding.ivImg)


        holder.itemView.setOnClickListener {
          listener.onPropertyClick(arrayList!![position],position,"")
        }
    }

     interface OnPropertyListener {
        fun onPropertyClick(model: PropertyModel.Property, position: Int, type: String)

    }

    fun notifyAdapter(propertyList: ArrayList<PropertyModel.Property>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }

}