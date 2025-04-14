package com.ludianseller.ui.post.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ludianseller.R
import com.ludianseller.databinding.FragmentPropertyAddressBinding
import java.io.IOException
import java.util.Locale


class PropertyAddressFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentPropertyAddressBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private lateinit var latLong : LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var myAnim: Animation? = null
    private var propertyCategoryId : String ? = null
    private var propertyCategoryName : String ? = null
    private var rentId : String ? = null
    private var rentStringName : String ? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPropertyAddressBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        myAnim = AnimationUtils.loadAnimation(requireActivity(), R.anim.bounce);
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        initViews()


    }





    private fun dropMarkerAtCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            // Permission already granted, get current location
            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null) {
                            latLong = LatLng(location.latitude,location.longitude)
                            //  mMap.isMyLocationEnabled = true
                            mMap.setOnCameraIdleListener {
                                latLong  = LatLng(mMap.cameraPosition.target.latitude,mMap.cameraPosition.target.longitude)
                                /* mMap.animateCamera(
                                     CameraUpdateFactory.newLatLngZoom(
                                         latLong, 17.0f
                                     )
                                 )*/
                                binding.tvAddress.text = getAddressFromLatLng(requireActivity(),latLong)
                                binding.ivMarker.startAnimation(myAnim)
                            }
                            mMap.animateCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLong, 17.0f
                                )
                            )

                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Error getting location", e)
                    }
            }catch (e:Exception){
                e.printStackTrace()
            }

        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0]  == PackageManager.PERMISSION_GRANTED
                    ) {
                    // Permission granted, drop marker at current location
                    dropMarkerAtCurrentLocation()
                } else {
                    // Permission denied, handle accordingly
                    Toast.makeText(requireActivity(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    companion object {
        private const val TAG = "MapsActivity"
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }




    private fun initViews() {

        propertyCategoryId = arguments?.getString("propertyCategoryId")
        propertyCategoryName = arguments?.getString("propertyCategoryName")
        rentId = arguments?.getString("rentId")
        rentStringName = arguments?.getString("rentStringName")

        binding.ivBack.setOnClickListener {
            Navigation.findNavController(binding.root).navigateUp()
        }


        binding.btnNext.setOnClickListener {
            try {
                val  bundle = Bundle()
                bundle.putString("propertyCategoryId",propertyCategoryId)
                bundle.putString("propertyCategoryName",propertyCategoryName)
                bundle.putString("rentId",rentId)
                bundle.putString("rentStringName",rentStringName)
                bundle.putString("lat",latLong.latitude.toString())
                bundle.putString("lon",latLong.longitude.toString())
                bundle.putString("address",binding.tvAddress.text.toString())


                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_propertyAddressFragment_to_propertyInsideFragment,bundle)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
    }



    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in a location and move the camera

        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) &&
            !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(requireContext(), getString(R.string.please_enable_location_service), Toast.LENGTH_SHORT).show()
        } else {
            dropMarkerAtCurrentLocation()
        }


     //  dropMarkerAtCurrentLocation()
         }



    private fun getAddressFromLatLng(context: Context, latLng: LatLng) : String{
        val geocoder = Geocoder(context, Locale.getDefault())
        var fullAddress : String?=null
        try {

            val addresses: List<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                val addressStringBuilder = StringBuilder()
                for (i in 0..address.maxAddressLineIndex) {
                    addressStringBuilder.append(address.getAddressLine(i)).append("\n")
                }
                Log.d(TAG, "Address: ${addressStringBuilder.toString()}")
                fullAddress = addressStringBuilder.toString()
            } else {
                Log.d(TAG, "No address found")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error getting address from location", e)
        }

        return fullAddress!!
    }


}