package com.ludianseller.ui.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ludianseller.R
import com.ludianseller.models.PropertyPriceModel
import com.ludianseller.ui.calendar.model.CalendarModel

class CompareListingBottomSheet : BottomSheetDialogFragment(), OnMapReadyCallback {
    private lateinit var bottomSheetView: View
    private var propertyPriceModel: PropertyPriceModel? = null
    private lateinit var map: GoogleMap
    private lateinit var propertyPriceArrayList : ArrayList<PropertyPriceModel.Property>


    companion object {
        fun newInstance(value: PropertyPriceModel): CompareListingBottomSheet {
            val fragment = CompareListingBottomSheet()
            fragment.propertyPriceModel = value
            fragment.propertyPriceArrayList =  ArrayList()
            fragment.propertyPriceArrayList.addAll(value.result)

            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bottomSheetView = inflater.inflate(R.layout.bottomsheet_comapre_listing, container, false)

        return bottomSheetView


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val ivBack = bottomSheetView.findViewById<ImageView>(R.id.ivBack)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        ivBack.setOnClickListener {
          //  dialog!!.dismiss()
        }



    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add markers for each item in the price list
        for (item in propertyPriceArrayList) {
            val location = LatLng(item.latitude.toDouble(), item.longitude.toDouble())
            map.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(item.title)
                    .snippet("Price: ${item.price} SAR")
            )
        }

        // Move camera to the first item's location
        if (propertyPriceArrayList.isNotEmpty()) {
            val firstItem = propertyPriceArrayList[0]
            val firstLocation = LatLng(firstItem.latitude.toDouble(), firstItem.longitude.toDouble())
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 10f))
        }
    }


}