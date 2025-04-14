package com.ludianseller.ui.post

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPostBinding
import com.ludianseller.ui.auth.AuthViewModel
import com.ludianseller.ui.auth.LoginAct
import com.ludianseller.utils.Constants
import com.ludianseller.utils.Helper
import com.ludianseller.utils.NetworkResult
import com.ludianseller.utils.RealPathUtil
import com.ludianseller.utils.SharedPrf
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private lateinit var addPropertyViewModel: AddPropertyViewModel

    private val binding get() = _binding!!
    private var clickImage: String = ""
    private var bedroom: String = ""
    private var house: String = ""
    private var guest: String = ""
    private var bathroom: String = ""
    private var count1: Int = 0
    private var count2: Int = 0
    private var count3: Int = 0
    private var count4: Int = 0

    private var arrayBitmap: ArrayList<Bitmap>? = null
    private var chkWifi: Boolean = false
    private var chkBreakFast: Boolean = false
    private var wifiValue: String = "0"
    private var breakFastValue: String = "0"
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        addPropertyViewModel = ViewModelProvider(this).get(AddPropertyViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        arrayBitmap = ArrayList()

        binding.iv1.setOnClickListener {
            clickImage = "1"
            checkPermission()
        }

        binding.iv2.setOnClickListener {
            clickImage = "2"
            checkPermission()
        }

        binding.iv3.setOnClickListener {
            clickImage = "3"
            checkPermission()
        }

        binding.iv4.setOnClickListener {
            clickImage = "4"
            checkPermission()
        }

        binding.iv5.setOnClickListener {
            clickImage = "5"
            checkPermission()
        }

        binding.iv6.setOnClickListener {
            clickImage = "6"
            checkPermission()
        }

        binding.iv7.setOnClickListener {
            clickImage = "7"
            checkPermission()
        }

        binding.iv8.setOnClickListener {
            clickImage = "8"
            checkPermission()
        }

        binding.iv9.setOnClickListener {
            clickImage = "9"
            checkPermission()
        }

        binding.iv10.setOnClickListener {
            clickImage = "10"
            checkPermission()
        }

        binding.checkWifi.setOnClickListener {
            if (!chkWifi) {
                binding.checkWifi.isChecked = true
                chkWifi = true
                wifiValue = "1"

            } else {
                binding.checkWifi.isChecked = false
                chkWifi = false
                wifiValue = "0"

            }
        }


        binding.checkBreakFast.setOnClickListener {
            if (!chkBreakFast) {
                binding.checkBreakFast.isChecked = true
                chkBreakFast = true
                breakFastValue = "1"
            } else {
                binding.checkBreakFast.isChecked = false
                chkBreakFast = false
                breakFastValue = "0"

            }
        }

        binding.ivPlus1.setOnClickListener {
            count1++
            Log.e("count value===",count1.toString())
            binding.tvCount1.text = count1.toString()
            binding.tvBedroomCount.text = count1.toString()

        }


        binding.ivMinus1.setOnClickListener {
           if(count1>0) {
               count1--
               binding.tvCount1.text = count1.toString()
               binding.tvBedroomCount.text = count1.toString()
           }
        }


        binding.ivPlus2.setOnClickListener {
            count2++
            binding.tvCount2.text = count2.toString()
            binding.tvGuestCount.text = count2.toString()

        }


        binding.ivMinus2.setOnClickListener {
            if(count2>0) {
                count2--
                binding.tvCount2.text = count2.toString()
                binding.tvGuestCount.text = count2.toString()
            }
        }

        binding.ivPlus3.setOnClickListener {
            count3++
            binding.tvCount3.text = count3.toString()
            binding.tvHouseCount.text = count3.toString()

        }


        binding.ivMinus3.setOnClickListener {
            if(count3>0) {
                count3--
                binding.tvCount3.text = count3.toString()
                binding.tvHouseCount.text = count3.toString()
            }
        }


        binding.ivPlus4.setOnClickListener {
            count4++
            binding.tvCount4.text = count4.toString()
            binding.tvBathroomsCount.text = count4.toString()

        }


        binding.ivMinus4.setOnClickListener {
            if(count4>0) {
                count4--
                binding.tvCount4.text = count4.toString()
                binding.tvBathroomsCount.text = count4.toString()
            }
        }


        binding.btnPostAdd.setOnClickListener {
            validation()
        }


        bindObservers()
    }

    private fun validation() {
        bedroom = binding.tvBedroomCount.text.toString()
        guest = binding.tvGuestCount.text.toString()
        house = binding.tvHouseCount.text.toString()
        bathroom = binding.tvBathroomsCount.text.toString()


        if (arrayBitmap!!.size == 0)
            showValidationErrors(getString(R.string.please_add_atleast_one_image))
        else if (binding.edTitle.text.toString() == "")
            showValidationErrors(getString(R.string.please_add_title))
        else if (binding.edAddress.text.toString() == "")
            showValidationErrors(getString(R.string.please_add_address))
        else if (binding.edDescription.text.toString() == "")
            showValidationErrors(getString(R.string.please_add_description))
        else if (bedroom == "")
            showValidationErrors(getString(R.string.please_enter_bedroom_number))
        else if (guest == "")
            showValidationErrors(getString(R.string.please_enter_guest_number))
        else if (house == "")
            showValidationErrors(getString(R.string.please_enter_house))
        else if (bathroom == "")
            showValidationErrors(getString(R.string.please_enter_bathroom))
        else if (binding.edSize.text.toString() == "")
            showValidationErrors(getString(R.string.please_enter_size))
        else if (binding.edPrice.text.toString() == "")
            showValidationErrors(getString(R.string.please_enter_price))
        else {
            Helper.showProgressMessage(
                requireActivity(),
                getString(R.string.please_wait))

            val token =
                sharedPrf.getStoredTag(Constants.USER_TOKEN).toRequestBody("text/plain".toMediaTypeOrNull())
            val userId =
                sharedPrf.getStoredTag(SharedPrf.USER_ID).toRequestBody("text/plain".toMediaTypeOrNull())
            val title =
                binding.edTitle.text.toString()!!.toRequestBody("text/plain".toMediaTypeOrNull())
            val address = binding.edAddress.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val lat = ""
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val long = ""
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val description = binding.edDescription.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val bedrooms = binding.tvBedroomCount.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val guests = binding.tvGuestCount.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val house = binding.tvHouseCount.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val bathrooms = binding.tvBathroomsCount.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val size = binding.edSize.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val price = binding.edPrice.text.toString()
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val wifi = wifiValue
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val breakfast = breakFastValue
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val pets = breakFastValue
                .toRequestBody("text/plain".toMediaTypeOrNull())
            val squreMeter = breakFastValue
                .toRequestBody("text/plain".toMediaTypeOrNull())


            //!!  val bitmap: Bitmap =  mBinding.signaturePad.signatureBitmap // Your bitmap image
            /*    val imageRequestBody = bitmapToRequestBody(bitmap!!)
                val imagePart = MultipartBody.Part.createFormData(
                    "sign_image",
                    "sign_image.png",
                    imageRequestBody
                )*/



            val images =  arrayBitmap // Your bitmap array
            val imageParts = prepareImageParts(images!!)


            addPropertyViewModel.addProperty(token,userId, title, address, lat, long, description, bedrooms, guests, house, bathrooms, size, price, wifi, breakfast,pets,squreMeter, imageParts)

        }
    }

    private fun dialogForImagePick() {
        val options = arrayOf("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Select Action")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> takePhotoFromCamera()
                1 -> choosePhotoFromGallery()
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun choosePhotoFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickFromGallery.launch(intent)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        takePicture.launch(intent)
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Image captured successfully, handle the result here
                val data: Intent? = result.data
                // Extract the image from the intent and do something with it
                // imageArrayList!!.add(data.toString())
                //arrayList!![position].imageList.add(data?.data.toString())
                // rateElementAdapter.notifyAdapter(arrayList!!)
                try {
                    if (data != null) {
                        val extras = data.extras
                        val bitmapNew = extras!!["data"] as Bitmap
                      //  val imageBitmap: Bitmap =
                       //     BITMAP_RE_SIZER(bitmapNew, bitmapNew.width, bitmapNew.height)!!
                        val tempUri: Uri = Helper.getImageUri(requireActivity(), bitmapNew)!!
                        val image = RealPathUtil.getRealPath(requireContext(), tempUri)
                        // profileImage = File(image)
                        Log.e("camera image path==", image!!)
                        setImageOnView(image, bitmapNew)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }

    private val pickFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Image selected from gallery successfully, handle the result here
                val data: Intent? = result.data
                val bitmapNew = getBitmapFromUri(Uri.parse(data!!.data.toString()))
             //   val imageBitmap: Bitmap =
            //        BITMAP_RE_SIZER(bitmapNew!!, bitmapNew.width, bitmapNew.height)!!
                setImageOnView(data.data.toString(), bitmapNew!!)

            }
        }


    private fun setImageOnView(imageUri: String, imageBitmap: Bitmap) {
        if (clickImage == "1") {
            arrayBitmap!!.add(0, imageBitmap)
            binding.iv1.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "2") {
            arrayBitmap!!.add(1, imageBitmap)
            binding.iv2.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "3") {
            arrayBitmap!!.add(2, imageBitmap)
            binding.iv3.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "4") {
            arrayBitmap!!.add(3, imageBitmap)
            binding.iv4.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "5") {
            arrayBitmap!!.add(4, imageBitmap)
            binding.iv5.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "6") {
            arrayBitmap!!.add(5, imageBitmap)
            binding.iv6.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "7") {
            arrayBitmap!!.add(6, imageBitmap)
            binding.iv7.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "8") {
            arrayBitmap!!.add(7, imageBitmap)
            binding.iv8.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "9") {
            arrayBitmap!!.add(8, imageBitmap)
            binding.iv9.setImageURI(Uri.parse(imageUri))
        } else if (clickImage == "10") {
            arrayBitmap!!.add(9, imageBitmap)
            binding.iv10.setImageURI(Uri.parse(imageUri))
        }
    }


    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE),
                1000
            )
        } else {
            dialogForImagePick()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1000) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with using the camera or accessing the gallery
                dialogForImagePick()
            } else {
                Toast.makeText(requireActivity(), R.string.permission_denied, Toast.LENGTH_LONG)
                    .show()
                // Permission denied, handle accordingly (e.g., display a message, disable certain features)
            }
        }
    }

    fun BITMAP_RE_SIZER(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap? {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 1.0f
        val middleY = newHeight / 1.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 1,
            middleY - bitmap.height / 1,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        return scaledBitmap
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
                                "Property added Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            Navigation.findNavController(binding.root).popBackStack()


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

    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }


 //   val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
  //  val part = MultipartBody.Part.createFormData("images[]", imageFile.name, requestBody)
 //   imageParts.add(part)

    private fun prepareImageParts(images: List<Bitmap>): List<MultipartBody.Part> {
        val imageParts: MutableList<MultipartBody.Part> = mutableListOf()

        for ((index, bitmap) in images.withIndex()) {
            val byteArray = bitmapToByteArray(bitmap)
            val requestFile = RequestBody.create("images/*".toMediaTypeOrNull(), byteArray)
            val part = MultipartBody.Part.createFormData("images[]", "images$index.png", requestFile)
            imageParts.add(part)
        }

        return imageParts
    }

    private fun getBitmapFromUri(uri: Uri): Bitmap? {
        return try {
            val inputStream = requireActivity().contentResolver.openInputStream(uri)
            inputStream?.use { stream ->
                val options = BitmapFactory.Options()
                options.inPreferredConfig = Bitmap.Config.ARGB_8888 // Force ARGB_8888
                BitmapFactory.decodeStream(stream, null, options)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


}