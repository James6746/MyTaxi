package com.mytaxi.mytaxitask.ui.home

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.mytaxi.mytaxitask.R
import com.mytaxi.mytaxitask.databinding.FragmentHomeBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback {
    lateinit var binding: FragmentHomeBinding
    private lateinit var mMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()
        return binding.root
    }

    private fun initViews() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.ivLocateMe.setOnClickListener {
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Bobur Park and move the camera
        val boburPark = LatLng(41.2901816305, 69.2562332295)
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.moveCamera(CameraUpdateFactory.newLatLng(boburPark))
        cameraMoveStartedListener(googleMap)
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                boburPark, 12.4f
            )
        )
    }

    private fun cameraMoveStartedListener(googleMap: GoogleMap) {

        googleMap.setOnCameraMoveStartedListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    binding.tvPinnedLocation.text = "Loading."
                    delay(400)
                    binding.tvPinnedLocation.text = "Loading.."
                    delay(400)
                    binding.tvPinnedLocation.text = "Loading..."
                }
            }
        }

        googleMap.setOnCameraIdleListener {
            val gcd = Geocoder(context, Locale.getDefault())
            val addresses: List<Address> = gcd.getFromLocation(
                googleMap.cameraPosition.target.latitude,
                googleMap.cameraPosition.target.longitude,
                1
            )
            if (addresses.isNotEmpty()) {
                binding.tvPinnedLocation.text = addresses[0].getAddressLine(0)
            } else {
                // do your stuff
            }
        }
    }


}