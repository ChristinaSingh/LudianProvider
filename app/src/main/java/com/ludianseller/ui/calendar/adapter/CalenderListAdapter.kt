package com.ludianseller.ui.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ludianseller.R
import com.ludianseller.databinding.ItemCalendarListBinding
import com.ludianseller.models.AmenitiesModel
import com.ludianseller.models.CalenderPropertyModel

class CalenderListAdapter (private val mContext: Context,
                           var arrayList: ArrayList<CalenderPropertyModel.Property>?,
                           private val listener : OnItemListener
) : RecyclerView.Adapter<CalenderListAdapter.MyViewHolder>()  {

    class MyViewHolder(var binding: ItemCalendarListBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemCalendarListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_calendar_list, parent, false
        )


        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.tvName.text = arrayList!![position].title
        holder.binding.tvStatus.text = arrayList!![position].approvalStatus

        Glide.with(mContext).load(arrayList!![position].imageUrls[0]).into(holder.binding.ivImg)

        holder.itemView.setOnClickListener {
            listener.onItemClick(position,arrayList!![position],true)
        }
    }


    fun notifyAdapter(calenderArrayList: ArrayList<CalenderPropertyModel.Property>) {
        arrayList = calenderArrayList
        notifyDataSetChanged()
    }



    interface OnItemListener {
        fun onItemClick(mainPosition: Int,data : CalenderPropertyModel.Property,check : Boolean)
    }
}