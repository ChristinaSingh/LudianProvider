package com.ludianseller.ui.post.fragment

import android.Manifest
import android.app.Activity
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
import androidx.navigation.Navigation
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPicPhotoBinding
import com.ludianseller.ui.post.adapter.PhotosAdapter
import com.ludianseller.utils.Helper
import com.ludianseller.utils.RealPathUtil
import java.io.ByteArrayOutputStream

class PicPhotoFragment : Fragment(), PhotosAdapter.OnPhotoListener {
        private var _binding: FragmentPicPhotoBinding? = null
        private val binding get() = _binding!!
        private var arrayBitmap: ArrayList<Bitmap>? = null
        private var checkButtonClick: String = "0"
        private lateinit var photosAdapter: PhotosAdapter
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

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            _binding = FragmentPicPhotoBinding.inflate(inflater, container, false)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            initViews()
        }

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
            pets  = arguments?.getString("pets")
            squreMeter  = arguments?.getString("squreMeter")

            arrayBitmap = ArrayList()


            photosAdapter = PhotosAdapter(requireActivity(),arrayBitmap,this@PicPhotoFragment)
            binding.rvPhotos.adapter = photosAdapter

            binding.ivBack.setOnClickListener {
                Navigation.findNavController(binding.root).navigateUp()
            }


            binding.rlAddNewPhotos.setOnClickListener {
                checkButtonClick = "1"
                checkPermission()
            }


            binding.rlTakeNewPhotos.setOnClickListener {
                checkButtonClick = "2"
                checkPermission()
            }


            binding.btnNext.setOnClickListener {
                  if(arrayBitmap!!.size==0) Toast.makeText(requireActivity(),getString(R.string.please_add_atleat_one_image),Toast.LENGTH_LONG).show()
                      else{

                     /* val byteArrayArrayList = ArrayList<ByteArray>()
                      for (bitmap in arrayBitmap!!) {
                          val byteArray = bitmapToByteArray(bitmap)
                          byteArrayArrayList.add(byteArray)
                      }*/

                          val  bundle = Bundle()
                      bundle.putString("propertyCategoryId",propertyCategoryId)
                      bundle.putString("propertyCategoryName",propertyCategoryName)
                      bundle.putString("rentId",rentId)
                      bundle.putString("rentStringName",rentStringName)
                      bundle.putString("lat",lat)
                      bundle.putString("lon",lon)
                      bundle.putString("address",address)
                      bundle.putString("bathroomCount",bathroomCount)
                      bundle.putString("bedCount",bedCount)
                      bundle.putString("bedroomCount",bedroomCount)
                      bundle.putString("guestQuantity",guestQuantity)
                      bundle.putString("selectFacilityId",selectFacilityId)
                      bundle.putString("selectFacilityName",selectFacilityName)
                      bundle.putString("selectAmenitiesId",selectAmenitiesId)
                      bundle.putString("selectAmenitiesName",selectAmenitiesName)
                      bundle.putString("selectSafetyId",selectSafetyId)
                      bundle.putString("selectSafetyName",selectSafetyName)
                      bundle.putParcelableArrayList("picsList", arrayBitmap!!)
                      bundle.putString("pets", pets)
                      bundle.putString("squreMeter", squreMeter)
                          Navigation.findNavController(binding.root)
                          .navigate(R.id.action_picPhotoFragment_to_propertyTitleFragment,bundle)
                  }
            }
        }


    private fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

    private fun choosePhotoFromGallery() {
      //  val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
      // pickFromGallery.launch(intent)

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // Enable multiple selection
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
                        arrayBitmap!!.add(bitmapNew)
                        photosAdapter.notifyAdapter(arrayBitmap!!)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }


            }
        }

/*    private val pickFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Image selected from gallery successfully, handle the result here
                val data: Intent? = result.data
                val bitmapNew = getBitmapFromUri(Uri.parse(data!!.data.toString()))
                //   val imageBitmap: Bitmap =
                //        BITMAP_RE_SIZER(bitmapNew!!, bitmapNew.width, bitmapNew.height)!!
               // setImageOnView(data.data.toString(), bitmapNew!!)
                arrayBitmap!!.add(bitmapNew!!)
                photosAdapter.notifyAdapter(arrayBitmap!!)
            }
        }*/


    private val pickFromGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                if (data != null) {
                    if (data.clipData != null) {
                        // Multiple images selected
                        val count = data.clipData!!.itemCount
                        for (i in 0 until count) {
                            val uri = data.clipData!!.getItemAt(i).uri
                            val bitmap = getBitmapFromUri(uri)
                            arrayBitmap!!.add(bitmap!!)
                        }
                    } else if (data.data != null) {
                        // Single image selected
                        val uri = data.data!!
                        val bitmap = getBitmapFromUri(uri)
                        arrayBitmap!!.add(bitmap!!)
                    }
                    // Notify adapter after adding all selected images
                    photosAdapter.notifyAdapter(arrayBitmap!!)
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
           if (checkButtonClick=="1") choosePhotoFromGallery()
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
                if (checkButtonClick=="1") choosePhotoFromGallery()
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

    override fun onPhoto(position: Int,type:String) {
       if(type=="Delete") {
           deleteDialog(position)
       }
           else {

       }
    }



    private fun deleteDialog(
        position: Int
    ) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Delete")
            .setMessage("Are you sure you want to delete this image?")
            .setPositiveButton("Yes") { dialog, _ ->
                // Call the callback function when the user confirms deletion
                dialog.dismiss()
                if(arrayBitmap!!.size>0){
                    arrayBitmap!!.removeAt(position)
                    photosAdapter.notifyAdapter(arrayBitmap!!)
                }

            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }


}