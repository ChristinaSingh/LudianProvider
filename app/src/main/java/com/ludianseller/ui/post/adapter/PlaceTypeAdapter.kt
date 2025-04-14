package com.ludianseller.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.ItemChatBinding
import com.ludianseller.databinding.ItemPlaceTypeBinding
import com.ludianseller.models.PlaceTypeModel
import com.ludianseller.models.PropertyModel
import com.ludianseller.utils.SharedPrf


class PlaceTypeAdapter (
    private val mContext: Context,
                          var arrayList: ArrayList<PlaceTypeModel.Property>?
    , val listener : OnPlaceTypeListener
) : RecyclerView.Adapter<PlaceTypeAdapter.MyViewHolder>() {

    private val sharedPrf by lazy { SharedPrf(mContext) }


    class MyViewHolder(var binding: ItemPlaceTypeBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemPlaceTypeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_place_type, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(sharedPrf.getStoredTag(SharedPrf.LANGUAGE)=="en")   holder.binding.tvNameType.text = arrayList!![position].propertyCategoryName
        else  holder.binding.tvNameType.text = arrayList!![position].propertyCategoryNameAr

        Glide.with(mContext).load(arrayList!![position].propertyCategoryImage).override(50,50).into(holder.binding.ivImg)
        if(arrayList!![position].check) holder.binding.ivSelect.setImageResource(R.drawable.ic_select_radio)
        else holder.binding.ivSelect.setImageResource(R.drawable.ic_unselect_radio)

        holder.itemView.setOnClickListener {
            for (i in 0 until arrayList!!.size) {
                arrayList!![i].check = false
            }
            arrayList!![position].check = true
            listener.onPlaceType(arrayList!!,position)
        }
    }

    interface   OnPlaceTypeListener{
        fun onPlaceType(propertyList: ArrayList<PlaceTypeModel.Property>, position: Int)

    }


    fun notifyAdapter(propertyList: ArrayList<PlaceTypeModel.Property>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }


}
