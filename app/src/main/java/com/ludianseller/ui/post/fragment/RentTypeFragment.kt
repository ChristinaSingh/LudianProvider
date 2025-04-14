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
import com.ludianseller.databinding.FragmentRentTypeBinding
import com.ludianseller.models.PlaceModel
import com.ludianseller.models.PlaceTypeModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.PlaceAdapter
import com.ludianseller.ui.post.adapter.PlaceTypeAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class RentTypeFragment : Fragment(), PlaceAdapter.OnPlaceListener {

    private var _binding: FragmentRentTypeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private lateinit var placeAdapter: PlaceAdapter
    private var placeArrayList: ArrayList<PlaceModel.Property>? = null
    private var check : Boolean = false
    private var propertyCategoryId : String ? = null
    private var propertyCategoryName : String ? = null
    private var rentId : String ? = null
    private var rentStringName : String ? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRentTypeBinding.inflate(inflater, container, false)
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


        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }



        binding.btnNext.setOnClickListener {
            if(check) {
                for (i in 0  until placeArrayList!!.size){
                    if(placeArrayList!![i].check){
                        rentId = placeArrayList!![i].id
                        rentStringName =  placeArrayList!![i].name
                    }
                }

                val  bundle = Bundle()
                bundle.putString("propertyCategoryId",propertyCategoryId)
                bundle.putString("propertyCategoryName",propertyCategoryName)
                bundle.putString("rentId",rentId)
                bundle.putString("rentStringName",rentStringName)

                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_rentTypeFragmentFragment_to_propertyAddressFragment,bundle)
            }
            else showValidationErrors(getString(R.string.please_select_place))

        }


        placeArrayList = ArrayList()

        placeAdapter = PlaceAdapter(requireActivity(), placeArrayList, this@RentTypeFragment)
        binding.rvPlace.adapter = placeAdapter


        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyPlace(sharedPrf.getStoredTag(Constants.USER_TOKEN))

    }

    private fun bindObservers() {
        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get property place Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val placeModel: PlaceModel = Gson().fromJson(
                                jsonObject.toString(),
                                PlaceModel::class.java
                            )
                            placeArrayList!!.clear()
                            placeArrayList!!.addAll(placeModel.result)
                            placeAdapter.notifyAdapter(placeArrayList!!)
                            binding.rlMain.visibility = View.VISIBLE
                            binding.btnNext.visibility = View.VISIBLE
                            //   binding.shimmerLayout.stopShimmer();
                            //    binding.shimmerLayout.visibility = View.GONE;

                            sellerDataViewModel.clearData()
                        } else {
                            placeArrayList!!.clear()
                            placeAdapter.notifyAdapter(placeArrayList!!)
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

    override fun onPlace(propertyList: ArrayList<PlaceModel.Property>, position: Int) {
        placeArrayList = propertyList
        placeAdapter.notifyAdapter(placeArrayList!!)
        check = true
    }


}