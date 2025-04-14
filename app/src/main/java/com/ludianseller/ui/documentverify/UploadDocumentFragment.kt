package com.ludianseller.ui.documentverify

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.ludianseller.databinding.FragmentUploadDocumentBinding
import com.ludianseller.ui.home.SellerDataViewModel
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
import java.util.Calendar

@AndroidEntryPoint
class UploadDocumentFragment : Fragment() {
    private var _binding: FragmentUploadDocumentBinding? = null
    private val binding get() = _binding!!
    private lateinit var sellerDataViewModel: SellerDataViewModel
    private var checkButtonClick: String = "0"
    private var countryId: String=""
    private var documentTypeId: String=""

    private var bitmapNew: Bitmap? = null
    private val sharedPrf by lazy { SharedPrf(requireActivity()) }
    private var bitmap1: Bitmap? = null
    private var bitmap2: Bitmap? = null
    private var bitmap3: Bitmap? = null
    private var uploadIdClick: String = "0"
    private var documentType: String = "File" // File Or Id
    private var dob: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUploadDocumentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sellerDataViewModel = ViewModelProvider(this).get(SellerDataViewModel::class.java)

        initViews()
    }

    private fun initViews() {

      //  countryId = arguments?.getString("countryId")
      //  documentTypeId = arguments?.getString("documentTypeId")
      //  binding.tvDocument.text = arguments?.getString("documentName")

      /*  binding.btnGallery.setOnClickListener {
            checkButtonClick = "1"
            checkPermission()
        }


        binding.btnCamera.setOnClickListener {
            checkButtonClick = "2"
            checkPermission()
        }*/



        binding.ivIdentity.setOnClickListener {
             uploadIdClick = "1"
            showImageSourceDialog()
        }

        binding.ivCommercialRegister.setOnClickListener {
            uploadIdClick = "2"
            showImageSourceDialog()
        }

        binding.ivTaxCertificate.setOnClickListener {
            uploadIdClick = "3"
            showImageSourceDialog()
        }

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.tvDob.setOnClickListener{
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
                    dob =  formattedDate
                    binding.tvDob.text = dob
                },
                year, month, day
            )

            // Show the DatePickerDialog
            datePickerDialog.show()
        }



        binding.btnUpload.setOnClickListener {
            if(documentType=="File") {
                if (bitmap1 == null) Toast.makeText(
                    requireActivity(),
                    getString(R.string.please_upload_document),
                    Toast.LENGTH_SHORT
                ).show()
                else if (bitmap2 == null) Toast.makeText(
                    requireActivity(),
                    getString(R.string.please_upload_document),
                    Toast.LENGTH_SHORT
                ).show()
                else if (bitmap3 == null) Toast.makeText(
                    requireActivity(),
                    getString(R.string.please_upload_document),
                    Toast.LENGTH_SHORT
                ).show()
                else addDocumentData()

            }
            else {
                if(binding.edIdNumber.text.toString()==""){
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.enter_id_number),
                        Toast.LENGTH_SHORT
                    ).show()
                }

               else if(binding.edIdNumber.text.toString().length<10){
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.id_number_must_be_ten_digits),
                        Toast.LENGTH_SHORT
                    ).show()
                }



                else  if(dob==""){
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.enter_dob),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else {
                    addDocumentIdData(binding.edIdNumber.text.toString(),dob)
                }

            }
        }


        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioAttachFile -> {
                    // Show "Attach File" UI and hide "ID Data" UI
                    binding.llDocumentFile .visibility = View.VISIBLE
                    binding.llDocumentId.visibility = View.GONE
                    documentType = "File"
                }
                R.id.radioIDData -> {
                    // Show "ID Data" UI and hide "Attach File" UI
                    binding.llDocumentFile.visibility = View.GONE
                    binding.llDocumentId.visibility = View.VISIBLE
                    documentType = "Id"

                }
            }
        }



        bindObservers()

    }

    private fun addDocumentIdData(idNumber:String,dob:String) {
        val addDocumentIdDataRequest = addDocumentIdDataRequest(idNumber,dob)
        Log.e("upload Document ID Request===", addDocumentIdDataRequest.toString())
        Helper.showProgressMessage(requireActivity(),getString(R.string.please_wait))
        sellerDataViewModel.uploadDocumentIdData(addDocumentIdDataRequest)


    }


    private fun addDocumentIdDataRequest(idNumber:String,dob:String): Map<String, String> {
        return binding.run {
            mapOf(
                "token" to sharedPrf.getStoredTag(Constants.USER_TOKEN),
                "userId" to sharedPrf.getStoredTag(SharedPrf.USER_ID),
                "document_id_number" to idNumber,
                "dob" to dob,
            )

        }
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
                        bitmapNew = extras!!["data"] as Bitmap
                        //  val imageBitmap: Bitmap =
                        //     BITMAP_RE_SIZER(bitmapNew, bitmapNew.width, bitmapNew.height)!!
                        val tempUri: Uri = Helper.getImageUri(requireActivity(), bitmapNew!!)!!
                        val image = RealPathUtil.getRealPath(requireContext(), tempUri)
                        // profileImage = File(image)
                        Log.e("camera image path==", image!!)
                        // arrayBitmap!!.add(bitmapNew)
                        //  photosAdapter.notifyAdapter(arrayBitmap!!)
                        if (uploadIdClick=="1"){
                            bitmap1 = bitmapNew
                            binding.ivIdentity.setImageBitmap(bitmap1)

                        }

                        else if (uploadIdClick=="2"){
                            bitmap2 = bitmapNew
                            binding.ivCommercialRegister.setImageBitmap(bitmap2)

                        }

                       else if (uploadIdClick=="3"){
                            bitmap3 = bitmapNew
                            binding.ivTaxCertificate.setImageBitmap(bitmap3)

                        }



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
                bitmapNew = getBitmapFromUri(Uri.parse(data!!.data.toString()))
                //   val imageBitmap: Bitmap =
                //        BITMAP_RE_SIZER(bitmapNew!!, bitmapNew.width, bitmapNew.height)!!
                // setImageOnView(data.data.toString(), bitmapNew!!)

                if (uploadIdClick=="1"){
                    bitmap1 = bitmapNew
                    binding.ivIdentity.setImageBitmap(bitmap1)

                }

                else if (uploadIdClick=="2"){
                    bitmap2 = bitmapNew
                    binding.ivCommercialRegister.setImageBitmap(bitmap2)

                }

                else if (uploadIdClick=="3"){
                    bitmap3 = bitmapNew
                    binding.ivTaxCertificate.setImageBitmap(bitmap3)

                }

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
            if (checkButtonClick == "1") choosePhotoFromGallery()
            else takePhotoFromCamera()
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
                if (checkButtonClick == "1") choosePhotoFromGallery()
                else takePhotoFromCamera()
            } else {
                Toast.makeText(requireActivity(), R.string.permission_denied, Toast.LENGTH_LONG)
                    .show()
                // Permission denied, handle accordingly (e.g., display a message, disable certain features)
            }
        }
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


    private fun bindObservers() {
        sellerDataViewModel.sellerResponseLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("upload document Response===", jsonObject.toString())
                        if (jsonObject.getString("status") == "1") {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.document_uploaded),
                                Toast.LENGTH_SHORT
                            ).show()
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_uploadDocumentFragment_to_homeFragment)
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
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait)
                    )

                }

                else -> {}
            }
        })

        sellerDataViewModel.documentIdLiveData.observe(viewLifecycleOwner, Observer {
            Helper.hideProgressMessage()
            when (it) {
                is NetworkResult.Success -> {
                    it.data?.let {
                        val jsonObject = JSONObject(it.string())
                        Log.e("upload document id Response===", jsonObject.toString())
                        if (jsonObject.getInt("status") == 1) {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.document_uploaded),
                                Toast.LENGTH_SHORT
                            ).show()
                            Navigation.findNavController(binding.root)
                                .navigate(R.id.action_uploadDocumentFragment_to_homeFragment)
                            sellerDataViewModel.clearDocumentIdData()

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
                    Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait)
                    )

                }

                else -> {}
            }
        })


    }


    private fun showImageSourceDialog() {
        val options = arrayOf(getString(R.string.take_new_photos), getString(R.string.choose_from_gallery))

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(getString(R.string.select_image_source))
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> {
                    // Camera option
                    checkButtonClick = "2"
                    checkPermission()
                }
                1 -> {
                    // Gallery option
                    checkButtonClick = "1"
                    checkPermission()
                }
            }
        }
        builder.show()
    }



    private fun showValidationErrors(error: String) {
        val text = String.format(resources.getString(R.string.txt_error_message, error))
        Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
    }

    private fun addDocumentData() {
        Helper.showProgressMessage(requireActivity(), getString(R.string.please_wait))

        val token =
            sharedPrf.getStoredTag(Constants.USER_TOKEN)
                .toRequestBody("text/plain".toMediaTypeOrNull())

        val userId =
            sharedPrf.getStoredTag(SharedPrf.USER_ID)
                .toRequestBody("text/plain".toMediaTypeOrNull())
        val countryId =
            countryId!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val documentTypeId = documentTypeId!!
            .toRequestBody("text/plain".toMediaTypeOrNull())



        val imageRequestBody = bitmapToRequestBody(bitmapNew!!)
        val imagePart = MultipartBody.Part.createFormData(
            "document_image",
            "document_image.png",
            imageRequestBody
        )



        val identityImageRequestBody = bitmapToRequestBody(bitmap1!!)
        val identityImagePart = MultipartBody.Part.createFormData(
            "identity",
            "identity.png",
            identityImageRequestBody
        )

        val commercialImageRequestBody = bitmapToRequestBody(bitmap2!!)
        val commercialImagePart = MultipartBody.Part.createFormData(
            "commercial_register",
            "commercial_register.png",
            commercialImageRequestBody
        )

        val taxImageRequestBody = bitmapToRequestBody(bitmap3!!)
        val taxImagePart = MultipartBody.Part.createFormData(
            "tax_certificate",
            "tax_certificate.png",
            taxImageRequestBody
        )


        sellerDataViewModel.uploadDocument(token,userId, countryId, documentTypeId,imagePart,identityImagePart,
            commercialImagePart,taxImagePart)


    }

    private fun bitmapToRequestBody(bitmap: Bitmap): RequestBody {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        return byteArray.toRequestBody("document_image/png".toMediaTypeOrNull())
    }


}