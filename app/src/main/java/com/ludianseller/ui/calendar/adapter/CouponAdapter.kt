package com.ludianseller.ui.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemCouponBinding
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.ui.calendar.model.CouponModel


class CouponAdapter (private val mContext: Context,
                     var arrayList: ArrayList<CouponModel.Result>?,
                     private val listener : OnItemListener
) : RecyclerView.Adapter<CouponAdapter.MyViewHolder>()  {

    class MyViewHolder(var binding: ItemCouponBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemCouponBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_coupon, parent, false
        )


        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.tvName.text = arrayList!![position].title
        holder.binding.tvDescription.text = mContext.getString(R.string.expire_on) + " : " +  arrayList!![position].endDate
        holder.binding.tvDiscount.text = arrayList!![position].discountPercent + "%"


        holder.itemView.setOnClickListener {
            listener.onItemClick(position,arrayList!![position])
        }
    }


    fun notifyAdapter(couponList: ArrayList<CouponModel.Result>) {
        arrayList = couponList
        notifyDataSetChanged()
    }



    interface OnItemListener {
        fun onItemClick(mainPosition: Int, data : CouponModel.Result)
    }
}
