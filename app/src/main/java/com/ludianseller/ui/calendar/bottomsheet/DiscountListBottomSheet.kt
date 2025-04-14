package com.ludianseller.ui.calendar.bottomsheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.models.DiscountModel
import com.ludianseller.ui.calendar.model.CalendarModel
import com.ludianseller.ui.calendar.model.CouponModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.adapter.DiscountAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Calendar

@AndroidEntryPoint
class DiscountListBottomSheet : BottomSheetDialogFragment(), DiscountAdapter.OnDiscountListener {
    private lateinit var bottomSheetView: View
    private var calendarValue: CouponModel.Result? = null
    private var position: Int = 0
    private var mainPosition: Int = 0
    private var propertyId: String=""
    private lateinit var listener: DiscountSelectListener
    private lateinit var discountAdapter: DiscountAdapter
    private var arrayList : ArrayList<DiscountModel.Discount>?=null
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var discountId : String ? = null
    private lateinit var edTitle :EditText
    private lateinit var tvDiscountPercent :TextView
    private lateinit var tvActivationDate :TextView
    private lateinit var tvExpiryDate :TextView

    private var startDate : String = ""
    private var endDate : String = ""
    private var discountPercent : String = ""
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }

    companion object {
        fun newInstance(
            value: CouponModel.Result,
            propertyId:String,
            listener: DiscountSelectListener
        ): DiscountListBottomSheet {
            val fragment = DiscountListBottomSheet()
            fragment.calendarValue = value
            fragment.propertyId= propertyId
            fragment.listener = listener

            return fragment
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_discount_select, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        val rvDiscount = bottomSheetView.findViewById<RecyclerView>(R.id.rvDiscount)
        val ivBack = bottomSheetView.findViewById<ImageView>(R.id.ivBack)
        val btnSave = bottomSheetView.findViewById<Button>(R.id.btnSave)

         edTitle = bottomSheetView.findViewById<EditText>(R.id.edTitle)
         tvDiscountPercent = bottomSheetView.findViewById<TextView>(R.id.tvDiscountPercent)
         tvActivationDate = bottomSheetView.findViewById<TextView>(R.id.tvActivationDate)
         tvExpiryDate = bottomSheetView.findViewById<TextView>(R.id.tvExpiryDate)

        startDate = calendarValue!!.startDate
        endDate = calendarValue!!.endDate
        discountPercent = calendarValue!!.discountPercent


        edTitle.setText(calendarValue!!.title)
        tvDiscountPercent.text = calendarValue!!.discountPercent
         tvActivationDate.text = calendarValue!!.startDate
         tvExpiryDate.text = calendarValue!!.endDate


        arrayList = ArrayList()



        tvActivationDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the selected date
                    val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    // Update the TextView with the selected date
                    startDate =  formattedDate
                    tvActivationDate.text = startDate
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }


        tvExpiryDate.setOnClickListener{
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Create a DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                requireActivity(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Format the selected date
                    val formattedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    // Update the TextView with the selected date
                    endDate =  formattedDate
                    tvExpiryDate.text = endDate
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }

        tvDiscountPercent.setOnClickListener { it ->
            val numbers = (1..100).toList()
            showDropDownDiscountPercentList(it,tvDiscountPercent,numbers.map { it.toString() })
        }


        discountAdapter = DiscountAdapter(requireActivity(),arrayList,this@DiscountListBottomSheet)
        rvDiscount.adapter = discountAdapter

        ivBack.setOnClickListener {
            dialog!!.dismiss()
        }


    /*    btnSave.setOnClickListener {
            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
            val updatePropertyDiscountRequest  = updatePropertyDiscountRequest()
            Log.e("Update property discount Request===", updatePropertyDiscountRequest.toString())
            sellerDataViewModel.updatePropertyDiscount(updatePropertyDiscountRequest)
        }*/

        btnSave.setOnClickListener {
            if(edTitle.text.toString()=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_title),Toast.LENGTH_LONG).show()
            else  if(discountPercent=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_discount_percent),Toast.LENGTH_LONG).show()
            else  if(startDate=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_coupon_activation_date),Toast.LENGTH_LONG).show()
            else  if(endDate=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_coupon_expiry_date),Toast.LENGTH_LONG).show()

            else {
                val addDiscountCouponRequest = addDiscountCouponRequest()
                Log.e("add discount coupon Request===", addDiscountCouponRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
               // sellerDataViewModel.addDiscountCoupon(addDiscountCouponRequest)
                   sellerDataViewModel.updateDiscountCoupon(addDiscountCouponRequest)

            }
        }




        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyDiscount(sharedPrf.getStoredTag(Constants.USER_TOKEN))

    }


    private fun addDiscountCouponRequest(): Map<String, String> {
        return run {
            mapOf(
                "token" to  sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "coupon_id" to calendarValue!!.id,
                "title" to edTitle.text.toString(),
                "discount_percent" to discountPercent,
                "start_date" to startDate,
                "end_date" to endDate,
            )

        }
    }

    private fun showDropDownDiscountPercentList(v: View, textView: TextView, stringList: List<String>) {
        val popupMenu = PopupMenu(requireActivity(), v)
        for (i in stringList.indices) {
            popupMenu.menu.add(stringList[i])
        }

        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            for (i in stringList.indices) {
                if (stringList[i].equals(menuItem.title.toString(), ignoreCase = true)) {
                    discountPercent = stringList[i]
                    textView.text = menuItem.title
                }
            }
            true
        }
        popupMenu.show()
    }



    private fun updatePropertyDiscountRequest(): Map<String, String> {
        return run {
            mapOf(
                "property_id" to propertyId!!,
                "discount_id" to discountId.toString()
            )



        }
    }




    interface DiscountSelectListener {
        fun onDiscountSelect(value: CouponModel.Result, mainPosition: Int, position: Int)
    }






    private fun bindObservers() {
        sellerDataViewModel.documentLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get propertyDiscount Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {
                                val discountModel: DiscountModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    DiscountModel::class.java
                                )
                                arrayList!!.clear()
                                arrayList!!.addAll(discountModel.result)
                                discountAdapter.notifyAdapter(arrayList!!)

/*
                                if(arrayList!!.size>0) {
                                    for (i in 0 until arrayList!!.size) {
                                        if(arrayList!![i].discount== calendarValue!!.discount!!.discount)
                                        {
                                            arrayList!![i].check = true
                                        }
                                    }

                                }
*/




                                sellerDataViewModel.clearDocumentData()
                            } else {
                                arrayList!!.clear()
                                discountAdapter.notifyAdapter(arrayList!!)

                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

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

        sellerDataViewModel.updatePriceWithDayLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("update propertyDiscount Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {

                                listener.onDiscountSelect(calendarValue!!,mainPosition,position)
                                dialog!!.dismiss()
                                sellerDataViewModel.clearUpdatePriceData()
                            } else {

                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

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



        sellerDataViewModel.updateDiscountCouponLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("update discount coupon Response===", jsonObject.toString())
                            if (jsonObject.getString("status") == "1") {
                                discountId = jsonObject.getString("discount_id")
                             /*   calendarValue!!.discount!!.id =  discountId!!
                                calendarValue!!.discount!!.discount =  tvDiscountPercent.text.toString()
                                calendarValue!!.discount!!.title =  edTitle.text.toString()
                                calendarValue!!.discount!!.subTitle = getString(R.string.expire_on) +" : " + tvExpiryDate.text.toString()
                                calendarValue!!.discount!!.startDate = tvActivationDate.text.toString()
                                calendarValue!!.discount!!.endDate = tvExpiryDate.text.toString()*/



                                listener.onDiscountSelect(calendarValue!!,mainPosition,position)
                                dialog!!.dismiss()
                                sellerDataViewModel.clearDiscountCouponData()
                            } else {


                                Toast.makeText(
                                    requireActivity(),
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()

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



    }


    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    override fun onDiscount(propertyList: ArrayList<DiscountModel.Discount>, position: Int) {
        arrayList = propertyList
        discountAdapter.notifyAdapter(arrayList!!)
        discountId = arrayList!![position].id
     /*   calendarValue!!.discount!!.id =  arrayList!![position].id
        calendarValue!!.discount!!.discount =  arrayList!![position].discount
        calendarValue!!.discount!!.title =  arrayList!![position].title
        calendarValue!!.discount!!.subTitle =  arrayList!![position].subTitle*/


    }


}