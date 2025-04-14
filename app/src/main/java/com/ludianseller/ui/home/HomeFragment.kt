package com.ludianseller.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentHomeBinding
import com.ludianseller.models.BookingModel
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.ui.booking.adapter.ReservationAdapter
import com.ludianseller.ui.notification.NotificationAct
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Constants.USER_TOKEN
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject

@AndroidEntryPoint
class HomeFragment : Fragment(){

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var calenderPropertyModel : CalenderPropertyModel?=null
    private lateinit var tabAdapter : TabAdapter
    private var tabArrayList: ArrayList<String>? = null
    private var  obj : JSONObject?=null
    private var propertyData: String = "0"
    private var tabSelection : Int =1
    private var arrayList : ArrayList<BookingModel.Data>?=null
    private lateinit var reservationAdapter: ReservationAdapter
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }

    private fun initViews() {
        tabArrayList = ArrayList()
        arrayList = ArrayList()

        tabArrayList!!.add( getString(R.string.checking_out))
        tabArrayList!!.add(getString(R.string.currently_hosting))
        tabArrayList!!.add(getString(R.string.arriving_soon))

        reservationAdapter = ReservationAdapter(requireActivity(),arrayList)
        binding.rvBooking.adapter = reservationAdapter
        reservationAdapter.notifyAdapter(arrayList!!)


        binding.ivChat.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationAct::class.java))
        }


        binding.rlComplete.setOnClickListener {
            tabSelection =1
            tabBarChange(tabSelection)
        }
        binding.rlInProgress.setOnClickListener {
            tabSelection =2
            tabBarChange(tabSelection)
        }

        binding.rlCancel.setOnClickListener {
            tabSelection =3
            tabBarChange(tabSelection)
        }



        tabAdapter = TabAdapter(requireActivity(),tabArrayList)
        binding.rvTab.adapter = tabAdapter


        binding.cardVerify.setOnClickListener {
           // if( propertyData== "1"  && calenderPropertyModel!!.properties.isNotEmpty()){
         //       val  bundle = Bundle()
            //    bundle.putString("name",calenderPropertyModel!!.properties[0].title)
            //    bundle.putString("address",calenderPropertyModel!!.properties[0].address)
           //     bundle.putString("image",calenderPropertyModel!!.properties[0].imageUrls[0])

        //    Navigation.findNavController(binding.root)
        //        .navigate(R.id.action_homeFragment_to_keyStoreFragment,bundle)
      //      }

       //     else {
        //         Toast.makeText(requireActivity(),getString(R.string.please_add_property), Toast.LENGTH_SHORT).show()
         //   }

            if((obj!!.getString("account_status")== "unverified" || obj!!.getString("account_status")== "pending" )){
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_homeFragment_to_keyStoreFragment)
            }
        }

        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getUserProfile(sharedPrf.getStoredTag(USER_TOKEN))


        tabSelection =1
        tabBarChange(tabSelection)

    }



    private fun tabBarChange(i : Int){
        when (i) {
            1 -> {
                binding.rlComplete.setBackgroundResource(R.drawable.rounded_brown_bg25)
                binding.rlInProgress.setBackgroundResource(R.drawable.rounded_gray_bg)
                binding.rlCancel.setBackgroundResource(R.drawable.rounded_gray_bg)

                binding.tvComplete.setTextColor(requireActivity().getColor(R.color.white))
                binding.tvComplete.text = getString(R.string.complete) + "(0)"
                binding.tvInProgress.setTextColor(requireActivity().getColor(R.color.black))
                binding.tvCancel.setTextColor(requireActivity().getColor(R.color.black))


                val getBookingRequest = getBookingRequest("complete")
                Log.e("Get Booking Request===", getBookingRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBooking(getBookingRequest)

            }
            2 -> {
                binding.rlComplete.setBackgroundResource(R.drawable.rounded_gray_bg)
                binding.rlInProgress.setBackgroundResource(R.drawable.rounded_brown_bg25)
                binding.rlCancel.setBackgroundResource(R.drawable.rounded_gray_bg)

                binding.tvComplete.setTextColor(requireActivity().getColor(R.color.black))
                binding.tvInProgress.setTextColor(requireActivity().getColor(R.color.white))
                binding.tvCancel.setTextColor(requireActivity().getColor(R.color.black))

                binding.tvInProgress.text = getString(R.string.in_progres) + "(0)"


                val getBookingRequest = getBookingRequest("upcoming")
                Log.e("Get Booking Request===", getBookingRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBooking(getBookingRequest)

            }
            else -> {
                binding.rlComplete.setBackgroundResource(R.drawable.rounded_gray_bg)
                binding.rlInProgress.setBackgroundResource(R.drawable.rounded_gray_bg)
                binding.rlCancel.setBackgroundResource(R.drawable.rounded_brown_bg25)

                binding.tvComplete.setTextColor(requireActivity().getColor(R.color.black))
                binding.tvInProgress.setTextColor(requireActivity().getColor(R.color.black))
                binding.tvCancel.setTextColor(requireActivity().getColor(R.color.white))

                binding.tvCancel.text = getString(R.string.cancelled) + "(0)"


                val getBookingRequest = getBookingRequest("cancel")
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



    private fun bindObservers() {

        sellerDataViewModel.bookingLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
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
                                    binding.tvComplete.text = getString(R.string.complete) + "(" + arrayList!!.size.toString() + ")"

                                }
                                else if(tabSelection==2)  {
                                    binding.tvInProgress.text = getString(R.string.in_progres) + "(" + arrayList!!.size.toString() + ")"

                                }
                                else if(tabSelection==3)  {
                                    binding.tvCancel.text = getString(R.string.cancelled) + "(" + arrayList!!.size.toString() + ")"

                                }


                                reservationAdapter = ReservationAdapter(requireActivity(),arrayList)
                                binding.rvBooking.adapter = reservationAdapter
                                reservationAdapter.notifyAdapter(arrayList!!)


                                sellerDataViewModel.clearBookingData()

                            }
                            else {
                                arrayList!!.clear()
                                reservationAdapter.notifyAdapter(arrayList!!)

                                if(tabSelection==1){
                                    binding.tvComplete.text = getString(R.string.complete) + "(" + arrayList!!.size.toString() + ")"

                                }
                                else if(tabSelection==2)  {
                                    binding.tvInProgress.text = getString(R.string.in_progres) + "(" + arrayList!!.size.toString() + ")"

                                }
                                else if(tabSelection==3)  {
                                    binding.tvCancel.text = getString(R.string.cancelled) + "(" + arrayList!!.size.toString() + ")"

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
                    }catch (e:JSONException){
                        Log.e("JSON Error", "Failed to parse response", e)
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



        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("user profile Response===", jsonObject.toString())
                            if (jsonObject.getString("status") == "1") {
                                obj = jsonObject.getJSONObject("data")
                                binding.tvName.text = obj!!.getString("first_name") + " "  + obj!!.getString("last_name")
                                Glide.with(this)
                                    .load(obj!!.getString("image"))
                                    .error(R.drawable.user_default)
                                    .placeholder(R.drawable.user_default)
                                    .override(50,50)
                                    .into(binding.ivUserImg)

                                Helper.showProgressMessage(
                                    requireActivity(),
                                    getString(R.string.please_wait)
                                )
                                sellerDataViewModel.getProperty(sharedPrf.getStoredTag(Constants.USER_TOKEN))

                                sellerDataViewModel.clearData()
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            //  Log.e("TAG", "observers: $it.")
                        }
                    }
                    catch (e:JSONException){
                        Log.e("JSON Error", "Failed to parse response", e)
                    }
                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })


        sellerDataViewModel.propertyAddedDataLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get added property Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {
                                propertyData = "1"
                                calenderPropertyModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    CalenderPropertyModel::class.java
                                )

                                if((obj!!.getString("account_status")== "unverified" || obj!!.getString("account_status")== "pending" ) || obj!!.getString("account_status")== "uploaded"       /*&& propertyData=="0"*/)
                                {
                                    binding.cardVerify.visibility = View.VISIBLE
                                    if (obj!!.getString("account_status")== "uploaded"){
                                        binding.tvAddress.text = getString(R.string.waiting_for_admin_approval)
                                    }
                                    else binding.tvAddress.text = getString(R.string.requried_to_publish)


                                    // binding.tvName22.text = calenderPropertyModel!!.properties[0].title

                                }
                                else binding.cardVerify.visibility = View.GONE

                                sellerDataViewModel.clearAddPropertyData()
                            } else {
                                propertyData = "0"
                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            //  Log.e("TAG", "observers: $it.")
                        }
                    }
                    catch (e:JSONException){
                        Log.e("JSON Error", "Failed to parse response", e)
                    }



                }

                is NetworkResult.Error -> {
                    propertyData = "0"
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })


    }

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        // Cancel any pending tasks like network requests or JSON parsing
    }


}