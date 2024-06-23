package com.mappls.sdk.demo.sample.activities.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mappls.sdk.demo.R
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.Style
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.location.LocationComponent
import com.mappls.sdk.maps.location.LocationComponentActivationOptions
import com.mappls.sdk.maps.location.LocationComponentOptions
import com.mappls.sdk.maps.location.engine.LocationEngine
import com.mappls.sdk.maps.location.engine.LocationEngineCallback
import com.mappls.sdk.maps.location.engine.LocationEngineRequest
import com.mappls.sdk.maps.location.engine.LocationEngineResult
import com.mappls.sdk.maps.location.modes.CameraMode
import com.mappls.sdk.maps.location.modes.RenderMode


object LocationUtils{
   var locationEngine:LocationEngine?=null
   var locationComponent:LocationComponent?=null
   var globallatitude: Double = 0.0

    var globallongitude: Double = 0.0


    fun enableLocation(
        mapplsMap: MapplsMap,
        style: Style,
        context: Context,
        locationEngineCallback: LocationEngineCallback<LocationEngineResult>,
        onLocationLoaded: (Location) -> Unit
    ) {
        val locationComponent = mapplsMap.locationComponent ?: return
        val options: LocationComponentOptions = LocationComponentOptions.builder(context)
            .trackingGesturesManagement(true)
            .accuracyColor(ContextCompat.getColor(context, R.color.purple_700))
            .build()

        val locationComponentActivationOptions =
            LocationComponentActivationOptions.builder(context, style)
                .locationComponentOptions(options)
                .build()
        locationComponent?.activateLocationComponent(locationComponentActivationOptions)
        //locationComponent?.addOnCameraTrackingChangedListener(cameraTrackingCallback)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        locationComponent?.isLocationComponentEnabled = true
        locationEngine = locationComponent?.locationEngine!!
        val request = LocationEngineRequest.Builder(1000)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .build()
        locationEngine?.requestLocationUpdates(
            request,
            locationEngineCallback,
            Looper.getMainLooper()
        )
        locationEngine?.getLastLocation(locationEngineCallback)
        locationComponent?.cameraMode = CameraMode.TRACKING
        locationComponent?.renderMode = RenderMode.COMPASS
        globallongitude = locationComponent.lastKnownLocation?.longitude ?: 0.0
        globallatitude = locationComponent.lastKnownLocation?.latitude ?: 0.0
        // Move camera to user's location with zoom
        mapplsMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    locationComponent.lastKnownLocation?.latitude ?: 0.0,
                    locationComponent.lastKnownLocation?.longitude ?: 0.0
                ),
                14.0
            )
        )

    }


}