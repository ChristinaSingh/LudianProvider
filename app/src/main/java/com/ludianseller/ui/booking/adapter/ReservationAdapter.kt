package com.ludianseller.ui.booking.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.databinding.ItemBookingBinding
import com.ludianseller.databinding.ItemReservationBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.booking.OrderDetailAct
import com.ludianseller.utils.Helper


class ReservationAdapter (
    private val mContext: Context, var arrayList: ArrayList<BookingModel.Data>?

) : RecyclerView.Adapter<ReservationAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemReservationBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemReservationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_reservation, parent, false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.tvName.text =  "${arrayList!![position].booking_request_id}# ${arrayList!![position].property.user_detail.first_name} ${arrayList!![position].property.user_detail.last_name}"
        holder.binding.tvAddress.text =   "${arrayList!![position].property.title} (${arrayList!![position].property.address})"
        holder.binding.tvPrice.text = "${arrayList!![position].orderTotal} SAR"// "$${String.format("%.2f", arrayList!![position].property.price)}"
     //   holder.binding.tvDateTime.text =   mContext.getString(R.string.from)+ " : ${arrayList!![position].booking_request_date_start}"
        holder.binding.tvDateTime.text =    mContext.getString(R.string.from)+" : ${Helper.formatDates(arrayList!![position].booking_request_date_start).joinToString(", ")}"

        //  holder.binding.tvStatus.text = arrayList!![position].booking_request_status
        holder.binding.tvAddress.ellipsize = TextUtils.TruncateAt.END


        if(arrayList!![position].property.image_urls.isNotEmpty()) {
            Glide.with(mContext)
                .load(arrayList!![position].property.image_urls[0])
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
                .error(com.denzcoskun.imageslider.R.drawable.default_error)
                .centerInside()
                .into(holder.binding.ivImg)
        }

        else {
            Glide.with(mContext)
                .load(com.denzcoskun.imageslider.R.drawable.default_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
                .error(com.denzcoskun.imageslider.R.drawable.default_error)
                .centerInside()
                .into(holder.binding.ivImg)
        }


        holder.itemView.setOnClickListener {
            mContext.startActivity(
                Intent(mContext, OrderDetailAct::class.java)
                .putExtra("BookingData",arrayList!![position])
                .putExtra("position",position.toString()))
        }




    }


    fun notifyAdapter(bookingArrayList: ArrayList<BookingModel.Data>) {
        arrayList = bookingArrayList
        notifyDataSetChanged()
    }

}