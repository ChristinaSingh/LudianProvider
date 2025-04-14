package com.ludianseller.ui.calendar

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
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentCalenderDataListBinding
import com.ludianseller.models.CalenderPropertyModel
import com.ludianseller.ui.calendar.adapter.CalenderListAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class CalenderDataListFragment : Fragment(), CalenderListAdapter.OnItemListener {
    private var _binding: FragmentCalenderDataListBinding? = null
    private val binding get() = _binding!!
    private lateinit var calenderViewModel: CalenderViewModel
    private lateinit var calenderListAdapter: CalenderListAdapter
    private var calenderArrayList: ArrayList<CalenderPropertyModel.Property>? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalenderDataListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        calenderViewModel = ViewModelProvider(this).get(CalenderViewModel::class.java)

        initViews()
    }


    private fun initViews() {
        calenderArrayList = ArrayList()

        calenderListAdapter = CalenderListAdapter(requireActivity(),calenderArrayList!!,this@CalenderDataListFragment)
        binding.rvMyList.adapter = calenderListAdapter
        currentDate()

        bindObservers()




        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        calenderViewModel.getProperty(sharedPrf.getStoredTag(Constants.USER_TOKEN))

    }

    private fun currentDate() {
        val currentDate = LocalDate.now()
        // Format the date to get the month and year
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH)
        val formattedDate = currentDate.format(formatter)
        binding.tvCurrentDate.text = formattedDate
    }


    private fun bindObservers() {
        calenderViewModel.propertyAddedDataLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get added property Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            val calenderPropertyModel: CalenderPropertyModel = Gson().fromJson(
                                jsonObject.toString(),
                                CalenderPropertyModel::class.java
                            )
                            binding.tvNotFound.visibility = View.GONE
                            calenderArrayList!!.clear()
                            calenderArrayList!!.addAll(calenderPropertyModel.properties)
                            calenderListAdapter.notifyAdapter(calenderArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //    binding.shimmerLayout.visibility = View.GONE;

                            calenderViewModel.clearData()
                        } else {
                            binding.tvNotFound.visibility = View.VISIBLE
                            calenderArrayList!!.clear()
                            calenderListAdapter.notifyAdapter(calenderArrayList!!)
                            //   binding.shimmerLayout.stopShimmer();
                            //   binding.shimmerLayout.visibility = View.GONE;
                          /*  Toast.makeText(
                                requireActivity(),
                                "" + jsonObject.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()*/
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
        val bundle = Bundle()
        bundle.putString("propertyId",data.propertyId)
        bundle.putString("price",data.price)
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_calenderPropertyList_to_calendarFragment,bundle)
    }


}