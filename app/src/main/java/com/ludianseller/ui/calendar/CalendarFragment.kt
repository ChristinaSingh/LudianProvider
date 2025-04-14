package com.ludianseller.ui.calendar

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.FragmentCalendarBinding
import com.ludianseller.ui.booking.BookingCompleteInfoAct
import com.ludianseller.ui.calendar.adapter.VerticalCalendarAdapter
import com.ludianseller.ui.calendar.bottomsheet.AddDiscountBottomSheet
import com.ludianseller.ui.calendar.bottomsheet.DiscountBottomSheet
import com.ludianseller.ui.calendar.model.BookingDetailModel
import com.ludianseller.ui.calendar.model.CalendarModel
import com.ludianseller.ui.calendar.model.CheckPriceModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class CalendarFragment : Fragment(), VerticalCalendarAdapter.OnMonthListener,
    CalendarClickBottomSheet.CalendarClickListener, DiscountBottomSheet.DiscountListener,
    AddDiscountBottomSheet.AddDiscountListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private var selectedDate: LocalDate? = null
    private var arrayList : ArrayList<CalendarModel>? =null
    private var checkPriceArrayList : ArrayList<CheckPriceModel.Property>? =null
  //  private var discountModel : CheckPriceModel.Discount? =null


    private lateinit var verticalCalendarAdapter: VerticalCalendarAdapter
    private var price: String?=null
    private var propertyId: String?=null

    private var allMonthDates = mutableListOf<LocalDate>()
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        selectedDate = LocalDate.now()
        initViews()
        addMonth()

        return binding.root
    }

    private fun initViews() {

        price = arguments?.getString("price")
        propertyId = arguments?.getString("propertyId")

        arrayList = ArrayList()
        checkPriceArrayList = ArrayList()
        verticalCalendarAdapter = VerticalCalendarAdapter(requireActivity(),arrayList,this@CalendarFragment)
        binding.rvMainCalendar!!.adapter = verticalCalendarAdapter

        bindObservers()

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyPriceWithDay(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)


        binding.tvCoupon.setOnClickListener {
            startActivity(Intent(requireActivity(),CouponListAct::class.java)
                .putExtra("propertyId",propertyId))
        }

    }


    private fun addMonth(){
       // arrayList!!.clear()


        val previousMonth = selectedDate!!.minusMonths(1)
        arrayList!!.add(CalendarModel(monthYearFromDate(previousMonth!!)!!,previousMonth!!,daysInMonthArray(previousMonth!!)/*,CalendarModel.Discount("","","","","","0")*/))

        arrayList!!.add(CalendarModel(monthYearFromDate(selectedDate!!)!!,selectedDate!!,daysInMonthArray(selectedDate!!)/*,CalendarModel.Discount("","","","","","0")*/))
        for (i in 1..12) {
            selectedDate = selectedDate!!.plusMonths(1)
            arrayList!!.add(CalendarModel(monthYearFromDate(selectedDate!!)!!,selectedDate!!,daysInMonthArray(selectedDate!!)/*,CalendarModel.Discount("","","","","","0")*/))
        }




        if(checkPriceArrayList!!.size>0) {
            for (i in 0 until arrayList!!.size) {
                for (j in 0 until arrayList!![i].monthList.size) {
                    for (k in 0 until checkPriceArrayList!!.size) {
                        if(checkPriceArrayList!![k].date == arrayList!![i].monthList[j].date){
                            arrayList!![i].monthList[j].price = checkPriceArrayList!![k].price
                            arrayList!![i].monthList[j].openBlock = checkPriceArrayList!![k].avlStatus
                            arrayList!![i].monthList[j].note = checkPriceArrayList!![k].note
                           // arrayList!![i].monthList[j].discountValue = discountModel!!.discount
                        }
                    }
                }
            }

        }

/*
        if(discountModel!=null) {
            for (i in 0 until arrayList!!.size) {
                arrayList!![i].discount!!.id = discountModel!!.id
                arrayList!![i].discount!!.discount = discountModel!!.discount
                arrayList!![i].discount!!.title = discountModel!!.title
                arrayList!![i].discount!!.subTitle =  getString(R.string.expire_on) +" : "+discountModel!!.endDate
                arrayList!![i].discount!!.startDate = discountModel!!.startDate
                arrayList!![i].discount!!.endDate = discountModel!!.endDate



            }

        }
*/



        verticalCalendarAdapter.notifyValue(arrayList!!)
    }


    private fun daysInMonthArray(date: LocalDate): ArrayList<CalendarModel.Days> {
        val daysInMonthArray = ArrayList<CalendarModel.Days>()
        val yearMonth = YearMonth.from(date)
        val daysInMonth = yearMonth!!.lengthOfMonth()
        val firstOfMonth: LocalDate = selectedDate!!.withDayOfMonth(1)
        val dayOfWeek = firstOfMonth.dayOfWeek.value
        Log.e("yearMonth====",yearMonth.toString())
        Log.e("daysInMonth====",daysInMonth.toString())
        Log.e("firstOfMonth====",firstOfMonth.toString())
        Log.e("dayOfWeek====",dayOfWeek.toString())
        Log.e("date====",date.toString())
       val dateList =  getAllDatesMonth(yearMonth)
        for (i in 1..42) {
            if (i <= dayOfWeek || i > daysInMonth + dayOfWeek) {
                daysInMonthArray.add(CalendarModel.Days("","",false,"","","0",""/*,"0"*/))
            } else {
              //  daysInMonthArray.add((i - dayOfWeek).toString())
              //  daysInMonthArray.add(CalendarModel.Days((i - dayOfWeek).toString(),false,price!!))
                val currentDate = LocalDate.now()
                val day = i - dayOfWeek
                var dateType :String =""
                // Determine if this day is the current date
                Log.e("iiiiii", i.toString())
                Log.e("day", day.toString())

                val isCurrentDate = (day == currentDate.dayOfMonth && yearMonth == YearMonth.from(currentDate))
                val isPastDate = currentDate.isBefore(dateList[day-1])
                val isFutureDate = currentDate.isAfter(dateList[day-1])

                if (isCurrentDate) {
                    Log.e("DateType", "${dateList[day-1]} is the current date")
                    dateType = "current"
                } else if (!isPastDate) {
                    Log.e("DateType", "${dateList[day-1]} is a past date")
                  //  daysInMonthArray.add(CalendarModel.Days(day.toString(),false,price!!,"past"))
                    dateType = "past"

                } else if (!isFutureDate) {
                    Log.e("DateType", "${dateList[day-1]} is a future date")
                   // daysInMonthArray.add(CalendarModel.Days(day.toString(),false,price!!,"future"))
                    dateType = "future"

                }
                Log.e("DateType", dateType)

                daysInMonthArray.add(CalendarModel.Days(day.toString(),dateList[day-1].toString(),
                    false,price!!,dateType,"0",""/*,"0.0"*/))

            }
        }
        return daysInMonthArray
    }

    private fun getAllDatesMonth(yearMonth: YearMonth) :  MutableList<LocalDate> {
       val allMonthDates = mutableListOf<LocalDate>()

        // Start date is the first day of the current month
        val startDate = LocalDate.of(yearMonth.year, yearMonth.month, 1)

        // Iterate through each day of the month
        var currentDatePointer = startDate
        while (currentDatePointer.isBefore(startDate.plusMonths(1))) {
            allMonthDates.add(currentDatePointer)
            currentDatePointer = currentDatePointer.plusDays(1)
        }

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH)
        allMonthDates.forEach {
            println(it.format(formatter))
        }

        return  allMonthDates
    }

    private fun monthYearFromDate(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("MMMM yyyy")
        return date.format(formatter)
    }

    private fun monthYearFromDate11(date: LocalDate): String? {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM")
        return date.format(formatter)
    }

    override fun onMonth(mainPosition:Int,position: Int, selectDate: LocalDate, dayText: String?,check : Boolean,whichTypeDate:String) {
        if (!dayText.equals("")) {
            val message = "Selected Date $dayText " + monthYearFromDate(selectDate)
           // Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

            for (i in 0 until arrayList!!.size){
                for (j in 0 until arrayList!![i].monthList.size) {
                    arrayList!![i].monthList[j].check = false
                }
            }

            arrayList!![mainPosition].monthList[position].check = check
            verticalCalendarAdapter.notifyValue(arrayList!!)

            if(whichTypeDate=="past"){
                val getBookingDetailsRequest = getBookingDetailsRequest(monthYearFromDate11(selectDate)+"-"+dayText)
                Log.e("get booking details by date Request===", getBookingDetailsRequest.toString())
                Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                sellerDataViewModel.getBookingDetailsByDate(getBookingDetailsRequest)
            }
            else {
                showChoiceDialog(mainPosition,position,monthYearFromDate11(selectDate)+"-"+dayText)
            }



        }
    }



    private fun showAlert(context: Context, message: String) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
            .setTitle("Alert")
            .setCancelable(false) // Prevents closing the dialog by tapping outside
            .setPositiveButton("OK") { dialog, _ ->
                // You can add any action when the button is clicked, for now it just dismisses the dialog
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    private fun showChoiceDialog(mainPosition:Int,position: Int,date:String) {
        val options = arrayOf("Check Booking on this Date", "Add")

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select an Option")
            .setItems(options) { dialog: DialogInterface, which: Int ->
                when (which) {
                    0 -> {
                        // call check booking api
                        val getBookingDetailsRequest = getBookingDetailsRequest(date)
                        Log.e("get booking details by date Request===", getBookingDetailsRequest.toString())
                        Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
                        sellerDataViewModel.getBookingDetailsByDate(getBookingDetailsRequest)
                    }
                    1 -> {
                        // add note or some functionality in selected date

                        val calendarClickBottomSheet = CalendarClickBottomSheet
                            .newInstance(arrayList!![mainPosition],mainPosition,position,this@CalendarFragment)
                        calendarClickBottomSheet.show(childFragmentManager, "")
                    }
                }
            }
            .show()
    }









    override fun onDiscount(mainPosition: Int,tag:String) {
     /*    if(tag=="added") {
             val discountBottomSheet = DiscountBottomSheet
                 .newInstance(
                     arrayList!![mainPosition],
                     mainPosition,
                     0,
                     propertyId!!,
                     this@CalendarFragment
                 )
             discountBottomSheet.show(childFragmentManager, "")
         }

        else
         {

             val addDiscountBottomSheet = AddDiscountBottomSheet
                 .newInstance(
                     arrayList!![mainPosition],
                     propertyId!!,
                     this@CalendarFragment
                 )
             addDiscountBottomSheet.show(childFragmentManager, "")

         }*/

    }





    override fun onCalendarItemClicked(value: CalendarModel, mainPosition: Int, position: Int) {
        arrayList!![mainPosition] = value
      //  verticalCalendarAdapter.notifyValue(arrayList!!)
        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        val updatePropertyPriceRequest  = updatePropertyPriceRequest(mainPosition,position)
        Log.e("Update property Price with day Request===", updatePropertyPriceRequest.toString())
        sellerDataViewModel.updatePropertyPrice(updatePropertyPriceRequest)
    }





    // note listener
    override fun onNeteAdd(value: CalendarModel, mainPosition: Int, position: Int) {
        arrayList!![mainPosition] = value
        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        val updatePropertyPriceRequest  = updatePropertyPriceRequest(mainPosition,position)
        Log.e("Add Note with day Request===", updatePropertyPriceRequest.toString())
        sellerDataViewModel.updatePropertyPrice(updatePropertyPriceRequest)
    }


    private fun updatePropertyPriceRequest(mainPosition: Int, position: Int): Map<String, String> {
        return binding.run {
           var avl =""
            if (arrayList!![mainPosition].monthList[position].openBlock== "0")  avl = "0"
            else if(arrayList!![mainPosition].monthList[position].openBlock== "1") avl = "1"
            else  avl = "2"

            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "property_id" to propertyId!!,
                "date" to arrayList!![mainPosition].monthList[position].date,
                "availability" to avl,
                "price" to arrayList!![mainPosition].monthList[position].price,
                "note" to arrayList!![mainPosition].monthList[position].note
            )



        }
    }




    private fun bindObservers() {
        sellerDataViewModel.propertyPriceWithDayLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get propertyPriceWithDay Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {

                           val checkPriceModel : CheckPriceModel = Gson().fromJson(
                                jsonObject.toString(),
                                CheckPriceModel::class.java
                            )
                            checkPriceArrayList!!.clear()
                            checkPriceArrayList!!.addAll(checkPriceModel.result)
                          //  discountModel  = checkPriceModel.discount
                            try {
                            if(checkPriceArrayList!!.size>0) {
                                for (i in 0 until arrayList!!.size) {
                                    for (j in 0 until arrayList!![i].monthList.size) {
                                        for (k in 0 until checkPriceArrayList!!.size) {
                                            if(checkPriceArrayList!![k].date == arrayList!![i].monthList[j].date){
                                                arrayList!![i].monthList[j].price = checkPriceArrayList!![k].price
                                                arrayList!![i].monthList[j].openBlock = checkPriceArrayList!![k].avlStatus
                                                arrayList!![i].monthList[j].note = checkPriceArrayList!![k].note
                                           //  if(discountModel!=null)   arrayList!![i].monthList[j].discountValue =  discountModel!!.discount
                                           //    else arrayList!![i].monthList[j].discountValue =  "0.0"

                                            }
                                        }
                                    }


                                }

                            }



                              /*  if(discountModel!=null) {
                                     //Log.e("discount id=====",discountModel!!.discount)
                                     for (i in 0 until arrayList!!.size) {
                                         arrayList!![i].discount!!.id = discountModel!!.id
                                         arrayList!![i].discount!!.discount = discountModel!!.discount
                                         arrayList!![i].discount!!.title = discountModel!!.title
                                         arrayList!![i].discount!!.subTitle = getString(R.string.expire_on) +" : "+discountModel!!.endDate
                                         arrayList!![i].discount!!.startDate = discountModel!!.startDate
                                         arrayList!![i].discount!!.endDate = discountModel!!.endDate
                                     }


                                 }

                                else {
                                    discountModel=null
                                    for (i in 0 until arrayList!!.size) {
                                        arrayList!![i].discount = discountModel
                                    }
                                    verticalCalendarAdapter.notifyValue(arrayList!!)
                                    sellerDataViewModel.clearPriceData()
                                }*/

                             }catch (e:Exception){
                                 e.printStackTrace()
                             }


                            verticalCalendarAdapter.notifyValue(arrayList!!)
                            sellerDataViewModel.clearPriceData()
                        } else {
                           // Toast.makeText(requireActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show()
                          /*  discountModel=null
                            for (i in 0 until arrayList!!.size) {
                                arrayList!![i].discount = discountModel
                            }*/
                            verticalCalendarAdapter.notifyValue(arrayList!!)
                            sellerDataViewModel.clearPriceData()
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


        sellerDataViewModel.updatePriceWithDayLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("update property price Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
                            sellerDataViewModel.getPropertyPriceWithDay(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)
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


        sellerDataViewModel.dateBookingLiveData.observe(viewLifecycleOwner, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("get booking details by date Response===", jsonObject.toString())
                        if (jsonObject.getString("status") == "1") {
                            val bookingDetailModel : BookingDetailModel = Gson().fromJson(
                                jsonObject.toString(),
                                BookingDetailModel::class.java
                            )
                            startActivity(Intent(requireActivity(), BookingDetailAct::class.java)
                                .putExtra("BookingDetailData",bookingDetailModel.data))

                        } else {
                              showAlert(requireActivity(),getString(R.string.booking_not_available))
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

    override fun onDiscount(value: CalendarModel, mainPosition: Int, position: Int) {

        arrayList!![mainPosition] = value

        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getPropertyPriceWithDay(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)
       // verticalCalendarAdapter.notifyValue(arrayList!!)

    }

    /*override fun onAddDiscount(value: CalendarModel) {
      //  Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
      //  sellerDataViewModel.getPropertyPriceWithDay(propertyId!!)
    }*/


    override fun onAddDiscount() {
        //  Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        //  sellerDataViewModel.getPropertyPriceWithDay(propertyId!!)
    }


    private fun getBookingDetailsRequest(date:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "property_id" to propertyId!!,
                "date" to date
            )

        }
    }

}