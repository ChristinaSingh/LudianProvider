package com.ludianseller.ui.calendar

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ludianseller.R
import com.ludianseller.databinding.ActivityBookingCompleteDetailBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.booking.BookingCompleteInfoAct
import com.ludianseller.ui.booking.ChatAct
import com.ludianseller.ui.booking.adapter.ImageSliderAdapter2
import com.ludianseller.ui.booking.adapter.PreviewAdapter11
import com.ludianseller.ui.calendar.model.BookingDetailModel
import com.ludianseller.utils.Helper
import com.ludianseller.utils.SharedPrf
import java.util.Timer
import java.util.TimerTask

class BookingDetailAct : AppCompatActivity() {
    private lateinit var binding: ActivityBookingCompleteDetailBinding
    private var bookingDetailModel : BookingDetailModel.Data? =null
    private var arrayList : ArrayList<BookingModel.Data>?=null

    private var imageArrayList : ArrayList<String> = ArrayList()
    private val sharedPrf by lazy { SharedPrf(this) }

    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds before auto sliding starts
    private val PERIOD_MS: Long = 3000 // Period in milliseconds between each slide
    private var position : Int = 0
    private val handler = Handler()
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_complete_detail)

        initViews()
    }

    private fun initViews() {
        arrayList = ArrayList()
        bookingDetailModel = intent.getSerializableExtra("BookingDetailData") as BookingDetailModel.Data


        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.cardStaus.setOnClickListener {
            if(bookingDetailModel!!.booking_request_status=="COMPLETED") {
                startActivity(Intent(this@BookingDetailAct, InvoiceAct::class.java)
                    .putExtra("BookingData",bookingDetailModel))
            }
        }






        binding.cardChat.setOnClickListener {
            startActivity(Intent(this, ChatAct::class.java).apply {
                putExtra("UserId", bookingDetailModel!!.property.user_detail.id)
                putExtra("UserName", bookingDetailModel!!.property.user_detail.user_name)
                putExtra("UserImage", "")
                putExtra("id", sharedPrf.getStoredTag(SharedPrf.USER_ID))
                putExtra("name", "")
                putExtra("img", "")
                putExtra("bookingID", bookingDetailModel!!.booking_request_id)

            })
        }



        setViewData()
    }


    private fun setViewData() {

        imageArrayList.clear()
        imageArrayList.addAll(bookingDetailModel!!.property.image_urls)

        binding.rvPreview.adapter = PreviewAdapter11(this@BookingDetailAct,imageArrayList)


        binding.viewPager.adapter = ImageSliderAdapter2(this@BookingDetailAct,imageArrayList)
        binding.dotsIndicator.setViewPager2(binding.viewPager)
        startAutoSlide()

        binding.tvName.text = bookingDetailModel!!.property.title
        binding.tvAddress.text =  bookingDetailModel!!.property.address
        binding.tvDescription.text =  bookingDetailModel!!.property.description
        binding.tvPrice.text =  bookingDetailModel!!.orderTotal +  " SAR "  + ",7"
        binding.tvStatus.text = bookingDetailModel!!.booking_request_status
        //  binding.tvDate1.text =    getString(R.string.booking_date)+" : ${bookingModel!!.booking_request_date_start}"
        binding.tvDate1.text =    getString(R.string.booking_date)+" : ${Helper.formatDates(bookingDetailModel!!.booking_request_date_start).joinToString(",")}"
        binding.tvRating.text = bookingDetailModel!!.property.average_rating

        binding.tvChildren.text = getString(R.string.children) + " : " + bookingDetailModel!!.booking_request_children
        binding.tvPerson.text = getString(R.string.person) + " : " + bookingDetailModel!!.booking_request_guests

        binding.tvBedroom.text =   bookingDetailModel!!.property.bedrooms + " "+ getString(R.string.bedrooms)
        binding.tvGuest.text =   bookingDetailModel!!.property.guests + " " + getString(R.string.guest_max)
        binding.tvHouse.text =  bookingDetailModel!!.property.rent_string_name
        binding.tvSize.text =  bookingDetailModel!!.property.squreMeter + " m²"
        binding.tvBathroom.text =  bookingDetailModel!!.property.bathrooms + " " + getString(R.string.bathrooms)


        if(bookingDetailModel!!.property.average_rating=="0.0") {
            binding.iv3.visibility = View.GONE
            binding.tvRating.visibility = View.GONE
        }
        else {
            binding.iv3.visibility = View.VISIBLE
            binding.tvRating.visibility = View.VISIBLE
        }

        if(bookingDetailModel!!.property.wifi) {
            binding.iv1.visibility = View.VISIBLE
            binding.tvWifi.visibility = View.VISIBLE
        }
        else {
            binding.iv1.visibility = View.GONE
            binding.tvWifi.visibility = View.GONE
        }




        if(bookingDetailModel!!.booking_request_status=="ACCEPT"){
            binding.cardStaus.visibility = View.VISIBLE
            binding.cardChat.visibility = View.VISIBLE
            binding.tvStatus.text = getString(R.string.accepted)

        }

        else if(bookingDetailModel!!.booking_request_status=="COMPLETED"){
            binding.cardStaus.visibility = View.VISIBLE
            binding.cardChat.visibility = View.GONE
            binding.tvStatus.text = getString(R.string.see_invoice)

        }



    }


    private fun startAutoSlide() {
        val update = Runnable {
            if (currentPage ==  bookingDetailModel!!.property.image_urls.size) {
                currentPage = 0
            }
            binding.viewPager.setCurrentItem(currentPage++, true)
        }

        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, DELAY_MS, PERIOD_MS)
    }

}