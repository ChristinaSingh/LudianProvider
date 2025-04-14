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
import com.ludianseller.databinding.FragmentDescribePlaceBinding
import com.ludianseller.models.PlaceTypeModel
import com.ludianseller.models.PropertyModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.PlaceTypeAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class DescribePlaceFragment : Fragment(), PlaceTypeAdapter.OnPlaceTypeListener {

    private var _binding: FragmentDescribePlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private lateinit var placeTypeAdapter: PlaceTypeAdapter
    private var propertyArrayList: ArrayList<PlaceTypeModel.Property>? = null
    private var check : Boolean = false
    private var selectString : String? =null
    private var selectId : String? =null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescribePlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }

    private fun initViews() {

        propertyArrayList = ArrayList()

        placeTypeAdapter = PlaceTypeAdapter(requireActivity(), propertyArrayList, this@DescribePlaceFragment)
        binding.rvPlaceType.adapter = placeTypeAdapter


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }



        binding.btnNext.setOnClickListener {
            if(check) {
                for (i in 0  until propertyArrayList!!.size){
                  if(propertyArrayList!![i].check){
                      selectId = propertyArrayList!![i].propertyCategoryId
                      selectString =  propertyArrayList!![i].propertyCategoryName
                  }
                }
                val  bundle = Bundle()
                bundle.putString("propertyCategoryId",selectId)
                bundle.putString("propertyCategoryName",selectString)
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_describePlaceFragment_to_rentTypeFragmentFragment,bundle)
            }
            else showValidationErrors(getString(R.string.please_select_category))

        }


        bindObservers()


        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyCategory(sharedPrf.getStoredTag(Constants.USER_TOKEN))



    }


    private fun bindObservers() {
        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get property category Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val placeTypeModel: PlaceTypeModel = Gson().fromJson(
                                jsonObject.toString(),
                                PlaceTypeModel::class.java
                            )
                            propertyArrayList!!.clear()
                            propertyArrayList!!.addAll(placeTypeModel.result)
                            placeTypeAdapter.notifyAdapter(propertyArrayList!!)
                            binding.rlMain.visibility = View.VISIBLE;
                            //   binding.shimmerLayout.stopShimmer();
                            //    binding.shimmerLayout.visibility = View.GONE;

                            sellerDataViewModel.clearData()
                        } else {
                            propertyArrayList!!.clear()
                            placeTypeAdapter.notifyAdapter(propertyArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //   binding.shimmerLayout.visibility = View.GONE;
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

    override fun onPlaceType(propertyList: ArrayList<PlaceTypeModel.Property>, position: Int) {
        propertyArrayList = propertyList
        placeTypeAdapter.notifyAdapter(propertyArrayList!!)
        check = true
    }


}