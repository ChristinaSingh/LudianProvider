package com.ludianseller.ui.booking

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.ActivityOrderDetailBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.booking.adapter.ImageSliderAdapter2
import com.ludianseller.ui.booking.adapter.PreviewAdapter11
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class OrderDetailAct : AppCompatActivity(){
    private lateinit var binding: ActivityOrderDetailBinding
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var arrayList : ArrayList<BookingModel.Data>?=null

    private var bookingModel : BookingModel.Data? =null
    private var imageArrayList : ArrayList<String> = ArrayList()
    private val sharedPrf by lazy { SharedPrf(this) }

    private var currentPage = 0
    private val DELAY_MS: Long = 3000 // Delay in milliseconds before auto sliding starts
    private val PERIOD_MS: Long = 3000 // Period in milliseconds between each slide
    private var position : Int = 0
    private val handler = Handler()
    private val timer = Timer()
    private var type:String=""


    private var orderStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra("user_id") != null) {
                Log.e("type====", intent.getStringExtra("type")!!)
                try {
                    if ("Complete" == intent.getStringExtra("type"))
                     {
                         startActivity(
                             Intent(
                                 this@OrderDetailAct,
                                 RateToUserAct::class.java)
                                 .putExtra("name",intent.getStringExtra("name"))
                                 .putExtra("user_id",intent.getStringExtra("user_id"))
                                 .putExtra("image",intent.getStringExtra("image")))


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        arrayList = ArrayList()

        bookingModel = intent.getSerializableExtra("BookingData") as BookingModel.Data?
        position  = intent.getStringExtra("position")!!.toInt()
        setViewData()

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.cardStaus.setOnClickListener {
            if(bookingModel!!.booking_request_status=="PENDING") {
                val bookingAcceptCancelRequest =
                    bookingAcceptCancelRequest(bookingModel!!.booking_request_id, "ACCEPT")
                Log.e("Booking Accept Cancel Request===", bookingAcceptCancelRequest.toString())
                Helper.showProgressMessage(this@OrderDetailAct, getString(R.string.please_wait))
                sellerDataViewModel.bookingAcceptCancel(bookingAcceptCancelRequest)
            }
        }


        binding.cardCancel.setOnClickListener {
            val bookingAcceptCancelRequest = bookingAcceptCancelRequest(bookingModel!!.booking_request_id,"CANCEL")
            Log.e("Booking Accept Cancel Request===", bookingAcceptCancelRequest.toString())
            Helper.showProgressMessage(this@OrderDetailAct,getString(R.string.please_wait))
            sellerDataViewModel.bookingAcceptCancel(bookingAcceptCancelRequest)
        }



        binding.cardChat.setOnClickListener {
            startActivity(Intent(this, ChatAct::class.java).apply {
                putExtra("UserId", bookingModel!!.property.user_detail.id)
                putExtra("UserName", bookingModel!!.property.user_detail.user_name)
                putExtra("UserImage", "")
                putExtra("id", sharedPrf.getStoredTag(SharedPrf.USER_ID))
                putExtra("name", "")
                putExtra("img", "")
                putExtra("bookingID", bookingModel!!.booking_request_id)

            })
        }


        binding.btnAllReviews.setOnClickListener {
            startActivity(Intent(this, CheckUserRateAct::class.java).apply {
                putExtra("user_id", bookingModel!!.property.user_detail.id)
                putExtra("name", bookingModel!!.property.user_detail.user_name)
                putExtra("image",  bookingModel!!.property.user_detail.image)


            })
        }


        bindObserver()


    }

    private fun setViewData() {

        imageArrayList.clear()
        imageArrayList.addAll(bookingModel!!.property.image_urls)

        binding.rvPreview.adapter = PreviewAdapter11(this@OrderDetailAct,imageArrayList)


        binding.viewPager.adapter = ImageSliderAdapter2(this@OrderDetailAct,imageArrayList)
        binding.dotsIndicator.setViewPager2(binding.viewPager)
        startAutoSlide()

        binding.tvName.text = bookingModel!!.property.title
        binding.tvAddress.text =  bookingModel!!.property.address
        binding.tvDescription.text =  bookingModel!!.property.description
        binding.tvPrice.text =  bookingModel!!.orderTotal +  " SAR "  + ",7"
        binding.tvStatus.text = bookingModel!!.booking_request_status
      //  binding.tvDate1.text =    getString(R.string.booking_date)+" : ${bookingModel!!.booking_request_date_start}"
        binding.tvDate1.text =    getString(R.string.booking_date)+" : ${formatDates(bookingModel!!.booking_request_date_start).joinToString(", ")}"
        binding.tvRating.text = bookingModel!!.property.average_rating

        binding.tvRating.text = bookingModel!!.property.average_rating
        binding.tvChildren.text = getString(R.string.children) + " : " + bookingModel!!.booking_request_children
        binding.tvPerson.text = getString(R.string.person) + " : " + bookingModel!!.booking_request_guests

        binding.tvBedroom.text =   bookingModel!!.property.bedrooms + " "+ getString(R.string.bedrooms)
        binding.tvGuest.text =   bookingModel!!.property.guests + " " + getString(R.string.guest_max)
        binding.tvHouse.text =  bookingModel!!.property.rent_string_name
        binding.tvSize.text =  bookingModel!!.property.squreMeter + " m²"
        binding.tvBathroom.text =  bookingModel!!.property.bathrooms + " " + getString(R.string.bathrooms)

        binding.tvGuestName.text =  bookingModel!!.property.user_detail.first_name +  " " + bookingModel!!.property.user_detail.last_name
        binding.ratingBar.rating =  bookingModel!!.property.user_detail.user_average_rating.toFloat()

        Glide.with(this@OrderDetailAct)
            .load(bookingModel!!.property.user_detail.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(R.drawable.user_default)
            .error(R.drawable.user_default)
            .centerInside()
            .into(binding.ivImg )



        if(bookingModel!!.property.average_rating=="0.0") {
            binding.iv3.visibility = View.GONE
            binding.tvRating.visibility = View.GONE
        }
        else {
            binding.iv3.visibility = View.VISIBLE
            binding.tvRating.visibility = View.VISIBLE
        }

        if(bookingModel!!.property.wifi) {
            binding.iv1.visibility = View.VISIBLE
            binding.tvWifi.visibility = View.VISIBLE
        }
        else {
            binding.iv1.visibility = View.GONE
            binding.tvWifi.visibility = View.GONE
        }

        if(bookingModel!!.booking_request_status=="ACCEPT"){
            binding.cardCancel.visibility = View.GONE
            binding.cardStaus.visibility = View.GONE
            binding.cardChat.visibility = View.VISIBLE

        }

       else if(bookingModel!!.booking_request_status=="COMPLETED"){
            binding.cardCancel.visibility = View.GONE
            binding.cardStaus.visibility = View.VISIBLE
            binding.cardChat.visibility = View.GONE
            binding.tvStatus.text = getString(R.string.completed)

        }

        else {
            binding.cardCancel.visibility = View.VISIBLE
            binding.cardChat.visibility = View.GONE
            binding.cardStaus.visibility = View.VISIBLE
            binding.tvStatus.text = getString(R.string.accept)

        }

    }


    private fun startAutoSlide() {
        val update = Runnable {
            if (currentPage ==  bookingModel!!.property.image_urls.size) {
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



    private fun bookingAcceptCancelRequest(bookingId:String,bookingType:String): Map<String, String> {
        return binding.run {
            mapOf(
                "booking_id" to bookingId,
                "type" to bookingType
            )

        }
    }


    private fun getBookingRequest(bookingType:String): Map<String, String> {
        return binding.run {
            mapOf(
                //"user_id" to sharedPrf.getStoredTag(SharedPrf.USER_ID),
                "type" to bookingType
            )

        }
    }



    private fun bindObserver(){
        sellerDataViewModel.bookingLiveData.observe(this, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get booking Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                            val bookingModel11: BookingModel = Gson().fromJson(
                                jsonObject.toString(),
                                BookingModel::class.java
                            )
                            arrayList!!.clear()
                            arrayList!!.addAll(bookingModel11.data)
                            for (i in 0 until arrayList!!.size) {
                               if(bookingModel!!.booking_request_id == arrayList!![i].booking_request_id ){
                                   bookingModel = arrayList!![i]
                               }
                            }

                            setViewData()

                            sellerDataViewModel.clearBookingData()

                        }
                        else {


                            /* Toast.makeText(
                                 requireActivity(),
                                 "" + jsonObject.getString("message"),
                                 Toast.LENGTH_SHORT
                             ).show()*/
                        }

                        //  Log.e("TAG", "observers: $it.")
                    }

                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(this@OrderDetailAct,getString(R.string.please_wait))
                }

                else -> {}
            }
        })

        sellerDataViewModel.bookingAcceptCancelLiveData.observe(this, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("booking Accept Cancel Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                            val getBookingRequest = getBookingRequest("upcoming")
                            Log.e("Get Booking Request===", getBookingRequest.toString())
                            Helper.showProgressMessage(this@OrderDetailAct,getString(R.string.please_wait))
                            sellerDataViewModel.getBooking(getBookingRequest)

                            sellerDataViewModel.clearBookingAcceptCancelData()

                        }
                        else {
                            Toast.makeText(
                                this@OrderDetailAct,
                                "" + jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        //  Log.e("TAG", "observers: $it.")
                    }

                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(this@OrderDetailAct,getString(R.string.please_wait))
                }

                else -> {}
            }
        })




    }

    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(this@OrderDetailAct,text, Toast.LENGTH_SHORT).show()
    }


    private fun formatDates(dateString: String): List<String> {
        // Define the output date format
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

        // Split the input string by commas
        val dateList = dateString.split(",")

        // Format each date and store them in a list
        return dateList.map { dateString ->
            // Parse each date string into LocalDate and format it
            val date = LocalDate.parse(dateString)
            date.format(outputFormatter)
        }

    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(
                orderStatusReceiver,
                IntentFilter("check_status"),
                RECEIVER_EXPORTED
            )
        } else {
            registerReceiver(orderStatusReceiver, IntentFilter("check_status"))
        }
    }


    override fun onStop() {
        super.onStop()
        unregisterReceiver(orderStatusReceiver)

    }

}