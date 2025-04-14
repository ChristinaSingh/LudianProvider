package com.ludianseller.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.ui.calendar.bottomsheet.AddNotBottomSheet
import com.ludianseller.ui.calendar.model.CalendarModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalendarClickBottomSheet : BottomSheetDialogFragment(),
    PriceEditBottomSheet.PriceClickListener, AddNotBottomSheet.NoteListener {
    private lateinit var bottomSheetView: View
    private var calendarValue : CalendarModel?= null
    private var position : Int =0
    private var mainPosition : Int =0
    private  var tvPrice :TextView?=null
    private lateinit var listener : CalendarClickListener
    private  var tvNote : TextView?=null
    private  var ivEditNote : ImageView?=null



    companion object {
        fun newInstance(value: CalendarModel,mainPosition:Int,position : Int,listener : CalendarClickListener): CalendarClickBottomSheet {
            val fragment = CalendarClickBottomSheet()
            fragment.calendarValue = value
            fragment.mainPosition = mainPosition
            fragment.position = position
            fragment.listener = listener

            return fragment
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_calender, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvDate = bottomSheetView.findViewById<TextView>(R.id.tvDate)
        val tvForbidden = bottomSheetView.findViewById<TextView>(R.id.tvForbidden)
        val tvNightAvailability = bottomSheetView.findViewById<TextView>(R.id.tvNightAvailability)
        val ivEdit = bottomSheetView.findViewById<ImageView>(R.id.ivEdit)
        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)

        tvPrice = bottomSheetView.findViewById<TextView>(R.id.tvPrice)

        val btnAddNote = bottomSheetView.findViewById<RelativeLayout>(R.id.btnAddNote)
        tvNote = bottomSheetView.findViewById<TextView>(R.id.tvNote)
        ivEditNote = bottomSheetView.findViewById<ImageView>(R.id.ivEditNote)



        tvDate.text = "${calendarValue!!.monthList[position].day} " + monthYearFromDate(calendarValue!!.selectDate)
        tvPrice!!.text =  "SAR${calendarValue!!.monthList[position].price}"
        if(calendarValue!!.monthList[position].openBlock=="1") tabClickNew(2,tvForbidden,tvNightAvailability)
        else tabClickNew(1,tvForbidden,tvNightAvailability)

        if(calendarValue!!.monthList[position].note != "") {
            ivEditNote!!.visibility =View.VISIBLE
            tvNote!!.text = calendarValue!!.monthList[position].note
        }
        else {
            ivEditNote!!.visibility =View.GONE
            tvNote!!.text = getString(R.string.add_note)
        }



        tvForbidden.setOnClickListener {
            tabClick(1,tvForbidden,tvNightAvailability)
        }

        tvNightAvailability.setOnClickListener {
            tabClick(2,tvForbidden,tvNightAvailability)
        }

        ivClose.setOnClickListener {
            dialog!!.dismiss()
        }


        ivEdit.setOnClickListener {
            val calendarClickBottomSheet = PriceEditBottomSheet
                .newInstance(calendarValue!!,mainPosition,position,this@CalendarClickBottomSheet)

            calendarClickBottomSheet.show(childFragmentManager, "")
        }


        btnAddNote.setOnClickListener {
          val note = if(calendarValue!!.monthList[position].note != "") "Edit note" else "Add note"

            val addNotBottomSheet = AddNotBottomSheet
                .newInstance(calendarValue!!,mainPosition,position,note,this@CalendarClickBottomSheet)

            addNotBottomSheet.show(childFragmentManager, "")
        }


    }

    private fun tabClickNew(i : Int, tvForbidden : TextView, tvNightAvailability : TextView){
        if(i==1){
            tvForbidden.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.white))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.black))
         //   calendarValue!!.monthList[position].openBlock=false
          //  listener.onCalendarItemClicked(calendarValue!!,mainPosition,position)
        }
        else{
            tvForbidden.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.black))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.white))
           // calendarValue!!.monthList[position].openBlock=true
          //  listener.onCalendarItemClicked(calendarValue!!,mainPosition,position)
        }
    }


    private fun tabClick(i : Int, tvForbidden : TextView, tvNightAvailability : TextView){
        if(i==1){
            tvForbidden.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.white))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.black))
            calendarValue!!.monthList[position].openBlock= "0"
            listener.onCalendarItemClicked(calendarValue!!,mainPosition,position)
        }
        else{
            tvForbidden.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.black))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.white))
            calendarValue!!.monthList[position].openBlock= "1"
            listener.onCalendarItemClicked(calendarValue!!,mainPosition,position)
        }
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }



    interface CalendarClickListener {
        fun onCalendarItemClicked(value: CalendarModel, mainPosition: Int,position: Int)
        fun onNeteAdd(value: CalendarModel, mainPosition: Int,position: Int)

    }

    override fun onPrice(value: CalendarModel, mainPosition: Int, position: Int) {
        tvPrice!!.text =  "SAR${value.monthList[position].price}"

        listener.onCalendarItemClicked(value,mainPosition,position)

    }

    override fun onNote(value: CalendarModel, mainPosition: Int, position: Int) {
        tvNote!!.text = calendarValue!!.monthList[position].note
        ivEditNote!!.visibility =View.VISIBLE
        listener.onNeteAdd(value,mainPosition,position)
    }

}