package com.mytaxi.mytaxitask

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.directions.route.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mytaxi.mytaxitask.databinding.ActivityTripDetailsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class TripDetailsActivity : AppCompatActivity(), OnMapReadyCallback, RoutingListener {
    lateinit var binding: ActivityTripDetailsBinding
    private lateinit var mMap: GoogleMap
    private val departureLocation = LatLng(38.4233438, -122.0728817)
    private val arrivalDestination = LatLng(37.4233493, -122.0726845)
    private var polyLines: MutableList<Polyline>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        setupBackButton()

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.layoutTripDetails.root)
        bottomSheetBehavior.peekHeight =
            getScreenHeight() * 3 / 5 + 32

        lifecycleScope.launch(Dispatchers.Main) {
            val mapFragment =
                supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync(this@TripDetailsActivity)
        }


    }

    private fun setupBackButton() {
        val params = FrameLayout.LayoutParams(
            120,
            120
        )
        params.setMargins(32, getStatusBarHeight() + 32, 0, 0)
        binding.fab.layoutParams = params

        binding.fab.setOnClickListener {
            this.finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
        mMap.moveCamera(CameraUpdateFactory.newLatLng(departureLocation))
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.uiSettings.isMyLocationButtonEnabled = false
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isCompassEnabled = false
        googleMap.uiSettings.isMapToolbarEnabled = false

        addMarker(departureLocation, R.drawable.ic_circle_from)
        addMarker(arrivalDestination, R.drawable.ic_circle_to)

        googleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(
                CameraPosition.Builder()
                    .target(getMiddlePoint(arrivalDestination, departureLocation)).zoom(6f)
                    .bearing(5f).tilt(45f).build()
            )
        )

        findRoutes(departureLocation, arrivalDestination)
    }

    private fun getMiddlePoint(arrivalDestination: LatLng, departureLocation: LatLng): LatLng {
        return LatLng(
            (arrivalDestination.latitude + departureLocation.latitude) / 2.0,
            (arrivalDestination.longitude + departureLocation.longitude) / 2.0,
        )
    }

    private fun findRoutes(start: LatLng?, end: LatLng?) {
        val routing = Routing.Builder()
            .travelMode(AbstractRouting.TravelMode.DRIVING)
            .withListener(this)
            .alternativeRoutes(true)
            .waypoints(start, end)
            .key(getString(R.string.map_key))
            .build()
        routing.execute()
    }

    private fun addMarker(location: LatLng, marker: Int) {
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    location.latitude,
                    location.longitude
                )
            ).icon(bitmapFromVector(marker))
        )


    }

    private fun bitmapFromVector(vectorResId: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(this, vectorResId)
        vectorDrawable!!.setBounds(
            0, 0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }

    private fun getStatusBarHeight(): Int {
        val resources: Resources = this.resources
        val id = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (id > 0) {
            resources.getDimensionPixelSize(id)
        } else 0
    }

    override fun onRoutingFailure(p0: RouteException?) {

    }

    override fun onRoutingStart() {

    }

    override fun onRoutingSuccess(route: ArrayList<Route>?, shortestRouteIndex: Int) {
        if (polyLines != null) {
            polyLines?.clear()
        }
        val polyOptions = PolylineOptions()
        polyLines = java.util.ArrayList()
        for (i in route!!.indices) {
            if (i == shortestRouteIndex) {
                polyOptions.color(
                    Color.parseColor("#5B89FF")
                )
                polyOptions.width(10f)
                polyOptions.addAll(route[shortestRouteIndex].points)
                val polyline = mMap.addPolyline(polyOptions)
                (polyLines as java.util.ArrayList<Polyline>).add(polyline)
            }
        }
    }

    override fun onRoutingCancelled() {
    }


    private fun getScreenHeight(): Int {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getRealMetrics(metrics)
        return metrics.heightPixels
    }
}