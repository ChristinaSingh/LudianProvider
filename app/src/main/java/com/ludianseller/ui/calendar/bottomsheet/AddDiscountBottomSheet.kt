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
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.ui.calendar.model.CalendarModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Calendar

@AndroidEntryPoint
class AddDiscountBottomSheet : BottomSheetDialogFragment() {
    private lateinit var bottomSheetView: View
    private var calendarValue: CalendarModel? = null
    private var startDate: String = ""
    private var endDate: String = ""
    private var discountPercent: String = ""
    private var propertyId: String = ""
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }

    private lateinit var edTitle: EditText
    private lateinit var tvDiscountPercent: TextView
    private lateinit var tvActivationDate: TextView
    private lateinit var tvExpiryDate: TextView
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var discountId : String ? = null
    private lateinit var listener: AddDiscountListener



    companion object {
        fun newInstance(
            propertyId:String,
            listener: AddDiscountListener
        ): AddDiscountBottomSheet {
            val fragment = AddDiscountBottomSheet()
            fragment.propertyId = propertyId
            fragment.listener = listener

            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.fragment_add_discount, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        edTitle = bottomSheetView.findViewById<EditText>(R.id.edTitle)
        tvDiscountPercent = bottomSheetView.findViewById<TextView>(R.id.tvDiscountPercent)
        tvActivationDate = bottomSheetView.findViewById<TextView>(R.id.tvActivationDate)
        tvExpiryDate = bottomSheetView.findViewById<TextView>(R.id.tvExpiryDate)
        val ivClose = bottomSheetView.findViewById<ImageView>(R.id.ivBack)
        val btnNext = bottomSheetView.findViewById<Button>(R.id.btnNext)



        ivClose.setOnClickListener {
            dialog!!.dismiss()
        }


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


        btnNext.setOnClickListener {
            if(edTitle.text.toString()=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_title), Toast.LENGTH_LONG).show()
            else  if(discountPercent=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_discount_percent), Toast.LENGTH_LONG).show()
            else  if(startDate=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_coupon_activation_date),
                    Toast.LENGTH_LONG).show()
            else  if(endDate=="")
                Toast.makeText(requireActivity(),getString(R.string.enter_coupon_expiry_date),
                    Toast.LENGTH_LONG).show()

            else {
                val addDiscountCouponRequest = addDiscountCouponRequest()
                Log.e("update discount coupon Request===", addDiscountCouponRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
             //   sellerDataViewModel.updateDiscountCoupon(addDiscountCouponRequest)
                sellerDataViewModel.addDiscountCoupon(addDiscountCouponRequest)

            }
        }

        bindObservers()

    }



    interface AddDiscountListener {
        fun onAddDiscount()
    }


    private fun addDiscountCouponRequest(): Map<String, String> {
        return run {
            mapOf(
                "token" to  sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "property_id" to propertyId,
                "seller_id" to  sharedPrf.getStoredTag(SharedPrf.USER_ID),
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



    private fun bindObservers() {

        sellerDataViewModel.discountCouponLiveData.observe(viewLifecycleOwner, Observer { it ->
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

                                listener.onAddDiscount()
                                dialog!!.dismiss()


                                sellerDataViewModel.clearUpdateDiscountCouponData()
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

}