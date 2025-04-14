package com.ludianseller.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.ludianseller.R
import com.ludianseller.databinding.FragmentProfileBinding
import com.ludianseller.ui.auth.SplashAct
import com.ludianseller.ui.home.SellerDataViewModel
import com.ludianseller.ui.notification.NotificationAct
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject
import java.util.Locale

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var  obj : JSONObject?=null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)
        initViews()
    }

    private fun initViews() {

        showLang(sharedPrf.getStoredTag(SharedPrf.LANGUAGE))


        binding.btnLogout.setOnClickListener {
            logoutDialog()
        }



        binding.btnChangePass.setOnClickListener {
            startActivity(Intent(requireActivity(), ChangePasswordAct::class.java))
        }


        binding.btnPersonalInfo.setOnClickListener {
            if(obj!=null)
                startActivity(Intent(requireActivity(), PersonalInfoAct::class.java)
                    .putExtra("userId",obj!!.getString("id"))
                    .putExtra("firstName",obj!!.getString("first_name"))
                    .putExtra("lastName",obj!!.getString("last_name"))
                    .putExtra("email",obj!!.getString("email"))
                    .putExtra("mobile",obj!!.getString("mobile"))
                    .putExtra("image",obj!!.getString("image"))
                )
        }

        binding.ivEdit.setOnClickListener {
           if(obj!=null)
            startActivity(Intent(requireActivity(), EditProfileAct::class.java)
                .putExtra("userId",obj!!.getString("id"))
                .putExtra("firstName",obj!!.getString("first_name"))
                .putExtra("lastName",obj!!.getString("last_name"))
                .putExtra("email",obj!!.getString("email"))
                .putExtra("mobile",obj!!.getString("mobile"))
                .putExtra("image",obj!!.getString("image"))
            )
        }

        binding.btnNotification.setOnClickListener {
            startActivity(Intent(requireActivity(), NotificationAct::class.java))
        }

        binding.btnFAQ.setOnClickListener {
            startActivity(Intent(requireActivity(), FaqAct::class.java))
        }

        binding.btnAbout.setOnClickListener {
            startActivity(Intent(requireActivity(), AboutAct::class.java))

        }

        binding.btnPrivacy.setOnClickListener {
            startActivity(Intent(requireActivity(), PrivacyAct::class.java))
        }


        binding.btnLanguage.setOnClickListener {
            val currentLang = sharedPrf.getStoredTag(SharedPrf.LANGUAGE)
            if (currentLang == "en") {
                setLocale("ar")
            } else if (currentLang == "ar") {
                setLocale("en")
            }
            else {
                setLocale("en")
            }
        }

        bindObservers()

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
                            binding.tvName.text = jsonObject.getJSONObject("data")
                                .getString("first_name") + jsonObject.getJSONObject("data")
                                .getString("last_name")
                            binding.tvEmail.text =  jsonObject.getJSONObject("data")
                                .getString("email")
                            Glide.with(requireActivity())
                                .load(jsonObject.getJSONObject("data").getString("image"))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.user_default)
                                .error(R.drawable.user_default)
                                .centerInside()
                                .into(binding.ivImage)
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





    private fun logoutDialog() {
        AlertDialog.Builder(requireActivity())
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                // Call the callback function when the user confirms deletion
                dialog.dismiss()
                sharedPrf.clearAll()
                startActivity(Intent(requireActivity(),SplashAct::class.java)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                requireActivity().finish()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }




    override fun onResume() {
        super.onResume()
        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))
        sellerDataViewModel.getUserProfile(sharedPrf.getStoredTag(Constants.USER_TOKEN))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLang(language: String) {
        when (language) {
            "en" -> binding.tvLanguage.text = getString(R.string.english)
            "ar" -> binding.tvLanguage.text = getString(R.string.arabic)
            else -> binding.tvLanguage.text = getString(R.string.english)
        }
    }


    private fun setLocale(lang: String) {
        val locale = Locale(lang)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        when (lang) {
            "en" -> binding.tvLanguage.text = getString(R.string.english)
            "ar" -> binding.tvLanguage.text = getString(R.string.arabic)
            else -> binding.tvLanguage.text = getString(R.string.english)
        }

        sharedPrf.setStoredTag(SharedPrf.LANGUAGE, lang)

        // Restart activity to apply changes
        startActivity(Intent(requireActivity(),SplashAct::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or  Intent.FLAG_ACTIVITY_CLEAR_TOP))
        requireActivity().finish()
    }

}