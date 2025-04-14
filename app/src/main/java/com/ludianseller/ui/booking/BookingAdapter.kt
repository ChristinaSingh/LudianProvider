package com.ludianseller.ui.booking

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemBookingBinding
import com.ludianseller.ui.home.BookingDetailAct


class BookingAdapter (
    private val mContext: Context/*,
                          var arrayList: ArrayList<String>?,*/
) : RecyclerView.Adapter<BookingAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemBookingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_booking, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // holder.binding.chk.text = arrayList!!.get(position)
        holder.itemView.setOnClickListener {
            mContext.startActivity(Intent(mContext, BookingDetailAct::class.java))

        }
    }
}