package com.ludianseller.ui.booking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.databinding.ActivityRateToUserBinding
import com.ludianseller.ui.home.HomeAct
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class RateToUserAct : AppCompatActivity(){
    private lateinit var binding: ActivityRateToUserBinding
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private val sharedPrf by lazy { SharedPrf(this) }

    private var userName : String = ""
    private var userId : String = ""
    private var image : String = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rate_to_user)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {
       if(intent!=null) {
           userName = intent.getStringExtra("name")!!
           userId = intent.getStringExtra("user_id")!!
           image = intent.getStringExtra("image")!!

           binding.tvName.text = userName



           Glide.with(this@RateToUserAct)
               .load(image)
               .diskCacheStrategy(DiskCacheStrategy.ALL)
               .placeholder(R.drawable.user_default)
               .error(R.drawable.user_default)
               .centerInside()
               .into(binding.ivUser)

       }
        binding.ivClose.setOnClickListener {
            finish()
        }


        binding.btnSubmit.setOnClickListener {
            val currentDate = LocalDate.now()
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val formattedDate = currentDate.format(formatter)
                val addReviewRequest = addReviewRequest(userId, sharedPrf.getStoredTag(SharedPrf.USER_ID), binding.ratingBar.rating.toString(), binding.edReview.text.toString(),formattedDate)
                Log.e("add Review Request Request===", addReviewRequest.toString())
                Helper.showProgressMessage(this@RateToUserAct,getString(R.string.please_wait))
                sellerDataViewModel.addReview(addReviewRequest)

        }

        bindObserver()

    }

    private fun addReviewRequest(userId: String, sellerId: String, rating: String, comment: String,currentDate:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "user_id" to userId ,
                "seller_id" to sellerId,
                "rating" to rating,
                "review" to comment,
                "current_date" to currentDate
            )

        }
    }


    private fun bindObserver(){
        sellerDataViewModel.addReviewResponseLiveData.observe(this, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("add rate user Response===",jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {


                            sellerDataViewModel.clearAddReviewData()

                            startActivity(Intent(this@RateToUserAct, HomeAct::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            finish()

                        }
                        else {
                            Toast.makeText(
                                this@RateToUserAct,
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
                    Helper.showProgressMessage(this@RateToUserAct,getString(R.string.please_wait))
                }

                else -> {}
            }
        })




    }

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(this@RateToUserAct,text, Toast.LENGTH_SHORT).show()
    }




}