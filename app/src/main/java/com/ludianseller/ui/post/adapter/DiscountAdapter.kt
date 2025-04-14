package com.ludianseller.ui.post.adapter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemDiscountBinding
import com.ludianseller.models.DiscountModel


class DiscountAdapter (
    private val mContext: Context,
    var arrayList: ArrayList<DiscountModel.Discount>?
    , val listener : OnDiscountListener
) : RecyclerView.Adapter<DiscountAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemDiscountBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemDiscountBinding = DataBindingUtil.inflate(
         LayoutInflater.from(mContext), R.layout.item_discount, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.tvName.text = arrayList!![position].title
        holder.binding.tvDescription.text = arrayList!![position].subTitle
        holder.binding.tvDiscount.text = arrayList!![position].discount + "%"

        if(arrayList!![position].check) {
            holder.binding.tvName.setTextColor(mContext.getColor(R.color.color_primary))
            holder.binding.tvDiscount.setTextColor(mContext.getColor(R.color.color_primary))
            holder.binding.rlMain.setBackgroundResource(R.drawable.rounded_corner_stroke_10)
           // holder.binding.ivImg.setColorFilter(mContext.getColor(R.color.color_primary), PorterDuff.Mode.SRC_ATOP)
            holder.binding.ivImg.setImageResource(R.drawable.ic_select_radio)

        }
        else {
            holder.binding.tvName.setTextColor(mContext.getColor(R.color.color_gray_light1))
            holder.binding.tvDiscount.setTextColor(mContext.getColor(R.color.color_gray_light1))
            holder.binding.rlMain.setBackgroundResource(R.drawable.rounded_gray_white10_bg)
        //    holder.binding.ivImg.setColorFilter(mContext.getColor(R.color.color_gray_light1), PorterDuff.Mode.SRC_ATOP)
            holder.binding.ivImg.setImageResource(R.drawable.ic_unselect_radio)

        }

        holder.itemView.setOnClickListener {
            for (i in 0 until arrayList!!.size) {
                arrayList!![i].check = false
            }
         //  if(arrayList!![position].check) arrayList!![position].check = false
          //  else arrayList!![position].check = true
            arrayList!![position].check = true
            listener.onDiscount(arrayList!!,position)
        }
    }

    interface   OnDiscountListener{
        fun onDiscount(propertyList: ArrayList<DiscountModel.Discount>, position: Int)

    }


    fun notifyAdapter(discountList: ArrayList<DiscountModel.Discount>) {
        arrayList = discountList
        notifyDataSetChanged()
    }


}