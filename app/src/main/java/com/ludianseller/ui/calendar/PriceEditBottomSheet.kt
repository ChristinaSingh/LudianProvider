package com.ludianseller.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.ui.calendar.model.CalendarModel

class PriceEditBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bottomSheetView: View
    private var calendarValue: CalendarModel? = null
    private var position: Int = 0
    private var mainPosition: Int = 0

    private lateinit var listener: PriceClickListener


    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    companion object {
        fun newInstance(
            value: CalendarModel,
            mainPosition: Int,
            position: Int,
            listener: PriceClickListener
        ): PriceEditBottomSheet {
            val fragment = PriceEditBottomSheet()
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
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_price_edit, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivClose)
        val edPrice = bottomSheetView.findViewById<EditText>(R.id.edPrice)
        val btnSave = bottomSheetView.findViewById<Button>(R.id.btnSave)


        // Ensure cursor is visible
        edPrice.isCursorVisible = true

        edPrice.setText(calendarValue!!.monthList[position].price)
        edPrice.setSelection(edPrice.text.length) // Move cursor to the end

        btnSave.setOnClickListener {
            calendarValue!!.monthList[position].price = edPrice.text.toString()
            listener.onPrice(calendarValue!!,mainPosition,position)
            dialog!!.dismiss()
        }


        ivClose.setOnClickListener {
         dialog!!.dismiss()
        }

    }

    interface PriceClickListener {
        fun onPrice(value: CalendarModel, mainPosition: Int, position: Int)
    }

}