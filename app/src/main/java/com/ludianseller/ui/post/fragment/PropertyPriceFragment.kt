package com.ludianseller.ui.post.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPropertyPriceBinding
import com.ludianseller.models.PropertyModel
import com.ludianseller.models.PropertyPriceModel
import com.ludianseller.ui.calendar.CalendarClickBottomSheet
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.CompareListingBottomSheet
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class PropertyPriceFragment  :  Fragment() {
    private var _binding: FragmentPropertyPriceBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private lateinit var propertyPriceModel: PropertyPriceModel
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
    private var pets : String ? = null
    private var squreMeter : String ? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyPriceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }

    private fun initViews() {

        picsList = ArrayList()

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
        pets  = arguments?.getString("pets")
        squreMeter  = arguments?.getString("squreMeter")


        binding.btnCompare.setOnClickListener {
            //CompareListingBottomSheet().show(childFragmentManager, "")
            val compareListingBottomSheet = CompareListingBottomSheet.newInstance(propertyPriceModel)
            compareListingBottomSheet.show(childFragmentManager, "")
        }


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {

           // val parts = binding.edPrice.text.toString().split("SAR")
            if(binding.edPrice.text.toString()==""){
                Toast.makeText(requireActivity(),getString(R.string.please_enter_price),Toast.LENGTH_LONG).show()
            } else{
                showDiscountDialog(binding.edPrice.text.trim().toString())

            }
           // showDiscountDialog(parts[0])

        }


        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getNearestPropertyPrice( sharedPrf.getStoredTag(Constants.USER_TOKEN),"22.7196","75.8577")



       // binding.edPrice.addTextChangedListener(textWatcher)


    }


/*
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            binding.edPrice.setText("$"+s.toString())
            val ss = s.toString()
            val ss22 = ss.toInt() + 1000
            binding.tvCompare.text = "Compare similar listings $ss - $ss22"
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {


           /// Compare similar listings 2,057 - ₹3,085

        }
        }
*/




    private fun showDiscountDialog(price:String) {
        val builder = AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.discount_coupon))
            .setMessage(getString(R.string.are_give_discount_coupon_for_this_property))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->

                dialog.dismiss()


                val  bundle = Bundle()
                bundle.putString("propertyCategoryId",propertyCategoryId)
                bundle.putString("propertyCategoryName",propertyCategoryName)
                bundle.putString("rentId",rentId)
                bundle.putString("rentStringName",rentStringName)
                bundle.putString("lat",lat)
                bundle.putString("lon",lon)
                bundle.putString("address",address)
                bundle.putString("bathroomCount",bathroomCount)
                bundle.putString("bedCount",bedCount)
                bundle.putString("bedroomCount",bedroomCount)
                bundle.putString("guestQuantity",guestQuantity)
                bundle.putString("selectFacilityId",selectFacilityId)
                bundle.putString("selectFacilityName",selectFacilityName)
                bundle.putString("selectAmenitiesId",selectAmenitiesId)
                bundle.putString("selectAmenitiesName",selectAmenitiesName)
                bundle.putString("selectSafetyId",selectSafetyId)
                bundle.putString("selectSafetyName",selectSafetyName)
                bundle.putParcelableArrayList("picsList", picsList!!)
                bundle.putString("title",title)
                bundle.putString("description",description)
                bundle.putString("requestAcceptTypeId",requestAcceptTypeId)
                bundle.putString("requestAcceptTypeName",requestAcceptTypeName)
                bundle.putString("price",price)
                bundle.putString("pets", pets)
                bundle.putString("squreMeter", squreMeter)

                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_propertyPriceFragment_to_addDiscountFragment,bundle)


            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
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
                bundle.putString("discountId", "")
                bundle.putString("pets", pets)
                bundle.putString("squreMeter", squreMeter)

                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_propertyPriceFragment_to_propertyReviewFragment, bundle)

            }

        // Show the dialog
        builder.show()
    }




    private fun bindObservers() {
        sellerDataViewModel.propertyResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get nearest property price Response===", jsonObject.toString())
                        if (jsonObject.getString("status") == "1") {
                             propertyPriceModel  = Gson().fromJson(
                                jsonObject.toString(),
                                PropertyPriceModel::class.java
                            )
                            sellerDataViewModel.clearPropertyData()
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

                is NetworkResult.Error -> {
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

}