package com.ludianseller.ui.post.fragment

import android.app.DatePickerDialog
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentAddDiscountBinding
import com.ludianseller.models.DiscountModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.DiscountAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Calendar

@AndroidEntryPoint
class AddDiscountFragment :Fragment(), DiscountAdapter.OnDiscountListener {
    private var _binding: FragmentAddDiscountBinding? = null
    private val binding get() = _binding!!
    private lateinit var discountAdapter: DiscountAdapter
    private var arrayList : ArrayList<DiscountModel.Discount>?=null
    private lateinit var sellerDataViewModel: SellerDataViewModel


    private var propertyCategoryId : String ? = null
    private var propertyCategoryName : String ? = null
    private var rentId : String ? = null
    private var rentStringName : String ? = null
    private var lat : String ? = null
    private var lon : String ? = null
    private var address : String ? = null
    private var bathroomCount : String ? = null
    private var bedCount : String ? = null
    private var bedroomCount : String ? = null
    private var guestQuantity : String ? = null
    private var selectFacilityId : String ? = null
    private var selectFacilityName : String ? = null
    private var selectAmenitiesId : String ? = null
    private var selectAmenitiesName : String ? = null
    private var selectSafetyId : String ? = null
    private var selectSafetyName : String ? = null
    private var picsList: ArrayList<Bitmap>? = null

