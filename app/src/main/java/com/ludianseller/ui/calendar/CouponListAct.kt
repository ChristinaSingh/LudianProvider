package com.ludianseller.ui.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.ActivityCouponListBinding
import com.ludianseller.ui.calendar.adapter.CouponAdapter
import com.ludianseller.ui.calendar.bottomsheet.AddDiscountBottomSheet
import com.ludianseller.ui.calendar.bottomsheet.DiscountBottomSheet
import com.ludianseller.ui.calendar.bottomsheet.DiscountListBottomSheet
import com.ludianseller.ui.calendar.model.CalendarModel
import com.ludianseller.ui.calendar.model.CouponModel
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class CouponListAct: AppCompatActivity(), CouponAdapter.OnItemListener,
    AddDiscountBottomSheet.AddDiscountListener, DiscountListBottomSheet.DiscountSelectListener {
    private lateinit var binding: ActivityCouponListBinding
    private val sharedPrf by lazy { SharedPrf(this@CouponListAct) }
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var couponArrayList : ArrayList<CouponModel.Result>? =null
    private lateinit var couponAdapter: CouponAdapter
    private var propertyId: String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon_list)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {

        if(intent!=null){
            propertyId = intent.getStringExtra("propertyId")
        }

        couponArrayList = ArrayList()

        couponAdapter = CouponAdapter(this@CouponListAct,couponArrayList,this@CouponListAct)
        binding.rvCoupon.adapter = couponAdapter


        binding.ivBack.setOnClickListener {
            finish()
        }


        binding.ivAdd.setOnClickListener {
            val addDiscountBottomSheet = AddDiscountBottomSheet
                .newInstance(
                    propertyId!!,
                    this@CouponListAct
                )
            addDiscountBottomSheet.show(supportFragmentManager, "")
        }


        bindObservers()



        Helper.showProgressMessage(this@CouponListAct,getString(R.string.please_wait))
        sellerDataViewModel.getAllCoupons(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)

    }


    private fun bindObservers() {
        sellerDataViewModel.allCouponsLiveData.observe(this, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get All coupon Response===", jsonObject.toString())
                            if (jsonObject.getInt("status") == 1) {

                                val couponModel : CouponModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    CouponModel::class.java
                                )
                                couponArrayList!!.clear()
                                couponArrayList!!.addAll(couponModel.result)
                                couponAdapter.notifyAdapter(couponArrayList!!)
                                binding.tvNotFound.visibility = View.GONE

                                sellerDataViewModel.clearAllCouponsData()
                            } else {
                                couponArrayList!!.clear()
                                couponAdapter.notifyAdapter(couponArrayList!!)
                                binding.tvNotFound.visibility =View.VISIBLE

                              /*  Toast.makeText(
                                    this@CouponListAct,
                                    "" + jsonObject.getString("message"),
                                    Toast.LENGTH_SHORT
                                ).show()*/

                            }
                            //  Log.e("TAG", "observers: $it.")
                        }
                    }catch (e : Exception){
                        e.printStackTrace()
                    }
                }

                is NetworkResult.Error -> {
                  //  Helper.showProgressMessage(, getString(R.string.please_wait))
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(this@CouponListAct, getString(R.string.please_wait))
                }

                else -> {}
            }
        })



    }



    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(this@CouponListAct, text, Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(mainPosition: Int, data: CouponModel.Result) {
        val discountListBottomSheet = DiscountListBottomSheet
            .newInstance(data!!,propertyId!!, this@CouponListAct)

        discountListBottomSheet.show(supportFragmentManager, "")
    }

    override fun onAddDiscount() {
        Helper.showProgressMessage(this@CouponListAct,getString(R.string.please_wait))
        sellerDataViewModel.getAllCoupons(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)
    }

    override fun onDiscountSelect(value: CouponModel.Result, mainPosition: Int, position: Int) {
        Helper.showProgressMessage(this@CouponListAct,getString(R.string.please_wait))
        sellerDataViewModel.getAllCoupons(sharedPrf.getStoredTag(Constants.USER_TOKEN),propertyId!!)
    }


}