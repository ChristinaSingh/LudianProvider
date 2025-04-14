package com.ludianseller.ui.booking

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import com.ludianseller.R
import com.ludianseller.databinding.ActivityRateToUserBinding
import com.ludianseller.databinding.ActivityUserReviewBinding
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.post.ReviewModel
import com.ludianseller.ui.post.adapter.ReviewAdapter
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@AndroidEntryPoint
class CheckUserRateAct : AppCompatActivity(){
    private lateinit var binding: ActivityUserReviewBinding
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private lateinit var reviewAdapter: ReviewAdapter
    private var arrayList : ArrayList<ReviewModel.Data> = ArrayList()
    private var userName : String = ""
    private var userId : String = ""
    private var image : String = ""
    private val sharedPrf by lazy { SharedPrf(this@CheckUserRateAct) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_review)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {
        if(intent!=null) {
            userName = intent.getStringExtra("name")!!
            userId = intent.getStringExtra("user_id")!!
            image = intent.getStringExtra("image")!!

        }

        reviewAdapter = ReviewAdapter(this@CheckUserRateAct,arrayList)
        binding.rvReview.adapter = reviewAdapter



        binding.ivClose.setOnClickListener {
            finish()
        }




        bindObservers()

        getAllReviews()

    }



    private fun getAllReviews(){
        val getReviewRequest = getReviewRequest()
        Log.e("get All review Request===", getReviewRequest.toString())
        Helper.showProgressMessage(this@CheckUserRateAct,getString(R.string.please_wait))
        sellerDataViewModel.getReviews(getReviewRequest)
    }

    private fun bindObservers() {
        sellerDataViewModel.reviewResponseLiveData.observe(this, Observer { it ->
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    try {
                        it.data?.let {
                            val jsonObject = JSONObject(it.string())
                            Log.e("get review on property Response===", jsonObject.toString())
                            if (jsonObject.getString("status") == "1") {
                                val reviewModel: ReviewModel = Gson().fromJson(
                                    jsonObject.toString(),
                                    ReviewModel::class.java
                                )
                                binding.rvReview.visibility = View.VISIBLE
                                arrayList.clear()
                                arrayList.addAll(reviewModel.data)
                                reviewAdapter.notifyAdapter(arrayList)

                                sellerDataViewModel.clearReviewData()
                            } else {
                                binding.rvReview.visibility = View.GONE
                                arrayList.clear()
                                reviewAdapter.notifyAdapter(arrayList)



                            }
                        }
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }

                is NetworkResult.Error -> {
                    showValidationErrors(it.message.toString())
                }

                is NetworkResult.Loading -> {
                    Helper.showProgressMessage(this@CheckUserRateAct, getString(R.string.please_wait))
                }

                else -> {}
            }
        })

    }

    private fun getReviewRequest(): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "user_id" to userId,
            )

        }
    }


    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(this@CheckUserRateAct,text, Toast.LENGTH_SHORT).show()
    }

}