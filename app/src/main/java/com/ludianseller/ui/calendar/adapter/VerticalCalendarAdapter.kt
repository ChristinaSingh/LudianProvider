package com.ludianseller.ui.calendar.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemCalMainBinding
import com.ludianseller.ui.calendar.model.CalendarModel
import java.time.LocalDate

class VerticalCalendarAdapter (
    private val mContext: Context,
    var arrayList: ArrayList<CalendarModel>?,
    private val listener : OnMonthListener
) : RecyclerView.Adapter<VerticalCalendarAdapter.MyViewHolder>(), CalendarAdapter.OnItemListener {



    class MyViewHolder(var binding: ItemCalMainBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemCalMainBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_cal_main, parent, false
        )
        /*  val layoutParams: ViewGroup.LayoutParams = parent.layoutParams
          layoutParams.height = (parent.height * 0.166666666).toInt()*/

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.monthYearTV.text = arrayList!![position].monthName

        holder.binding.calendarRecyclerView.adapter = CalendarAdapter(mContext,position,arrayList!![position].monthList,this@VerticalCalendarAdapter)

        /*
            if(arrayList!![position].discount==null)   {
                holder.binding.tvDiscount.text = mContext.getString(R.string.add_discount)
            }
            else {
                holder.binding.tvDiscount.text = mContext.getString(R.string.discount)

            }



            holder.binding.tvDiscount.setOnClickListener {
                if(holder.binding.tvDiscount.text.toString() == mContext.getString(R.string.discount)){
                    listener.onDiscount(position,"added")
                }else{
                    listener.onDiscount(position,"new")

                }

            }
    */
    }


    interface OnMonthListener {
        fun onMonth(mainPosition: Int,position: Int, selectDate : LocalDate, dayText: String?,check: Boolean,whichTypeDate:String)
        fun onDiscount(mainPosition: Int,tag:String)

    }

    override fun onItemClick(mainPosition: Int,position: Int, dayText: String?,check : Boolean,whichTypeDate:String) {
        listener.onMonth(mainPosition,position,arrayList!![mainPosition].selectDate,dayText,check,whichTypeDate)
    }

    fun notifyValue(list: ArrayList<CalendarModel>){
        arrayList = list
        notifyDataSetChanged()
    }

}