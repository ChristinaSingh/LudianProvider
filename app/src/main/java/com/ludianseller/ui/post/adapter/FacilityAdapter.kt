package com.ludianseller.ui.post.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.ItemFacilityBinding
import com.ludianseller.models.FacilitiesModel
import com.ludianseller.models.PlaceModel
import com.ludianseller.utils.SharedPrf


class FacilityAdapter (
    private val mContext: Context,
                          var arrayList: ArrayList<FacilitiesModel.Property>?
    , val listener : OnFacilityListener
) : RecyclerView.Adapter<FacilityAdapter.MyViewHolder>() {

    private val sharedPrf by lazy { SharedPrf(mContext) }

    class MyViewHolder(var binding: ItemFacilityBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemFacilityBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_facility, parent, false
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
            listener.onFacility(arrayList!!,position)
        }
    }

      interface   OnFacilityListener{
          fun onFacility(model: ArrayList<FacilitiesModel.Property>, position: Int)

    }

    fun notifyAdapter(propertyList: ArrayList<FacilitiesModel.Property>) {
        arrayList = propertyList
        notifyDataSetChanged()
    }


}