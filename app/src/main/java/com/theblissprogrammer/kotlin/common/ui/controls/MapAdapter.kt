package com.theblissprogrammer.kotlin.common.ui.controls

import android.Manifest
import android.app.Activity
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import com.theblissprogrammer.kotlin.common.ui.extensions.dpToPx
import java.lang.ref.WeakReference
import com.google.android.gms.maps.model.LatLng
import android.location.Location
import android.location.LocationManager
import android.annotation.SuppressLint


/**
 * Created by ahmed.saad on 2019-04-23.
 * Copyright © 2019. All rights reserved.
 */
class MapAdapter(val activity: WeakReference<Activity>,
                 val delegate: WeakReference<ListResourcesMapDelegate>? = null): GoogleMap.OnInfoWindowClickListener {

    private val DEFAULT_ZOOM = 17F
    var locationPermissionGranted = false
    var PERMISSION_ACCESS_COARSE_LOCATION = 91919191
    private val DEFAULT_LOCATION = LatLng(43.7300068, -79.4342381)

    data class MarkerModel (
        val id: String,
        val title: String? = null,
        val description: String? = null,
        val latitude: Double,
        val longitude: Double)

    interface ListResourcesMapDelegate {
        fun onInfoWindowClicked(id: String)
        fun onMapViewChanged(bounds: LatLngBounds)
    }

    private var markers: ArrayList<Marker> = arrayListOf()
    private var calls: ArrayList<(map: GoogleMap) -> Unit> = arrayListOf()
    private var viewModel: ArrayList<MarkerModel> = arrayListOf()

    fun loadMap(map: GoogleMap?) {
        this.map = map

        locationPermissionGranted = getLocationPermission()

        if (locationPermissionGranted)
            updateLocationUI()

        callOnMapReady {
            it.setMaxZoomPreference(DEFAULT_ZOOM)

            it.setOnCameraIdleListener {
                map?.projection?.visibleRegion?.latLngBounds?.apply {
                    delegate?.get()?.onMapViewChanged(this)
                }
            }

            it.setInfoWindowAdapter(object: GoogleMap.InfoWindowAdapter {
                override fun getInfoContents(marker: Marker?): View {
                    val info = LinearLayout(activity.get())
                    info.orientation = LinearLayout.VERTICAL

                    val title =  TextView(activity.get())
                    title.maxWidth = dpToPx(200)
                    title.setTextColor(Color.BLACK)
                    title.gravity = Gravity.CENTER
                    title.setTypeface(null, Typeface.BOLD)
                    title.text = marker?.title

                    val snippet = TextView(activity.get())
                    snippet.maxWidth = dpToPx(200)
                    snippet.setTextColor(Color.GRAY)
                    snippet.text = marker?.snippet

                    info.addView(title)
                    info.addView(snippet)

                    return info
                }

                override fun getInfoWindow(p0: Marker?): View? {
                    return null
                }
            })
        }
    }

    fun reloadData(viewModel: List<MarkerModel>, fitToBounds: Boolean) {
        this.viewModel.clear()
        this.viewModel.addAll(viewModel)

        reloadMarkers()

        // Fit markers to view
        if (fitToBounds)
            fitToBounds()
    }

    fun updateLocationUI() {
        callOnMapReady {
            try {
                if (locationPermissionGranted) {
                    it.isMyLocationEnabled = true
                    it.uiSettings.isMyLocationButtonEnabled = true

                    val location = getLastKnownLocation()

                    zoomWithRadius(
                        if (location != null) {
                            LatLng(
                                location.latitude,
                                location.longitude
                            )
                        } else {
                            DEFAULT_LOCATION
                        },
                        2000.0
                    )
                } else {
                    it.isMyLocationEnabled = false
                    it.uiSettings.isMyLocationButtonEnabled = false
                }
            } catch (e: SecurityException) {

            }
        }
    }

    private fun getLocationPermission(): Boolean {
        activity.get()?.apply {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, Array(1) { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION
                )

                return false
            }
        }

        return true
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(): Location? {
        val locationManager = activity.get()?.getSystemService(LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }

        return bestLocation
    }

    private fun callOnMapReady(call: (map: GoogleMap) -> Unit) {
        synchronized(MapAdapter::class.java.simpleName) {
            this.calls.add(call)
            val map = map ?: return@synchronized

            this.calls.forEach {
                it(map)
            }

            this.calls.clear()
        }
    }

    private var map: GoogleMap? = null
        set(value) {
            field = value

            synchronized(MapAdapter::class.java.simpleName) {
                val map = map ?: return

                this.calls.forEach {
                    it(map)
                }

                this.calls.clear()
            }
        }

    private fun reloadMarkers() {
        callOnMapReady { map ->
            // Clear all previous annotations
            clear()

            viewModel.forEach {
                val location = LatLng(it.latitude, it.longitude)
                val markerOptions = MarkerOptions().position(location)
                    .title(it.title)
                    .snippet(it.description)

                val marker = map.addMarker(markerOptions)
                marker.tag = it.id

                markers.add(marker)
            }

            map.projection.visibleRegion?.latLngBounds?.apply {
                delegate?.get()?.onMapViewChanged(this)
            }
        }
    }

    private fun fitToBounds() {
        if (markers.isNotEmpty()) {
            val builder = LatLngBounds.Builder()

            for (marker in markers) {
                builder.include(marker.position)
            }

            val bounds = builder.build()
            val padding = 100 // offset from edges of the map in pixels

            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            map?.animateCamera(cu)
        }
    }

    private fun clear() {
        map?.clear()
        markers.clear()
    }

    private fun zoomWithRadius(location: LatLng, meters: Double) {

        val scale = meters / 500
        val zoomLevel = (16 - Math.log(scale) / Math.log(2.0)) + .5f

        map?.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                location,
                zoomLevel.toFloat()
            ), 2, null
        )
    }

    override fun onInfoWindowClick(marker: Marker?) {
        val id = marker?.tag as? String ?: return

        delegate?.get()?.onInfoWindowClicked(id)
    }
}