    private var title : String ? = null
    private var description : String ? = null
    private var requestAcceptTypeId : String ? = null
    private var requestAcceptTypeName : String ? = null
    private var price : String ? = null
    private var discountId : String ? = null
    private var pets : String ? = null
    private var squreMeter : String ? = null
    private var startDate : String = ""
    private var endDate : String = ""
    private var discountPercent : String = ""
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddDiscountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }



    private fun initViews() {
        arrayList = ArrayList()

      //  arrayList!!.add(DiscountModel("20%","New listing promotion","Offer 20% off your first 3 bookings",true))
      //  arrayList!!.add(DiscountModel("10%","Weekly discount","For stays of 7 nights or more",true))
     //   arrayList!!.add(DiscountModel("24%","Monthly discount","For stays of 28 nights or more",true))


        propertyCategoryId = arguments?.getString("propertyCategoryId")
        propertyCategoryName = arguments?.getString("propertyCategoryName")
        rentId = arguments?.getString("rentId")
        rentStringName = arguments?.getString("rentStringName")
        lat = arguments?.getString("lat")
        lon = arguments?.getString("lon")
        address = arguments?.getString("address")
        bathroomCount = arguments?.getString("bathroomCount")
        bedCount = arguments?.getString("bedCount")
        bedroomCount  = arguments?.getString("bedroomCount")
        guestQuantity  = arguments?.getString("guestQuantity")
        selectFacilityId  = arguments?.getString("selectFacilityId")
        selectFacilityName  = arguments?.getString("selectFacilityName")
        selectAmenitiesId  = arguments?.getString("selectAmenitiesId")
        selectAmenitiesName  = arguments?.getString("selectAmenitiesName")
        selectSafetyId  = arguments?.getString("selectSafetyId")
        selectSafetyName  = arguments?.getString("selectSafetyName")
        picsList = arguments?.getParcelableArrayList<Bitmap>("picsList")
        title  = arguments?.getString("title")
        description  = arguments?.getString("description")
        requestAcceptTypeId  = arguments?.getString("requestAcceptTypeId")
        requestAcceptTypeName  = arguments?.getString("requestAcceptTypeName")
        price  = arguments?.getString("price")
        pets  = arguments?.getString("pets")
        squreMeter  = arguments?.getString("squreMeter")

        discountAdapter = DiscountAdapter(requireActivity(),arrayList,this@AddDiscountFragment)
        binding.rvDiscount.adapter = discountAdapter



        binding.btnNext.setOnClickListener {
           if(binding.edTitle.text.toString()=="")
               Toast.makeText(requireActivity(),getString(R.string.enter_title),Toast.LENGTH_LONG).show()
          else  if(discountPercent=="")
               Toast.makeText(requireActivity(),getString(R.string.enter_discount_percent),Toast.LENGTH_LONG).show()
           else  if(startDate=="")
               Toast.makeText(requireActivity(),getString(R.string.enter_coupon_activation_date),Toast.LENGTH_LONG).show()
           else  if(endDate=="")
               Toast.makeText(requireActivity(),getString(R.string.enter_coupon_expiry_date),Toast.LENGTH_LONG).show()

          else {
               val addDiscountCouponRequest = addDiscountCouponRequest()
               Log.e("add discount coupon Request===", addDiscountCouponRequest.toString())
               Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
               sellerDataViewModel.addDiscountCoupon(addDiscountCouponRequest)
          }
        }

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyDiscount(sharedPrf.getStoredTag(Constants.USER_TOKEN))



        binding.tvActivationDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the selected date
                    val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    // Update the TextView with the selected date
                    startDate =  formattedDate
                    binding.tvActivationDate.text = startDate
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }


        binding.tvExpiryDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the selected date
                    val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    // Update the TextView with the selected date
                    endDate =  formattedDate
                    binding.tvExpiryDate.text = endDate
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        binding.tvDiscountPercent.setOnClickListener { it ->
            val numbers = (1..100).toList()
            showDropDownDiscountPercentList(it,binding.tvDiscountPercent,numbers.map { it.toString() })
        }


    }


    private fun addDiscountCouponRequest(): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to  sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "property_id" to "0",
                "seller_id" to  sharedPrf.getStoredTag(SharedPrf.USER_ID),
                "title" to binding.edTitle.text.toString(),
                "discount_percent" to discountPercent,
                "start_date" to startDate,
                "end_date" to endDate,
                )

        }
    }



    private fun bindObservers() {
        sellerDataViewModel.documentLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get propertyDiscount Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {
                                val discountModel: DiscountModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    DiscountModel::class.java
                                )
                                arrayList!!.clear()
                                arrayList!!.addAll(discountModel.result)
                                discountAdapter.notifyAdapter(arrayList!!)


                                sellerDataViewModel.clearDocumentData()
                            } else {
                                arrayList!!.clear()
                                discountAdapter.notifyAdapter(arrayList!!)

                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            //  Log.e("TAG", "observers: $it.")
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                is NetworkResult.Error -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })


        sellerDataViewModel.discountCouponLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("add discount coupon Response===", jsonObject.toString())
                            if (jsonObject.getString("status") == "1") {
                                discountId = jsonObject.getString("discount_id")
                                val bundle = Bundle()
                                bundle.putString("propertyCategoryId", propertyCategoryId)
                                bundle.putString("propertyCategoryName", propertyCategoryName)
                                bundle.putString("rentId", rentId)
                                bundle.putString("rentStringName", rentStringName)
                                bundle.putString("lat", lat)
                                bundle.putString("lon", lon)
                                bundle.putString("address", address)
                                bundle.putString("bathroomCount", bathroomCount)
                                bundle.putString("bedCount", bedCount)
                                bundle.putString("bedroomCount", bedroomCount)
                                bundle.putString("guestQuantity", guestQuantity)
                                bundle.putString("selectFacilityId", selectFacilityId)
                                bundle.putString("selectFacilityName", selectFacilityName)
                                bundle.putString("selectAmenitiesId", selectAmenitiesId)
                                bundle.putString("selectAmenitiesName", selectAmenitiesName)
                                bundle.putString("selectSafetyId", selectSafetyId)
                                bundle.putString("selectSafetyName", selectSafetyName)
                                bundle.putParcelableArrayList("picsList", picsList!!)
                                bundle.putString("title", title)
                                bundle.putString("description", description)
                                bundle.putString("requestAcceptTypeId", requestAcceptTypeId)
                                bundle.putString("requestAcceptTypeName", requestAcceptTypeName)
                                bundle.putString("price", price)
                                bundle.putString("discountId", discountId)
                                bundle.putString("pets", pets)
                                bundle.putString("squreMeter", squreMeter)

                                Navigation.findNavController(binding.root)
                                    .navigate(R.id.action_addDiscountFragment_to_propertyReviewFragment, bundle)

                                sellerDataViewModel.clearDiscountCouponData()
                            } else {


                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            //  Log.e("TAG", "observers: $it.")
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                is NetworkResult.Error -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                }

                else -> {}
            }
        })



    }


    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }




    override fun onDiscount(propertyList: ArrayList<DiscountModel.Discount>, position: Int) {
        arrayList = propertyList
        discountAdapter.notifyAdapter(arrayList!!)
        discountId = arrayList!![position].id
    }


    private fun showDropDownDiscountPercentList(v: View, textView: TextView, stringList: List<String>) {
        val popupMenu = PopupMenu(requireActivity(), v)
        for (i in stringList.indices) {
            popupMenu.menu.add(stringList[i])
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            for (i in stringList.indices) {
                if (stringList[i].equals(menuItem.title.toString(), ignoreCase = true)) {
                    discountPercent = stringList[i]
                    textView.text = menuItem.title
                }
            }
            true
        }
        popupMenu.show()
    }

}