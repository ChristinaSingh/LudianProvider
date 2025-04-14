package com.ludianseller.ui.calendar.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.ui.calendar.model.CalendarModel
import com.ludianseller.ui.calendar.model.CouponModel

class DiscountBottomSheet : BottomSheetDialogFragment(),
    DiscountListBottomSheet.DiscountSelectListener {
    private lateinit var bottomSheetView: View
    private var calendarValue: CalendarModel? = null
    private var position: Int = 0
    private var mainPosition: Int = 0
    private var propertyId: String=""
    private lateinit var listener: DiscountListener
    private lateinit var tvDiscount : TextView
    private lateinit var tvDescription : TextView
    private lateinit var tvName : TextView

    companion object {
        fun newInstance(
            value: CalendarModel,
            mainPosition: Int,
            position: Int,
            propertyId:String,
            listener: DiscountListener
        ): DiscountBottomSheet {
            val fragment = DiscountBottomSheet()
            fragment.calendarValue = value
            fragment.mainPosition = mainPosition
            fragment.position = position
            fragment.propertyId =propertyId
            fragment.listener = listener

            return fragment
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_discount, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvMonth = bottomSheetView.findViewById<TextView>(R.id.tvMonth)
         tvDiscount = bottomSheetView.findViewById<TextView>(R.id.tvDiscount)
         tvDescription = bottomSheetView.findViewById<TextView>(R.id.tvDescription)
         tvName = bottomSheetView.findViewById<TextView>(R.id.tvName)
        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)
        val rlMain = bottomSheetView.findViewById<RelativeLayout>(R.id.rlMain)


        tvMonth.text = calendarValue!!.monthName
     //   tvDiscount.text = "${calendarValue!!.discount!!.discount}%"
     //   tvDescription.text = "${calendarValue!!.discount!!.subTitle}%"
    //    tvDescription.text = calendarValue!!.discount!!.subTitle

    //    tvName.text = "${calendarValue!!.discount!!.title}%"








        ivClose.setOnClickListener {
            dialog!!.dismiss()
        }


        rlMain.setOnClickListener {
          /*  val discountListBottomSheet = DiscountListBottomSheet
                .newInstance(calendarValue!!, mainPosition, position,propertyId, this@DiscountBottomSheet)

            discountListBottomSheet.show(childFragmentManager, "")*/
        }

    }

     interface DiscountListener {
         fun onDiscount(value: CalendarModel, mainPosition: Int,position: Int)
     }

    override fun onDiscountSelect(value: CouponModel.Result, mainPosition: Int, position: Int) {
        //calendarValue = value
      /*  tvDiscount.text = "${calendarValue!!.discount!!.discount}%"
        tvDescription.text = calendarValue!!.discount!!.subTitle
        tvName.text = "${calendarValue!!.discount!!.title}%"*/
       // listener.onDiscount(value,mainPosition,position)
    }


}