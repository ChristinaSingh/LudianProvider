package com.ludianseller.ui.auth

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ludianseller.R
import com.ludianseller.repository.SellerRepository
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val sellerRepository: SellerRepository) : ViewModel() {

    val sellerResponseLiveData: LiveData<NetworkResult<ResponseBody>>
    get() = sellerRepository.sellerResponseLiveData

    fun registerUser(params : Map<String,String>){
        viewModelScope.launch {
            sellerRepository.registerUser(params)
        }
    }

    fun loginUser(params : Map<String,String>){
        viewModelScope.launch {
            sellerRepository.loginUser(params)
        }
    }

    fun forgetPassword(params : Map<String,String>){
        viewModelScope.launch {
            sellerRepository.forgotPasswordRepo(params)
        }
    }


    fun otpVerify(params : Map<String,String>){
        viewModelScope.launch {
            sellerRepository.otpVerifyRepo(params)
        }
    }

    fun createNewPassword(params : Map<String,String>){
        viewModelScope.launch {
            sellerRepository.createNewPasswordRepo(params)
        }
    }





    fun validateLogin(context: Context,emailAddress: String, password: String) : Pair<Boolean, String> {

        var result = Pair(true, "")
         if(emailAddress==""){
            result = Pair(false, context.getString(R.string.please_enter_email))
        }

        else if(!Helper.isValidEmail(emailAddress)){
            result = Pair(false, context.getString(R.string.email_is_invalid))
        }
         else if(password == ""){
            result = Pair(false, context.getString(R.string.please_enter_password))

        }
        return result
    }






    fun validateSignup(context : Context,fName : String,lName : String,emailAddress: String, mobile: String, password: String,checkTermsCondition : Boolean,
     ) : Pair<Boolean, String> {

        var result = Pair(true, "")

        if(fName==""){
            result = Pair(false, context.getString(R.string.please_enter_first_name))

        }
      else if(lName==""){
            result = Pair(false, context.getString(R.string.please_enter_last_name))
        }

       else if(emailAddress==""){
            result = Pair(false, context.getString(R.string.please_enter_email))
        }

        else if(!Helper.isValidEmail(emailAddress)){
            result = Pair(false, context.getString(R.string.email_is_invalid))
        }

        else if(mobile == ""){
            result = Pair(false, context.getString(R.string.please_enter_mobile_number))

        }

        else if(password == ""){
            result = Pair(false, context.getString(R.string.please_enter_password))

        }


       /* else if(!TextUtils.isEmpty(password)){
            result = Pair(false, "Password length should be greater than 5")
        }*/

        else if(!checkTermsCondition){
            result = Pair(false, context.getString(R.string.please_check_terms_condition))
        }

        return result
    }


    fun validateForgotPassword(context: Context,emailAddress: String,type : String) : Pair<Boolean, String> {

        var result = Pair(true, "")
       if(type=="email") {
           if (emailAddress == "") {
               result = Pair(false, context.getString(R.string.please_enter_email))
           } else if (!Helper.isValidEmail(emailAddress)) {
               result = Pair(false, context.getString(R.string.email_is_invalid))
           }
       }
        else {
           if (emailAddress == "mobile") {
               result = Pair(false, context.getString(R.string.please_enter_mobile_number))
           }
       }

        return result
    }



    fun validatePassword(context: Context,password: String, confirmPassword: String) : Pair<Boolean, String> {

        var result = Pair(true, "")
        if(password==""){
            result = Pair(false, context.getString(R.string.please_enter_password))
        }
        else if(confirmPassword == ""){
            result = Pair(false, context.getString(R.string.please_enter_confirm_password))

        }
        else if(confirmPassword != password){
            result = Pair(false, context.getString(R.string.password_dont_matched))

        }
        return result
    }


}