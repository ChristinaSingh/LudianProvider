package com.ludianseller.ui.post.fragment

import android.annotation.SuppressLint
import android.graphics.Bitmap
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
import androidx.navigation.ui.navigateUp
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPropertyReviewBinding
import com.ludianseller.ui.post.AddPropertyViewModel
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class PropertyReviewFragment :  Fragment() {
    private var _binding: FragmentPropertyReviewBinding? = null
    private val binding get() = _binding!!
    private var propertyCategoryId : String ? = null
    private var propertyCategoryName : String ? = null
    private var rentId : String ? = null
    private var rentStringName : String ? = null
    private var lat : String ? = null
    private var lon : String ? = null
    private var address : String ? = null
    private var bathroomCount : String ? = null
    private var bedCount : String ? = null
    private var bedroomCount : String ? = null
    private var guestQuantity : String ? = null
    private var selectFacilityId : String ? = null
    private var selectFacilityName : String ? = null
    private var selectAmenitiesId : String ? = null
    private var selectAmenitiesName : String ? = null
    private var selectSafetyId : String ? = null
    private var selectSafetyName : String ? = null
    private var pets : String ? = null
    private var squreMeter : String ? = null

    private var picsList: ArrayList<Bitmap>? = null

    private var title : String ? = null
    private var description : String ? = null
    private var requestAcceptTypeId : String ? = null
    private var requestAcceptTypeName : String ? = null
    private var price : String ? = null
    private var discountId : String ? = null


    private val sharedPrf by lazy { SharedPrf(requireActivity()) }
    private lateinit var addPropertyViewModel: AddPropertyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyReviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addPropertyViewModel = ViewModelProvider(this).get(AddPropertyViewModel::class.java)
        initViews()
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {


        propertyCategoryId = arguments?.getString("propertyCategoryId")
        propertyCategoryName = arguments?.getString("propertyCategoryName")
        rentId = arguments?.getString("rentId")
        rentStringName = arguments?.getString("rentStringName")
        lat = arguments?.getString("lat")
        lon = arguments?.getString("lon")
        address = arguments?.getString("address")
        bathroomCount = arguments?.getString("bathroomCount")
        bedCount = arguments?.getString("bedCount")
        bedroomCount  = arguments?.getString("bedroomCount")
        guestQuantity  = arguments?.getString("guestQuantity")
        selectFacilityId  = arguments?.getString("selectFacilityId")
        selectFacilityName  = arguments?.getString("selectFacilityName")
        selectAmenitiesId  = arguments?.getString("selectAmenitiesId")
        selectAmenitiesName  = arguments?.getString("selectAmenitiesName")
        selectSafetyId  = arguments?.getString("selectSafetyId")
        selectSafetyName  = arguments?.getString("selectSafetyName")
        picsList = arguments?.getParcelableArrayList<Bitmap>("picsList")
        title  = arguments?.getString("title")
        description  = arguments?.getString("description")
        requestAcceptTypeId  = arguments?.getString("requestAcceptTypeId")
        requestAcceptTypeName  = arguments?.getString("requestAcceptTypeName")
        price  = arguments?.getString("price")
        discountId  = arguments?.getString("discountId")
        pets  = arguments?.getString("pets")
        squreMeter  = arguments?.getString("squreMeter")


        binding.tvName.text = title
        binding.tvAddress.text = address
        binding.tvPrice.text = "$price SAR"
        binding.ivImg.setImageBitmap(picsList!![0])

        if(price!!.contains(" SAR")){
           val pricess = price!!.split(" SAR")
            price = pricess[0]
            Log.e("Price====",pricess[0])

        }
        Log.e("Price====",price!!)



        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {
            addPropertyData()
        }

        bindObservers()

    }



    private fun bindObservers() {
        addPropertyViewModel.addPropertyResponseLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("add property Response===", jsonObject.toString())
                        if (jsonObject.getString("status") == "1") {
                            Toast.makeText(
                                requireActivity(),
                                "Data upload on server Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_propertyReviewFragment_to_homeFragment)
                           addPropertyViewModel.clearData()

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
                    Helper.showProgressMessage(
                        requireActivity(),
                        getString(R.string.please_wait))

                }

                else -> {}
            }
        })
    }




    private fun addPropertyData(){
        Helper.showProgressMessage(
            requireActivity(),
            getString(R.string.please_wait))
        val token =
            sharedPrf.getStoredTag(Constants.USER_TOKEN).toRequestBody("text/plain".toMediaTypeOrNull())

        val userId =
            sharedPrf.getStoredTag(SharedPrf.USER_ID).toRequestBody("text/plain".toMediaTypeOrNull())
        val propertyCategoryId1 =
            propertyCategoryId!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val propertyCategoryName1 =
            propertyCategoryName!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val rentId1 =
            rentId!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val rentStringName1 =
            rentStringName!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val lat1 =
            lat!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val lon1 =
            lon!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val address1 =
            address!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val bathroomCount1 =
            bathroomCount!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val bedCount1 =
            bedCount!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val bedroomCount1 =
            bedroomCount!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val guestQuantity1 =
            guestQuantity!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val selectFacilityId1 =
            selectFacilityId!!.toRequestBody("text/plain".toMediaTypeOrNull())

        val selectAmenitiesId1 =
            selectAmenitiesId!!.toRequestBody("text/plain".toMediaTypeOrNull())

        val selectSafetyId1 =
            selectSafetyId!!.toRequestBody("text/plain".toMediaTypeOrNull())

        val title1 =
            title!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val description1 =
            description!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestAcceptTypeId1 =
            requestAcceptTypeId!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val price1 =
            price!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val discountId1 =
            discountId!!.toRequestBody("text/plain".toMediaTypeOrNull())

        val pets =
            pets!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val squreMeter =
            squreMeter!!.toRequestBody("text/plain".toMediaTypeOrNull())


        val images =  picsList // Your bitmap array
        val imageParts = prepareImageParts(images!!)


        addPropertyViewModel.addMyProperty(token,userId,propertyCategoryId1,rentId1,rentStringName1,lat1,
            lon1,address1,bathroomCount1,bedCount1,bedroomCount1,guestQuantity1,selectFacilityId1,selectAmenitiesId1,
            selectSafetyId1,title1,description1,requestAcceptTypeId1,price1,discountId1,pets,squreMeter,imageParts)
    }


    private fun prepareImageParts(images: List<Bitmap>): List<MultipartBody.Part> {
        val imageParts: MutableList<MultipartBody.Part> = mutableListOf()

        for ((index, bitmap) in images.withIndex()) {
            val byteArray = bitmapToByteArray(bitmap)
            val requestFile = RequestBody.create("picsList/*".toMediaTypeOrNull(), byteArray)
            val part = MultipartBody.Part.createFormData("picsList[]", "picsList$index.png", requestFile)
            imageParts.add(part)
        }

        return imageParts
    }


    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }


    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }


}