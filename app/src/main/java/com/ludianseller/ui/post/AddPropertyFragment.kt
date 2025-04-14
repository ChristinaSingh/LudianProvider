package com.ludianseller.ui.post

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
import com.ludianseller.R
import com.ludianseller.databinding.FragmentAddPropertyBinding
import com.ludianseller.ui.home.HomeAct
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Constants.USER_TOKEN
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class AddPropertyFragment : Fragment() {

    private var _binding: FragmentAddPropertyBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var  obj : JSONObject?=null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }

    private fun initViews() {

        binding.rlNewList.setOnClickListener {
            if((obj!!.getString("account_status")== "unverified" || obj!!.getString("account_status")== "pending" )){
            Toast.makeText(requireActivity(),getString(R.string.not_verify_by_admin_please_contact_to_admin),Toast.LENGTH_LONG).show()
            }
            else{
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addPropertyFragment_to_startFragment)
            }
        }


        binding.rlExitingList.setOnClickListener {
            if((obj!!.getString("account_status")== "unverified" || obj!!.getString("account_status")== "pending" )){
                Toast.makeText(requireActivity(),getString(R.string.not_verify_by_admin_please_contact_to_admin),Toast.LENGTH_LONG).show()
            }
            else{
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_addPropertyFragment_to_exitingFragment)
            }

        }

        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getUserProfile(sharedPrf.getStoredTag(USER_TOKEN))

    }


    private fun bindObservers() {
        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("user profile Response===", jsonObject.toString())
                        if (jsonObject.getString("status") == "1") {
                            obj = jsonObject.getJSONObject("data")
                            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
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

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }


}