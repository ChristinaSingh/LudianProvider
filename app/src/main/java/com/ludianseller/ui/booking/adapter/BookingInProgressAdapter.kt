package com.ludianseller.ui.booking.adapter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemBookingBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.booking.OrderDetailAct
import com.ludianseller.ui.home.BookingDetailAct
import com.ludianseller.utils.Helper


class BookingInProgressAdapter (
    private val mContext: Context,var arrayList: ArrayList<BookingModel.Data>?
    ,val listener : OnBookingAcceptCancelListener
) : RecyclerView.Adapter<BookingInProgressAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemBookingBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_booking, parent, false
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
      //  holder.binding.tvDateTime.text =   mContext.getString(R.string.from)+ " : ${arrayList!![position].booking_request_date_start}"
        holder.binding.tvDateTime.text =    mContext.getString(R.string.from)+" : ${Helper.formatDates(arrayList!![position].booking_request_date_start).joinToString(", ")}"

        //  holder.binding.tvStatus.text = arrayList!![position].booking_request_status
        holder.binding.tvAddress.ellipsize = TextUtils.TruncateAt.END

        holder.binding.tvAccept.text = mContext.getString(R.string.show_details)




        holder.binding.tvAccept.setOnClickListener {
            mContext.startActivity(Intent(mContext,OrderDetailAct::class.java)
                .putExtra("BookingData",arrayList!![position])
                .putExtra("position",position.toString()))
        }


        /*if(arrayList!![position].booking_request_status=="ACCEPT"){
            holder.binding.cardAccept.visibility = View.GONE
            holder.binding.cardCancel.visibility = View.GONE
           holder.binding.cardStatus.visibility = View.VISIBLE
            holder.binding.tvStatus.text = "Accepted"

        }
        else {
            holder.binding.cardAccept.visibility = View.VISIBLE
            holder.binding.cardCancel.visibility = View.VISIBLE
            holder.binding.cardStatus.visibility = View.GONE
        }*/



       /* holder.binding.tvAccept.setOnClickListener {
            listener.onAcceptCancel(arrayList!![position],position,"ACCEPT")

        }


        holder.binding.tvCancel.setOnClickListener {
           listener.onAcceptCancel(arrayList!![position],position,"CANCEL")
        }*/


    }


    interface OnBookingAcceptCancelListener {
        fun onAcceptCancel(model: BookingModel.Data, position: Int, type: String)

    }




    fun notifyAdapter(bookingArrayList: ArrayList<BookingModel.Data>) {
        arrayList = bookingArrayList
        notifyDataSetChanged()
    }

}