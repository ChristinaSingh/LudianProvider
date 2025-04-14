package com.ludianseller.ui.calendar

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ludianseller.R
import com.ludianseller.databinding.ActivityBookingCompleteInfoBinding
import com.ludianseller.ui.calendar.model.BookingDetailModel


class InvoiceAct : AppCompatActivity() {
    private lateinit var binding: ActivityBookingCompleteInfoBinding
    private var bookingModel : BookingDetailModel.Data? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_complete_info)
        initViews()
    }

    private fun initViews() {
        bookingModel = intent.getSerializableExtra("BookingData") as BookingDetailModel.Data?
        setViewData()

        binding.ivBack.setOnClickListener {
            finish()
        }

    }

    private fun setViewData() {
        binding.tvGuestName.text = bookingModel!!.property.user_detail.first_name + " " + bookingModel!!.property.user_detail.last_name
        binding.tvGuestProofNo.text =  "##"+bookingModel!!.property.user_detail.id
        binding.tvReservationNo.text =  bookingModel!!.orderNumber
        binding.tvReservationCreateDate.text =  bookingModel!!.booking_request_created_at
        binding.tvReservationPlace.text =  bookingModel!!.property.rent_string_name
        binding.tvReservationUnit.text =  bookingModel!!.property.rent_string_name
        binding.tvReservationDateOfEntry.text =  bookingModel!!.booking_request_date_start
        binding.tvReservationDeparture.text =  bookingModel!!.checkout_date
        binding.tvReservationNoOfNights.text =  bookingModel!!.booking_request_date_start

        binding.tvPaymentPaidUp.text =  bookingModel!!.orderTotal + " SAR "
        binding.tvPaymentTotal.text =  bookingModel!!.orderTotal + " SAR "

        try {
            val dateList = bookingModel!!.booking_request_date_start.split(", ")
            binding.tvBookingPriceTitle.text =  bookingModel!!.property.price + " SAR " + "X "+ dateList.size+ " " + getString(
                R.string.night)
        }catch (e : Exception){
            e.printStackTrace()
        }



        binding.tvBookingPrice.text =  bookingModel!!.orderSubTotal + " SAR "
        binding.tvBookingServiceFee.text =  bookingModel!!.taxFee + " SAR "
        binding.tvBookingTotal.text =  bookingModel!!.orderTotal + " SAR "


    }


}