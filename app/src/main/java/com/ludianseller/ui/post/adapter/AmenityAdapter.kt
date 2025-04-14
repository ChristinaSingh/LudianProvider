package com.ludianseller.ui.post.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.ItemAmenityBinding
import com.ludianseller.models.AmenitiesModel
import com.ludianseller.models.PlaceModel
import com.ludianseller.utils.SharedPrf


class AmenityAdapter (
    private val mContext: Context,
                          var arrayList: ArrayList<AmenitiesModel.Property>?
    , val listener : OnAmenitiesListener,
) : RecyclerView.Adapter<AmenityAdapter.MyViewHolder>() {

    private val sharedPrf by lazy { SharedPrf(mContext) }


    class MyViewHolder(var binding: ItemAmenityBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemAmenityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_amenity, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if(sharedPrf.getStoredTag(SharedPrf.LANGUAGE)=="en")  holder.binding.tvNameType.text = arrayList!![position].name
        else holder.binding.tvNameType.text = arrayList!![position].nameAr

        Glide.with(mContext).load(arrayList!![position].image).override(50,50).into(holder.binding.ivImg)
        if(arrayList!![position].check) holder.binding.ivSelect.setImageResource(R.drawable.ic_select_radio)
        else holder.binding.ivSelect.setImageResource(R.drawable.ic_unselect_radio)

        holder.itemView.setOnClickListener {
          /*  for (i in 0 until arrayList!!.size) {
                arrayList!![i].check = false
            }*/
         if(arrayList!![position].check)   arrayList!![position].check = false
            else arrayList!![position].check = true
            listener.onAmenities(arrayList!!,position)
        }
    }

      interface   OnAmenitiesListener{
          fun onAmenities(model: ArrayList<AmenitiesModel.Property>, position: Int)

    }


    fun notifyAdapter(propertyList: ArrayList<AmenitiesModel.Property>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }


}