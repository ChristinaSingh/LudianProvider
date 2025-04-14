package com.ludianseller.ui.booking.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemBookingBinding
import com.ludianseller.databinding.ItemBookingCancelBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.home.BookingDetailAct
import com.ludianseller.utils.Helper


class BookingCancelAdapter (
    private val mContext: Context,
                          var arrayList: ArrayList<BookingModel.Data>?,
) : RecyclerView.Adapter<BookingCancelAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemBookingCancelBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemBookingCancelBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_booking_cancel, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.binding.tvName.text =  "${arrayList!![position].booking_request_id}# ${arrayList!![position].property.user_detail.first_name} ${arrayList!![position].property.user_detail.last_name}"
      //  holder.binding.tvAddress.text =   "${arrayList!![position].property.title} (${arrayList!![position].property.address})"
      //  holder.binding.tvPrice.text = "$${arrayList!![position].property.price}"// "$${String.format("%.2f", arrayList!![position].property.price)}"
      //  holder.binding.tvDateTime.text =    "From : ${arrayList!![position].booking_request_date_start}"
      //  holder.binding.tvStatus.text = arrayList!![position].booking_request_status

        holder.binding.tvName.text =  "${arrayList!![position].booking_request_id}# ${arrayList!![position].property.user_detail.first_name} ${arrayList!![position].property.user_detail.last_name}"
        holder.binding.tvAddress.text =   "${arrayList!![position].property.title} (${arrayList!![position].property.address})"
        holder.binding.tvPrice.text = "${arrayList!![position].orderTotal} SAR"// "$${String.format("%.2f", arrayList!![position].property.price)}"
      //  holder.binding.tvDateTime.text =   mContext.getString(R.string.from)+ " : ${arrayList!![position].booking_request_date_start}"
        holder.binding.tvDateTime.text =    mContext.getString(R.string.from)+" : ${Helper.formatDates(arrayList!![position].booking_request_date_start).joinToString(", ")}"

        //  holder.binding.tvStatus.text = arrayList!![position].booking_request_status
       // holder.binding.tvAddress.ellipsize = TextUtils.TruncateAt.END




        holder.itemView.setOnClickListener {
          //  mContext.startActivity(Intent(mContext, BookingDetailAct::class.java))

        }
    }

    fun notifyAdapter(bookingArrayList: ArrayList<BookingModel.Data>) {
        arrayList = bookingArrayList
        notifyDataSetChanged()
    }

}