package com.ludianseller.ui.calendar.adapter

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ludianseller.R
import com.ludianseller.databinding.ItemCalendarBinding
import com.ludianseller.ui.calendar.model.CalendarModel

class CalendarAdapter (
    private val mContext: Context,
    private val mainPosition : Int,
    var arrayList: ArrayList<CalendarModel.Days>?,
    private val listener : OnItemListener
) : RecyclerView.Adapter<CalendarAdapter.MyViewHolder>() {



    class MyViewHolder(var binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemCalendarBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext), R.layout.item_calendar, parent, false
        )
        /*  val layoutParams: ViewGroup.LayoutParams = parent.layoutParams
          layoutParams.height = (parent.height * 0.166666666).toInt()*/

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {



        if(arrayList!![position].day!="")  {
            holder.binding.llMain.visibility = View.VISIBLE
            holder.binding.cellDayText.text = arrayList!![position].day

          /* if(arrayList!![position].discountValue!=""){

                val orgPrice : Double = arrayList!![position].price.toDouble()
                val discountVal : Double = arrayList!![position].discountValue!!.toDouble()
                val discountPrice  = calculateDiscountedPrice(orgPrice,discountVal)
               // arrayList!![position].price = String.format("%.2f", discountPrice)
                holder.binding.tvPrice.text = String.format("%.2f", discountPrice)

            }*/

           /* else*/ holder.binding.tvPrice.text = arrayList!![position].price
        }
        else {
            holder.binding.llMain.visibility = View.GONE
        }



        if(arrayList!![position].note!="") holder.binding.viewNote.visibility = View.VISIBLE
         else holder.binding.viewNote.visibility = View.GONE



        if(arrayList!![position].whichTypeDate=="current") {

            if(arrayList!![position].check){
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

                holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_primary))
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.white))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.white))

            }
            else {
                val bg =  mContext.getDrawable(R.drawable.rounded_brown_with0)
                holder.binding.llMain.background = bg
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.black))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.black))
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

            }
        }
      else if(arrayList!![position].whichTypeDate=="future") {
            if(arrayList!![position].check){
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

                holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_primary))
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.white))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.white))


            }
            else {
                holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_gray_calendar))
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.black))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.black))
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

            }
        }

        else{
            if(arrayList!![position].check){
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.WHITE), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

                holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_primary))
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.white))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.white))


            }
            else {
                holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_gray_calendar))
                holder.binding.cellDayText.setTextColor(mContext.getColor(R.color.black))
                holder.binding.tvPrice.setTextColor(mContext.getColor(R.color.black))
                if(arrayList!![position].openBlock=="1"){
                    val spannableString = SpannableString(arrayList!![position].day)
                    // Apply strikethrough
                    spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                    // Apply color to the strikethrough text
                    spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, arrayList!![position].day.length, 0)
                    // Set the SpannableString to TextView
                    holder.binding.cellDayText.text = spannableString
                }

            }
            }




/*

        else{
            holder.binding.llMain.setBackgroundColor(mContext.getColor(R.color.color_gray_light7))
            if(arrayList!![position].openBlock=="1"){
                val spannableString = SpannableString(arrayList!![position].day)
                // Apply strikethrough
                spannableString.setSpan(StrikethroughSpan(), 0, arrayList!![position].day.length, 0)
                // Apply color to the strikethrough text
                spannableString.setSpan(ForegroundColorSpan(Color.BLACK), 0, arrayList!![position].day.length, 0)
                // Set the SpannableString to TextView
                holder.binding.cellDayText.text = spannableString
            }

        }
*/




            holder.itemView.setOnClickListener {

        //  if(arrayList!![position].whichTypeDate=="current" || arrayList!![position].whichTypeDate=="future")  listener.onItemClick(mainPosition,position,arrayList!![position].day,true)

                listener.onItemClick(mainPosition,position,arrayList!![position].day,true,arrayList!![position].whichTypeDate)

            }
    }


    interface OnItemListener {
        fun onItemClick(mainPosition: Int,position: Int, dayText: String?,check : Boolean,whichTypeDate:String)
    }

    private fun calculateDiscountedPrice(originalPrice: Double, discountPercentage: Double): Double {
        // Ensure the discount percentage is between 0 and 100
        require(discountPercentage in 0.0..100.0) { "Discount percentage must be between 0 and 100" }

        // Calculate the discount amount
        val discountAmount = originalPrice * (discountPercentage / 100)

        // Subtract the discount amount from the original price
        return originalPrice - discountAmount
    }


}