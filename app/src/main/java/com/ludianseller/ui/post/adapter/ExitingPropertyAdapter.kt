package com.ludianseller.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.databinding.ItemPropertyBinding
import com.ludianseller.models.CalenderPropertyModel



class ExitingPropertyAdapter (private val mContext: Context,
                           var arrayList: ArrayList<CalenderPropertyModel.Property>?,
                           private val listener : OnItemListener
) : RecyclerView.Adapter<ExitingPropertyAdapter.MyViewHolder>()  {

    class MyViewHolder(var binding: ItemPropertyBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemPropertyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_property, parent, false
        )


        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.itemView.setOnClickListener {
            listener.onItemClick(position,arrayList!![position],true)
        }


        if(arrayList!![position].imageUrls.isNotEmpty()) {
            holder.binding.tvName.text = arrayList!![position].title
            holder.binding.tvAddress.text = arrayList!![position].address
            holder.binding.tvPrice.text =  arrayList!![position].price + " SAR "  + ",7"
            holder.binding.tvRating.text =  arrayList!![position].ratting_avg

            Glide.with(mContext)
                .load(arrayList!![position].imageUrls[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
                .error(com.denzcoskun.imageslider.R.drawable.default_error)
                .centerInside()
                .into(holder.binding.ivImg)
        }




    }


    fun notifyAdapter(calenderArrayList: ArrayList<CalenderPropertyModel.Property>) {
        arrayList = calenderArrayList
        notifyDataSetChanged()
    }



    interface OnItemListener {
        fun onItemClick(mainPosition: Int, data : CalenderPropertyModel.Property, check : Boolean)
    }
}