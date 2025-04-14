package com.ludianseller.ui.post.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ludianseller.R

import com.ludianseller.databinding.FragmentPropertyInsideBinding

class PropertyInsideFragment : Fragment() {
    private var _binding: FragmentPropertyInsideBinding? = null
    private val binding get() = _binding!!
    private var count1: Int = 0
    private var count2: Int = 0
    private var count3: Int = 0
    private var propertyCategoryId : String ? = null
    private var propertyCategoryName : String ? = null
    private var rentId : String ? = null
    private var rentStringName : String ? = null
    private var lat : String ? = null
    private var lon : String ? = null
    private var address : String ? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyInsideBinding.inflate(inflater, container, false)
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



        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {
            val  bundle = Bundle()
            bundle.putString("propertyCategoryId",propertyCategoryId)
            bundle.putString("propertyCategoryName",propertyCategoryName)
            bundle.putString("rentId",rentId)
            bundle.putString("rentStringName",rentStringName)
            bundle.putString("lat",lat)
            bundle.putString("lon",lon)
            bundle.putString("address",address)
            bundle.putString("bathroomCount",binding.tvBathroomCount.text.toString())
            bundle.putString("bedCount",binding.tvBedCount.text.toString())
            bundle.putString("bedroomCount",binding.tvBedroomCount.text.toString())

            Navigation.findNavController(binding.root)
                    .navigate(R.id.action_propertyInsideFragment_to_guestQuantityFragment,bundle)
        }

        binding.ivPlus1.setOnClickListener {
            count1++
            Log.e("count value===",count1.toString())
            binding.tvCount1.text = count1.toString()
            binding.tvBedroomCount.text = count1.toString()

        }


        binding.ivMinus1.setOnClickListener {
            if(count1>0) {
                count1--
                binding.tvCount1.text = count1.toString()
                binding.tvBedroomCount.text = count1.toString()
            }
        }


        binding.ivPlus2.setOnClickListener {
            count2++
            Log.e("count value===",count2.toString())
            binding.tvCount2.text = count2.toString()
            binding.tvBedCount.text = count2.toString()

        }


        binding.ivMinus2.setOnClickListener {
            if(count2>0) {
                count2--
                binding.tvCount2.text = count2.toString()
                binding.tvBedCount.text = count2.toString()
            }
        }



        binding.ivPlus3.setOnClickListener {
            count3++
            Log.e("count value===",count3.toString())
            binding.tvCount3.text = count3.toString()
            binding.tvBathroomCount.text = count3.toString()

        }


        binding.ivMinus3.setOnClickListener {
            if(count3>0) {
                count3--
                binding.tvCount3.text = count3.toString()
                binding.tvBathroomCount.text = count3.toString()
            }
        }


    }




}