package com.ludianseller.ui.post

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.ActivityOrderDetailBinding
import com.ludianseller.databinding.ActivityPropertyDetailBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.models.PropertyModel
import com.ludianseller.ui.booking.ChatAct
import com.ludianseller.ui.booking.adapter.ImageSliderAdapter2
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.ReviewAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class PropertyDetailAct : AppCompatActivity(){
    private lateinit var binding: ActivityPropertyDetailBinding
    private lateinit var sellerDataViewModel: SellerDataViewModel

    private var bookingModel : CalenderPropertyModel.Property? =null
    private var imageArrayList : ArrayList<String> = ArrayList()
    private val sharedPrf by lazy { SharedPrf(this) }

    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds before auto sliding starts
    private val PERIOD_MS: Long = 3000 // Period in milliseconds between each slide
    private var position : Int = 0
    private val handler = Handler()
    private val timer = Timer()
    private var type:String=""
    private lateinit var reviewAdapter: ReviewAdapter
    private var arrayList : ArrayList<ReviewModel.Data> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_property_detail)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {

        bookingModel = intent.getSerializableExtra("BookingData") as CalenderPropertyModel.Property?
        position  = intent.getStringExtra("position")!!.toInt()
        setViewData()

        binding.ivBack.setOnClickListener {
            finish()
        }

        /* binding.cardComplete.setOnClickListener {
             val bookingCompleteRequest = bookingCompleteRequest(bookingModel!!.booking_request_id)
             Log.e("Booking Complete Request===", bookingCompleteRequest.toString())
             Helper.showProgressMessage(this@OrderDetailAct,getString(R.string.please_wait))
             userDataViewModel.bookingComplete(bookingCompleteRequest)
         }*/






    }

    private fun setViewData() {

        imageArrayList.clear()
        imageArrayList.addAll(bookingModel!!.imageUrls)


        arrayList = ArrayList()

        reviewAdapter = ReviewAdapter(this@PropertyDetailAct,arrayList)
        binding.rvReview.adapter = reviewAdapter

        binding.viewPager.adapter = ImageSliderAdapter2(this@PropertyDetailAct,imageArrayList)
        binding.dotsIndicator.setViewPager2(binding.viewPager)
        startAutoSlide()

      //  binding.tvName.text = bookingModel!!.title
     //   binding.tvAddress.text =  bookingModel!!.address
     //   binding.tvDescription.text =  bookingModel!!.description
   //    binding.tvPrice.text = "SAR" +   bookingModel!!.price + ",7"
      //  binding.tvStatus.text = bookingModel!!.booking_request_status
      //  binding.tvDate1.text =    "Booking Date : ${bookingModel!!.b}"

        binding.tvName.text = bookingModel!!.title
        binding.tvAddress.text = bookingModel!!.address
        binding.tvDescription.text = bookingModel!!.description
        binding.tvPrice.text =  bookingModel!!.price + " SAR "  + ",7"
        binding.tvRating.text = bookingModel!!.ratting_avg

     //   binding.tvBedroom.text =   bookingModel!!.bedrooms + " bedrooms"
      //  binding.tvGuest.text =   bookingModel!!.guests + " guests max."
        //  binding.tvHouse.text =  propertyModel!!.house + " House"
        //   binding.tvSize.text =  propertyModel!!.size + " m²"
     //   binding.tvBathroom.text =  bookingModel!!.bathrooms + " bathrooms"


        binding.tvBedroom.text =   bookingModel!!.bedrooms + " "+ getString(R.string.bedrooms)
        binding.tvGuest.text =   bookingModel!!.guests + " " + getString(R.string.guest_max)
        binding.tvHouse.text =  bookingModel!!.rentStringName
        binding.tvSize.text =  bookingModel!!.squreMeter + " m²"
        binding.tvBathroom.text =  bookingModel!!.bathrooms + " " + getString(R.string.bathrooms)

        if(bookingModel!!.ratting_avg=="0.0") {
            binding.iv3.visibility = View.GONE
            binding.tvRating.visibility = View.GONE
        }
        else {
            binding.iv3.visibility = View.VISIBLE
            binding.tvRating.visibility = View.VISIBLE
        }

        if(bookingModel!!.wifi) {
            binding.iv1.visibility = View.VISIBLE
            binding.tvWifi.visibility = View.VISIBLE
        }
        else {
            binding.iv1.visibility = View.GONE
            binding.tvWifi.visibility = View.GONE
        }

        bindObservers();

        getAllReviews();

    }


    private fun startAutoSlide() {
        val update = Runnable {
            if (currentPage ==  bookingModel!!.imageUrls.size) {
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

    private fun getAllReviews(){
        val getReviewRequest = getReviewRequest()
        Log.e("get All review Request===", getReviewRequest.toString())
        Helper.showProgressMessage(this@PropertyDetailAct,getString(R.string.please_wait))
        sellerDataViewModel.getReviewsProvider(getReviewRequest)
    }

    private fun bindObservers() {
        sellerDataViewModel.reviewResponseLiveData.observe(this, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get review on property Response===", jsonObject.toString())
                            if (jsonObject.getString("status") == "1") {
                                val reviewModel: ReviewModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    ReviewModel::class.java
                                )
                                binding.tvReview.visibility = View.VISIBLE
                                arrayList.clear()
                                arrayList.addAll(reviewModel.data)
                                reviewAdapter.notifyAdapter(arrayList)

                                sellerDataViewModel.clearReviewData()
                            } else {
                                binding.tvReview.visibility = View.GONE
                                arrayList.clear()
                                reviewAdapter.notifyAdapter(arrayList)



                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(this@PropertyDetailAct, getString(R.string.please_wait))
                }

                else -> {}
            }
        })

    }

    private fun getReviewRequest(): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "property_id" to bookingModel!!.propertyId,
            )

        }
    }


    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(this@PropertyDetailAct,text, Toast.LENGTH_SHORT).show()
    }


}