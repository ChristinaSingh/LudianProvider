package com.ludianseller.ui.post.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ludianseller.R
import com.ludianseller.databinding.FragmentGuestQuantityBinding
import com.ludianseller.ui.post.adapter.ImageAdapter

class GuestQuantityFragment : Fragment() {

    private var _binding: FragmentGuestQuantityBinding? = null
    private val binding get() = _binding!!
    private lateinit var imageAdapter: ImageAdapter
    private var count: Int = 0
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
    private var pets : String ? = "0"



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuestQuantityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }

        binding.tvPet.setOnClickListener {
           if(pets=="0"){
              binding.ivSelect.setImageResource(R.drawable.ic_select_radio)
               pets = "1"
           }
            else {
               binding.ivSelect.setImageResource(R.drawable.ic_unselect_radio)
               pets = "0"
           }
        }






        binding.btnNext.setOnClickListener {

            if(binding.edDimension.text.toString()=="")
                Toast.makeText(requireActivity(),getString(R.string.please_enter_area),Toast.LENGTH_LONG).show()
            else {
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
                bundle.putString("guestQuantity", binding.tvCount.text.toString())
                bundle.putString("pets", pets)
                bundle.putString("squreMeter", binding.edDimension.text.toString())



                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_guestQuantityFragment_to_guestOfferFragment, bundle)
            }
        }

        imageAdapter = ImageAdapter(requireActivity())
        binding.rvImg.adapter = imageAdapter


        binding.ivPlus.setOnClickListener {
            count++
            Log.e("count value===",count.toString())
            binding.tvCount.text = count.toString()
            imageAdapter.itemCount = count
            binding.rvImg.visibility =View.GONE
        }


        binding.ivMinus.setOnClickListener {
            if(count>0) {
                count--
                binding.tvCount.text = count.toString()
                imageAdapter.itemCount = count
                binding.rvImg.visibility =View.GONE

            }
            else {
                binding.rvImg.visibility =View.GONE
            }
        }


    }



}