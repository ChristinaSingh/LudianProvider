package com.ludianseller.ui.home

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ludianseller.R
import com.ludianseller.databinding.ActivityBookingDetailBinding
import com.ludianseller.models.PropertyModel
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.util.Timer
import java.util.TimerTask

class BookingDetailAct : AppCompatActivity() {
    private lateinit var binding: ActivityBookingDetailBinding
    private var propertyModel : PropertyModel.Property? =null
    private var imageArrayList : ArrayList<PropertyModel.Property.PropertyImage> = ArrayList()

    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds before auto sliding starts
    private val PERIOD_MS: Long = 3000 // Period in milliseconds between each slide

    private val handler = Handler()
    private val timer = Timer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_booking_detail)
        initViews()
    }

    private fun initViews() {

        if(intent!=null) propertyModel = intent.getSerializableExtra("propertyData") as PropertyModel.Property?

         setViewData()





        binding.btnEdit.setOnClickListener {
           // startActivity(Intent(this@BookingDetailAct, BookingCompleteAct::class.java))
        }


        binding.ivBack.setOnClickListener {
            finish()
        }


    }

    private fun setViewData() {

        imageArrayList.clear()
        imageArrayList.addAll(propertyModel!!.propertyImages)


        binding.viewPager.adapter = ImageSliderAdapter(this@BookingDetailAct,imageArrayList)
        binding.dotsIndicator.setViewPager2(binding.viewPager)
        startAutoSlide()


        binding.rvPreview.adapter = PreviewAdapter(this@BookingDetailAct,imageArrayList)

        binding.tvName.text = propertyModel!!.title
        binding.tvAddress.text = propertyModel!!.address
        binding.tvDescription.text = propertyModel!!.description
        binding.tvPrice.text = "$" +  propertyModel!!.price + ",7"

        binding.tvBedroom.text =   propertyModel!!.bedrooms + " bedrooms"
        binding.tvGuest.text =   propertyModel!!.guests + " guests max."
        binding.tvHouse.text =  propertyModel!!.house + " House"
        binding.tvSize.text =  propertyModel!!.size + " m²"
        binding.tvBathroom.text =  propertyModel!!.bathrooms + " bathrooms"

        if(propertyModel!!.wifi=="1") {
            binding.iv1.visibility = View.VISIBLE
            binding.tvWifi.visibility = View.VISIBLE
        }
        else {
            binding.iv1.visibility = View.GONE
            binding.tvWifi.visibility = View.GONE
        }

        if(propertyModel!!.breakfast=="1") {
            binding.iv2.visibility = View.VISIBLE
            binding.tvBreakfast.visibility = View.VISIBLE
        }
        else {
            binding.iv2.visibility = View.GONE
            binding.tvBreakfast.visibility = View.GONE
        }

    }


    private fun startAutoSlide() {
        val update = Runnable {
            if (currentPage == propertyModel!!.propertyImages.size) {
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
