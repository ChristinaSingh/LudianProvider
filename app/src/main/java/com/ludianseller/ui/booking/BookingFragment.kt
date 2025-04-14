package com.ludianseller.ui.booking

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.RECEIVER_EXPORTED
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentBookingBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.ui.booking.adapter.BookingCancelAdapter
import com.ludianseller.ui.booking.adapter.BookingCompleteAdapter
import com.ludianseller.ui.booking.adapter.BookingInProgressAdapter
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class BookingFragment : Fragment(), BookingInProgressAdapter.OnBookingAcceptCancelListener {

    private var _binding: FragmentBookingBinding? = null
    private lateinit var sellerDataViewModel: SellerDataViewModel

    private val binding get() = _binding!!
    private var tabSelection : Int = 1
    private var bookingType : String =""



    private lateinit var bookingInProgressAdapter: BookingInProgressAdapter
    private lateinit var bookingCancelAdapter: BookingCancelAdapter
    private lateinit var bookingCompleteAdapter: BookingCompleteAdapter

    private var arrayList : ArrayList<BookingModel.Data>?=null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }



    private var orderStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getStringExtra("user_id") != null) {
                Log.e("type====", intent.getStringExtra("type")!!)
                try {
                    if ("Complete" == intent.getStringExtra("type"))
                    {
                        startActivity(
                            Intent(
                                requireActivity(),
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
        return binding.root
    }

    private fun initViews() {

        arrayList = ArrayList()


        binding.tvForbidden.setOnClickListener { tabClick(1,binding.tvForbidden,binding.tvNightAvailability) }

        binding.tvNightAvailability.setOnClickListener { tabClick(2,binding.tvForbidden,binding.tvNightAvailability) }



        binding.llUpcoming.setOnClickListener {
            tabSelection =1
            tabBarChange(tabSelection)
        }
        binding.llPrevious.setOnClickListener {
            tabSelection =2
            tabBarChange(tabSelection)
        }

        binding.llCancelled.setOnClickListener {
            tabSelection =3
            tabBarChange(tabSelection)
        }



        binding.edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length >= 1) {
                    // Debounce the API request
                    val searchBookingListRequest = searchBookingListRequest(bookingType,s.toString())
                    Log.e("Search Booking List Request===", searchBookingListRequest.toString())
                  //  Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                    sellerDataViewModel.searchBookingListData(searchBookingListRequest)
                }
                else{
                    val getBookingRequest = getBookingRequest(bookingType)
                    Log.e("Get Booking Request===", getBookingRequest.toString())
                    Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                    sellerDataViewModel.getBooking(getBookingRequest)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })




        tabBarChange(tabSelection)

        bindObserver()

    }


    private fun tabBarChange(i : Int){
        when (i) {
            1 -> {
                binding.view1.visibility =View.VISIBLE
                binding.view2.visibility =View.GONE
                binding.view3.visibility =View.GONE
                bookingType = "upcoming"
                val getBookingRequest = getBookingRequest(bookingType)
                Log.e("Get Booking Request===", getBookingRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBooking(getBookingRequest)

            }
            2 -> {
                binding.view1.visibility =View.GONE
                binding.view2.visibility =View.VISIBLE
                binding.view3.visibility =View.GONE
                bookingType = "complete"
                val getBookingRequest = getBookingRequest(bookingType)
                Log.e("Get Booking Request===", getBookingRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBooking(getBookingRequest)

            }
            else -> {
                binding.view1.visibility =View.GONE
                binding.view2.visibility =View.GONE
                binding.view3.visibility =View.VISIBLE
                bookingType = "cancel"
                val getBookingRequest = getBookingRequest(bookingType)
                Log.e("Get Booking Request===", getBookingRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBooking(getBookingRequest)
            }
        }
    }


    private fun getBookingRequest(bookingType:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "type" to bookingType
            )

        }
    }


    private fun searchBookingListRequest(bookingType:String,searchKey:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "type" to bookingType,
                "search" to searchKey

            )

        }
    }





    private fun bookingAcceptCancelRequest(bookingId:String,bookingType:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "booking_id" to bookingId,
                "type" to bookingType
            )

        }
    }





    private fun tabClick(i : Int, tvForbidden : TextView, tvNightAvailability : TextView){
        if(i==1){
            tvForbidden.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.white))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.black))

        }
        else{
            tvForbidden.setBackgroundResource(R.drawable.rounded_gray_bg)
            tvNightAvailability.setBackgroundResource(R.drawable.rounded_brown_bg25)
            tvForbidden.setTextColor(requireActivity().getColor(R.color.black))
            tvNightAvailability.setTextColor(requireActivity().getColor(R.color.white))
        }
    }



    private fun bindObserver(){
        sellerDataViewModel.bookingLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get booking Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                            val bookingModel: BookingModel = Gson().fromJson(
                                jsonObject.toString(),
                                BookingModel::class.java
                            )
                            arrayList!!.clear()
                            arrayList!!.addAll(bookingModel.data)
                            binding.tvNotFound.visibility =View.GONE


                            if(tabSelection==1){
                                bookingInProgressAdapter = BookingInProgressAdapter(requireActivity(),arrayList,this@BookingFragment)
                                binding.rvBooking.adapter = bookingInProgressAdapter
                                bookingInProgressAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==2)  {
                                bookingCompleteAdapter = BookingCompleteAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCompleteAdapter
                                bookingCompleteAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==3)  {
                                bookingCancelAdapter = BookingCancelAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCancelAdapter
                                bookingCancelAdapter.notifyAdapter(arrayList!!)
                            }

                            sellerDataViewModel.clearBookingData()

                        }
                        else {

                            if(tabSelection==1){
                                arrayList!!.clear()
                                bookingInProgressAdapter = BookingInProgressAdapter(requireActivity(),arrayList,this@BookingFragment)
                                binding.rvBooking.adapter = bookingInProgressAdapter
                                bookingInProgressAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==2)  {
                                arrayList!!.clear()
                                bookingCompleteAdapter = BookingCompleteAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCompleteAdapter
                                bookingCompleteAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==3)  {
                                arrayList!!.clear()
                                bookingCancelAdapter = BookingCancelAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCancelAdapter
                                bookingCancelAdapter.notifyAdapter(arrayList!!)
                            }
                            binding.tvNotFound.visibility =View.VISIBLE
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
                    Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                }

                else -> {}
            }
        })

        sellerDataViewModel.bookingAcceptCancelLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("booking Accept Cancel Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                            tabBarChange(tabSelection)
                            sellerDataViewModel.clearBookingAcceptCancelData()

                        }
                        else {
                            Toast.makeText(
                                 requireActivity(),
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
                    Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                }

                else -> {}
            }
        })


        sellerDataViewModel.searchBookingLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("search booking list data Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                            val bookingModel: BookingModel = Gson().fromJson(
                                jsonObject.toString(),
                                BookingModel::class.java
                            )
                            arrayList!!.clear()
                            arrayList!!.addAll(bookingModel.data)
                            binding.tvNotFound.visibility =View.GONE


                            if(tabSelection==1){
                                bookingInProgressAdapter = BookingInProgressAdapter(requireActivity(),arrayList,this@BookingFragment)
                                binding.rvBooking.adapter = bookingInProgressAdapter
                                bookingInProgressAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==2)  {
                                bookingCompleteAdapter = BookingCompleteAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCompleteAdapter
                                bookingCompleteAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==3)  {
                                bookingCancelAdapter = BookingCancelAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCancelAdapter
                                bookingCancelAdapter.notifyAdapter(arrayList!!)
                            }

                            sellerDataViewModel.clearSearchBookingListData()

                        }
                        else {

                            if(tabSelection==1){
                                arrayList!!.clear()
                                bookingInProgressAdapter = BookingInProgressAdapter(requireActivity(),arrayList,this@BookingFragment)
                                binding.rvBooking.adapter = bookingInProgressAdapter
                                bookingInProgressAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==2)  {
                                arrayList!!.clear()
                                bookingCompleteAdapter = BookingCompleteAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCompleteAdapter
                                bookingCompleteAdapter.notifyAdapter(arrayList!!)
                            }
                            else if(tabSelection==3)  {
                                arrayList!!.clear()
                                bookingCancelAdapter = BookingCancelAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = bookingCancelAdapter
                                bookingCancelAdapter.notifyAdapter(arrayList!!)
                            }
                            binding.tvNotFound.visibility =View.VISIBLE
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
                   // Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                }

                else -> {}
            }
        })


    }


    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(),text, Toast.LENGTH_SHORT).show()
    }

    override fun onAcceptCancel(model: BookingModel.Data, position: Int, type: String) {
        val bookingAcceptCancelRequest = bookingAcceptCancelRequest(model.booking_request_id,type)
        Log.e("Booking Accept Cancel Request===", bookingAcceptCancelRequest.toString())
        Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
        sellerDataViewModel.bookingAcceptCancel(bookingAcceptCancelRequest)
    }


    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().registerReceiver(
                orderStatusReceiver,
                IntentFilter("check_status"),
                RECEIVER_EXPORTED
            )
        } else {
            requireActivity().registerReceiver(orderStatusReceiver, IntentFilter("check_status"))
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().unregisterReceiver(orderStatusReceiver)

    }


}