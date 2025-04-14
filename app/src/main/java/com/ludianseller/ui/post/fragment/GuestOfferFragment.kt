package com.ludianseller.ui.post.fragment

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
import com.ludianseller.databinding.FragmentGuestOfferBinding
import com.ludianseller.models.AmenitiesModel
import com.ludianseller.models.FacilitiesModel
import com.ludianseller.models.PlaceModel
import com.ludianseller.models.SafetyModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.AmenityAdapter
import com.ludianseller.ui.post.adapter.FacilityAdapter
import com.ludianseller.ui.post.adapter.SafetyAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class GuestOfferFragment : Fragment(), FacilityAdapter.OnFacilityListener,
    AmenityAdapter.OnAmenitiesListener, SafetyAdapter.OnSafetyListener {
    private var _binding: FragmentGuestOfferBinding? = null
        private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel

    private lateinit var amenityAdapter: AmenityAdapter
    private lateinit var facilityAdapter: FacilityAdapter
    private lateinit var safetyAdapter: SafetyAdapter



    private var amenitiesArrayList: ArrayList<AmenitiesModel.Property>? = null
    private var facilitiesArrayList: ArrayList<FacilitiesModel.Property>? = null
    private var safetyArrayList: ArrayList<SafetyModel.Property>? = null

    private var check : Boolean = false
    private var check2 : Boolean = false
    private var check3 : Boolean = false
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
    private var pets : String ? = null
    private var squreMeter : String ? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentGuestOfferBinding.inflate(inflater, container, false)
        return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
            initViews()
        }

        private fun initViews() {


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
            pets  = arguments?.getString("pets")
            squreMeter  = arguments?.getString("squreMeter")



            amenitiesArrayList = ArrayList()
            facilitiesArrayList = ArrayList()
            safetyArrayList = ArrayList()


            facilityAdapter = FacilityAdapter(requireActivity(),facilitiesArrayList,this@GuestOfferFragment)
            binding.rvFav.adapter = facilityAdapter

            amenityAdapter = AmenityAdapter(requireActivity(),amenitiesArrayList,this@GuestOfferFragment)
            binding.rvAmenity.adapter = amenityAdapter


            safetyAdapter = SafetyAdapter(requireActivity(),safetyArrayList,this@GuestOfferFragment)
            binding.rvSafety.adapter = safetyAdapter

            binding.ivBack.setOnClickListener {
                Navigation.findNavController(binding.root).navigateUp()
            }


            binding.btnNext.setOnClickListener {
                if(!check) showValidationErrors(getString(R.string.please_select_facility))
                else if(!check2) showValidationErrors(getString(R.string.please_select_amenities))
                else if(!check3) showValidationErrors(getString(R.string.please_select_safety))
                else{

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
                    bundle.putString("selectFacilityId",commaSepFacilities())
                    bundle.putString("selectFacilityName",selectFacilityName)
                    bundle.putString("selectAmenitiesId",commaSepAmenities())
                    bundle.putString("selectAmenitiesName",selectAmenitiesName)
                    bundle.putString("selectSafetyId",commaSepSafety())
                    bundle.putString("selectSafetyName",selectSafetyName)
                    bundle.putString("pets", pets)
                    bundle.putString("squreMeter", squreMeter)

                    Navigation.findNavController(binding.root)
                        .navigate(R.id.action_guestOfferFragment_to_picPhotoFragment,bundle)
                }


            }

            bindObservers()

            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
            sellerDataViewModel.getTypeOfFacilities(sharedPrf.getStoredTag(Constants.USER_TOKEN))

        }

    private fun bindObservers() {
        sellerDataViewModel.amenitiesResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                  try {
                      it.data?.let {
                          val jsonObject = JSONObject(it.string())
                          Log.e("get amenities Response===", jsonObject.toString())
                          if (jsonObject.getInt("status") == 1) {
                              val amenitiesModel: AmenitiesModel = Gson().fromJson(
                                  jsonObject.toString(),
                                  AmenitiesModel::class.java
                              )
                              amenitiesArrayList!!.clear()
                              amenitiesArrayList!!.addAll(amenitiesModel.result)
                              amenityAdapter.notifyAdapter(amenitiesArrayList!!)
                              binding.rlMain.visibility = View.VISIBLE
                              binding.btnNext.visibility = View.VISIBLE

                              Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                              sellerDataViewModel.getTypeOfSafetyItem(sharedPrf.getStoredTag(Constants.USER_TOKEN))

                              sellerDataViewModel.clearAmenitiesData()
                          } else {
                              amenitiesArrayList!!.clear()
                              amenityAdapter.notifyAdapter(amenitiesArrayList!!)

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


        sellerDataViewModel.facilityResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get facilities Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val facilitiesModel: FacilitiesModel = Gson().fromJson(
                                jsonObject.toString(),
                                FacilitiesModel::class.java
                            )
                            facilitiesArrayList!!.clear()
                            facilitiesArrayList!!.addAll(facilitiesModel.result)
                            facilityAdapter.notifyAdapter(facilitiesArrayList!!)
                            binding.rlMain.visibility = View.VISIBLE
                            binding.btnNext.visibility = View.VISIBLE

                            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                            sellerDataViewModel.getTypeOfAmenities(sharedPrf.getStoredTag(Constants.USER_TOKEN))

                            sellerDataViewModel.clearFacilitiesData()
                        } else {
                            facilitiesArrayList!!.clear()
                            facilityAdapter.notifyAdapter(facilitiesArrayList!!)

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


        sellerDataViewModel.safetyResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get safety Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {
                                val safetyModel: SafetyModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    SafetyModel::class.java
                                )
                                safetyArrayList!!.clear()
                                safetyArrayList!!.addAll(safetyModel.result)
                                safetyAdapter.notifyAdapter(safetyArrayList!!)
                                binding.rlMain.visibility = View.VISIBLE
                                binding.btnNext.visibility = View.VISIBLE

                                sellerDataViewModel.clearAmenitiesData()
                            } else {
                                safetyArrayList!!.clear()
                                safetyAdapter.notifyAdapter(safetyArrayList!!)

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

                    }catch (e:Exception){
                        e.printStackTrace()
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




    override fun onFacility(model: ArrayList<FacilitiesModel.Property>, position: Int) {
        facilitiesArrayList = model
        facilityAdapter.notifyAdapter(facilitiesArrayList!!)
        check = true
        selectFacilityId = facilitiesArrayList!![position].id
        selectFacilityName = facilitiesArrayList!![position].name

    }

    override fun onAmenities(model: ArrayList<AmenitiesModel.Property>, position: Int) {
        amenitiesArrayList = model
        amenityAdapter.notifyAdapter(amenitiesArrayList!!)
        check2 = true
        selectAmenitiesId = amenitiesArrayList!![position].id
        selectAmenitiesName = amenitiesArrayList!![position].name
    }

    override fun onSafety(model: ArrayList<SafetyModel.Property>, position: Int) {
        safetyArrayList = model
        safetyAdapter.notifyAdapter(safetyArrayList!!)
        check3 = true
        selectSafetyId = safetyArrayList!![position].id
        selectSafetyName = safetyArrayList!![position].name
    }

    private fun commaSepFacilities() : String {
        val commaSeparatedNames = StringBuilder()

        for ((index, obj) in facilitiesArrayList!!.withIndex()) {
            if (index < facilitiesArrayList!!.size - 1) {
                if (obj.check) {
                    commaSeparatedNames.append(obj.id)
                    commaSeparatedNames.append(",")
                }            }
        }

        Log.e("comma separated Facilities===",commaSeparatedNames.dropLast(1).toString())

        return  commaSeparatedNames.dropLast(1).toString()
    }

    private fun commaSepAmenities() : String {
        val commaSeparatedNames = StringBuilder()

        for ((index, obj) in amenitiesArrayList!!.withIndex()) {
            if (index < amenitiesArrayList!!.size - 1) {
                if (obj.check) {
                    commaSeparatedNames.append(obj.id)
                    commaSeparatedNames.append(",")
                }            }
        }
        Log.e("comma separated Amenities===",commaSeparatedNames.dropLast(1).toString())


        return  commaSeparatedNames.dropLast(1).toString()
    }

    private fun commaSepSafety() : String {
        val commaSeparatedNames = StringBuilder()

        for ((index, obj) in safetyArrayList!!.withIndex()) {
            if (index < safetyArrayList!!.size - 1) {
                if (obj.check) {
                    commaSeparatedNames.append(obj.id)
                    commaSeparatedNames.append(",")
                }
            }
        }
        Log.e("comma separated Safety===",commaSeparatedNames.dropLast(1).toString())

        return  commaSeparatedNames.dropLast(1).toString()
    }


}


