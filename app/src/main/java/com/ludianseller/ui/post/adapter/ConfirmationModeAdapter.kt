package com.ludianseller.ui.post.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.ItemPlaceBinding
import com.ludianseller.models.ConfirmationModeModel



class ConfirmationModeAdapter (
    private val mContext: Context,
    var arrayList: ArrayList<ConfirmationModeModel.Property>?
    , val listener : OnConfirmationListener
) : RecyclerView.Adapter<ConfirmationModeAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemPlaceBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_place, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = arrayList!![position].name
        holder.binding.tvDescription.text = arrayList!![position].description

        Glide.with(mContext).load(arrayList!![position].image).override(50,50).into(holder.binding.ivImg)
        if(arrayList!![position].check) {
            holder.binding.tvName.setTextColor(mContext.getColor(R.color.color_primary))
            holder.binding.rlMain.setBackgroundResource(R.drawable.rounded_corner_stroke_10)
            holder.binding.ivImg.setColorFilter(mContext.getColor(R.color.color_primary), PorterDuff.Mode.SRC_ATOP)


        }
        else {
            holder.binding.tvName.setTextColor(mContext.getColor(R.color.color_gray_light1))
            holder.binding.rlMain.setBackgroundResource(R.drawable.rounded_gray_white10_bg)
            holder.binding.ivImg.setColorFilter(mContext.getColor(R.color.color_gray_light1), PorterDuff.Mode.SRC_ATOP)

        }

        holder.itemView.setOnClickListener {
            for (i in 0 until arrayList!!.size) {
                arrayList!![i].check = false
            }
            arrayList!![position].check = true
            listener.onConfirm(arrayList!!,position)
        }
    }

    interface   OnConfirmationListener{
        fun onConfirm(propertyList: ArrayList<ConfirmationModeModel.Property>, position: Int)

    }


    fun notifyAdapter(propertyList: ArrayList<ConfirmationModeModel.Property>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }


}