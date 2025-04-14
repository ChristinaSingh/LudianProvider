package com.ludianseller.ui.post.fragment

import android.graphics.Bitmap
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
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentBookingConfirmationTypeSelectBinding
import com.ludianseller.models.ConfirmationModeModel
import com.ludianseller.models.PlaceModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.ConfirmationModeAdapter
import com.ludianseller.ui.post.adapter.PlaceAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class BookingConfirmationTypeSelectFragment :  Fragment(),
    ConfirmationModeAdapter.OnConfirmationListener {
    private var _binding: FragmentBookingConfirmationTypeSelectBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private lateinit var confirmationModeAdapter: ConfirmationModeAdapter
    private var confirmationArrayList: ArrayList<ConfirmationModeModel.Property>? = null
    private var check : Boolean = false
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
        _binding = FragmentBookingConfirmationTypeSelectBinding.inflate(inflater, container, false)

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
        pets  = arguments?.getString("pets")
        squreMeter  = arguments?.getString("squreMeter")

        confirmationArrayList = ArrayList()



        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {
            if(check) {
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
                bundle.putString("pets", pets)
                bundle.putString("squreMeter", squreMeter)

                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_bookingConfirmationTypeSelectFragment_to_propertyPriceFragment,bundle)
            }
            else showValidationErrors(getString(R.string.please_select_confirmation_mode))
        }


        confirmationModeAdapter = ConfirmationModeAdapter(requireActivity(), confirmationArrayList, this@BookingConfirmationTypeSelectFragment)
        binding.rvConfirm.adapter = confirmationModeAdapter


        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getTypeOfConfirmationMode(sharedPrf.getStoredTag(Constants.USER_TOKEN))



    }


    private fun bindObservers() {
        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get confirmation mode Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val confirmationModeModel: ConfirmationModeModel = Gson().fromJson(
                                jsonObject.toString(),
                                ConfirmationModeModel::class.java
                            )
                            confirmationArrayList!!.clear()
                            confirmationArrayList!!.addAll(confirmationModeModel.result)
                            confirmationModeAdapter.notifyAdapter(confirmationArrayList!!)
                            binding.rlMain.visibility = View.VISIBLE
                            binding.btnNext.visibility = View.VISIBLE
                            //   binding.shimmerLayout.stopShimmer();
                            //    binding.shimmerLayout.visibility = View.GONE;

                            sellerDataViewModel.clearData()
                        } else {
                            confirmationArrayList!!.clear()
                            confirmationModeAdapter.notifyAdapter(confirmationArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //   binding.shimmerLayout.visibility = View.GONE;
                            Toast.makeText(
                                requireActivity(),
                                "" + jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.rlMain.visibility = View.GONE
                            binding.btnNext.visibility = View.GONE

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

    override fun onConfirm(propertyList: ArrayList<ConfirmationModeModel.Property>, position: Int) {
        confirmationArrayList = propertyList
        confirmationModeAdapter.notifyAdapter(confirmationArrayList!!)
        check = true
        requestAcceptTypeId = confirmationArrayList!![position].id
        requestAcceptTypeName = confirmationArrayList!![position].name

    }


}