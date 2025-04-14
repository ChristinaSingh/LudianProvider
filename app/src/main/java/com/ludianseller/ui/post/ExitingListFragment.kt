package com.ludianseller.ui.post

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
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentExitingBinding
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.ui.booking.OrderDetailAct
import com.ludianseller.ui.calendar.CalenderViewModel
import com.ludianseller.ui.post.adapter.ExitingPropertyAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class ExitingListFragment  : Fragment(), ExitingPropertyAdapter.OnItemListener {

    private var _binding: FragmentExitingBinding? = null
    private val binding get() = _binding!!
    private lateinit var exitingPropertyViewModel: ExitingPropertyViewModel
    private lateinit var exitingPropertyAdapter: ExitingPropertyAdapter
    private var exitingPropertyArrayList: ArrayList<CalenderPropertyModel.Property>? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExitingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        exitingPropertyViewModel = ViewModelProvider(this).get(ExitingPropertyViewModel::class.java)

        initViews()
    }

    private fun initViews() {

        exitingPropertyArrayList = ArrayList()

        exitingPropertyAdapter = ExitingPropertyAdapter(requireActivity(),exitingPropertyArrayList!!,this@ExitingListFragment)
        binding.rvProperty.adapter = exitingPropertyAdapter

        bindObservers()


        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        exitingPropertyViewModel.getExitingProperty(sharedPrf.getStoredTag(Constants.USER_TOKEN))


    }


    private fun bindObservers() {
        exitingPropertyViewModel.exitingPropertyDataLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get exiting property Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val calenderPropertyModel: CalenderPropertyModel = Gson().fromJson(
                                jsonObject.toString(),
                                CalenderPropertyModel::class.java
                            )
                            exitingPropertyArrayList!!.clear()
                            exitingPropertyArrayList!!.addAll(calenderPropertyModel.properties)
                            exitingPropertyAdapter.notifyAdapter(exitingPropertyArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //    binding.shimmerLayout.visibility = View.GONE;

                            exitingPropertyViewModel.clearData()
                        } else {
                            exitingPropertyArrayList!!.clear()
                            exitingPropertyAdapter.notifyAdapter(exitingPropertyArrayList!!)
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



    override fun onItemClick(
        mainPosition: Int,
        data: CalenderPropertyModel.Property,
        check: Boolean
    ) {
        startActivity(
            Intent(requireActivity(), PropertyDetailAct::class.java)
            .putExtra("BookingData",exitingPropertyArrayList!![mainPosition])
            .putExtra("position",mainPosition.toString()))
    }


}