package com.ludianseller.ui.calendar.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.ui.calendar.model.CalendarModel

class AddNotBottomSheet : BottomSheetDialogFragment(){
    private lateinit var bottomSheetView: View
    private var calendarValue: CalendarModel? = null
    private var position: Int = 0
    private var mainPosition: Int = 0
    private var title: String ? =null

    private var tvPrice: TextView? = null
    private lateinit var listener: NoteListener


    companion object {
        fun newInstance(
            value: CalendarModel,
            mainPosition: Int,
            position: Int,
            title:String,
            listener: NoteListener
        ): AddNotBottomSheet {
            val fragment = AddNotBottomSheet()
            fragment.calendarValue = value
            fragment.mainPosition = mainPosition
            fragment.position = position
            fragment.title = title
            fragment.listener = listener

            return fragment
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_add_note, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val edNote = bottomSheetView.findViewById<EditText>(R.id.edNote)
        val tvTile = bottomSheetView.findViewById<TextView>(R.id.tvTile)
        val ivBack = bottomSheetView.findViewById<ImageView>(R.id.ivBack)
        val btnSave = bottomSheetView.findViewById<Button>(R.id.btnSave)


         tvTile.text = title
         edNote.setText(calendarValue!!.monthList[position].note)

        ivBack.setOnClickListener {
            dialog!!.dismiss()
        }


        btnSave.setOnClickListener {
            if(edNote.text.toString()!="") {
                calendarValue!!.monthList[position].note = edNote.text.toString()
                listener.onNote(calendarValue!!, mainPosition, position)
                dialog!!.dismiss()
            }
            else Toast.makeText(requireActivity(),getString(R.string.please_add_note),Toast.LENGTH_LONG).show()
        }

    }

    interface NoteListener {
        fun onNote(value: CalendarModel, mainPosition: Int, position: Int)
    }




}