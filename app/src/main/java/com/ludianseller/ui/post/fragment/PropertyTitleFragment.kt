package com.ludianseller.ui.post.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPropertyTitleBinding

class PropertyTitleFragment : Fragment() {
    private var _binding: FragmentPropertyTitleBinding? = null
    private val binding get() = _binding!!
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
    private var pets : String ? = null
    private var squreMeter : String ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyTitleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        pets  = arguments?.getString("pets")
        squreMeter  = arguments?.getString("squreMeter")

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {
             if(binding.edTitle.text.toString() =="") Toast.makeText(requireActivity(),getString(R.string.enter_title),Toast.LENGTH_LONG).show()
              else if(binding.edDescription.text.toString() =="") Toast.makeText(requireActivity(),getString(R.string.enter_description),Toast.LENGTH_LONG).show()
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
                 bundle.putString("selectFacilityId",selectFacilityId)
                 bundle.putString("selectFacilityName",selectFacilityName)
                 bundle.putString("selectAmenitiesId",selectAmenitiesId)
                 bundle.putString("selectAmenitiesName",selectAmenitiesName)
                 bundle.putString("selectSafetyId",selectSafetyId)
                 bundle.putString("selectSafetyName",selectSafetyName)
                 bundle.putParcelableArrayList("picsList", picsList!!)
                 bundle.putString("title",binding.edTitle.text.toString())
                 bundle.putString("description",binding.edDescription.text.toString())
                 bundle.putString("pets", pets)
                 bundle.putString("squreMeter", squreMeter)

                 Navigation.findNavController(binding.root)
                     .navigate(R.id.action_propertyTitleFragment_to_bookingConfirmationTypeSelectFragment,bundle)
             }

        }
    }

}