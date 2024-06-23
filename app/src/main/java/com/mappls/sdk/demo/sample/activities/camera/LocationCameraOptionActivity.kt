package com.mappls.sdk.demo.sample.activities.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mappls.sdk.demo.MapplsMap
import com.mappls.sdk.demo.R
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.Style
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.location.LocationComponent
import com.mappls.sdk.maps.location.LocationComponentActivationOptions
import com.mappls.sdk.maps.location.LocationComponentOptions
import com.mappls.sdk.maps.location.OnCameraTrackingChangedListener
import com.mappls.sdk.maps.location.engine.LocationEngine
import com.mappls.sdk.maps.location.engine.LocationEngineCallback
import com.mappls.sdk.maps.location.engine.LocationEngineRequest
import com.mappls.sdk.maps.location.engine.LocationEngineResult
//import com.mappls.sdk.demo.utils.LocationUtils
import com.mappls.sdk.maps.location.modes.CameraMode
import com.mappls.sdk.maps.location.modes.RenderMode
//import com.mappls.sdk.services.api.directions.MapplsDirection
import com.mappls.sdk.services.api.directions.models.DirectionsResponse
import com.mappls.sdk.services.api.directions.models.DirectionsRoute
//import com.mappls.sdk.services.api.directions.models.MapplsDirectionResource


var locationEngine: LocationEngine? = null
var locationComponent: LocationComponent? = null

class LocationCameraOptionActivity : ComponentActivity() {

    //val directionsCriteria = DirectionsCriteria.RESOURCE_ROUTE  // Route without traffic
    private val locationEngineResult = object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(p0: LocationEngineResult?) {
            Log.e("TAG", "onSuccess: ")
            var lat = LocationUtils.globallatitude
            var lng = LocationUtils.globallongitude

//                                    if (p0 != null) {
//                                        val location = p0.lastLocation
//                                        location?.let {
//                                            mapplsMap.animateCamera(
//                                                CameraUpdateFactory.newLatLngZoom(
//                                                    LatLng(location.latitude, location.longitude),
//                                                    16.0
//                                                )
//                                            )
//                                        }
//
//                                    }

        }

        override fun onFailure(p0: Exception) {
//                                    p0.stackTrace
            Log.e("TAG", "Exception: ")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocationCameraOptionScreen(locationEngineResult)
            //openGoogleMaps(this, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.9028802,77.5046476)

        }

    }

    override fun onResume() {
        super.onResume()
        //openGoogleMaps(this, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.9028802,77.5046476)
    }

    override fun onDestroy() {
        super.onDestroy()
        //openGoogleMaps(this, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.9028802,77.5046476)
        LocationUtils.locationEngine?.removeLocationUpdates(locationEngineResult)
        LocationUtils.locationEngine = null
    }

}

@Composable
fun LocationCameraOptionScreen(locationEngineResult: LocationEngineCallback<LocationEngineResult>) {
    val context = LocalContext.current
    val selectedMode = remember { mutableStateOf("Normal") }
    val selectedTracking = remember { mutableStateOf("None") }
    val trackingDialog = remember { mutableStateOf(false) }
    var mapplsMap1 : MapplsMap ?= null
    val mapplsMapState = remember { mutableStateOf<MapplsMap?>(null) }
    val locationState = remember { mutableStateOf<Location?>(null) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Map View") })
    }) {
        val modeDialog = remember { mutableStateOf(false) }


        Column {

            MapplsMap(
                modifier = Modifier.weight(1f),
                onSuccess = { map, mapplsMap ->
                    mapplsMapState.value = mapplsMap
                    mapplsMap.uiSettings?.setLogoMargins(0, 0, 0, 200)
                    mapplsMap.getStyle {
                        LocationUtils.enableLocation(
                            mapplsMap,
                            it,
                            context,
                            locationEngineResult
                        ) { location ->
                            openGoogleMaps(context, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.9028802,77.5046476)
                        }
                    }
                    mapplsMap1 = mapplsMap
                    var lat = LocationUtils.globallatitude
                    var lng = LocationUtils.globallongitude
                    // Enable user location layer (refer to MapplsMap documentation)

                },

                onError = { _, _ ->
                }
            )
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(colorResource(id = R.color.purple_500))
//                    .padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Text(text = "Main Gate", color = Color.White, modifier = Modifier
//                    .clickable(enabled = true) {
//                        mapplsMap1?.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(
//                                LatLng(
//                                22.553147478403194,
//                                77.23388671875), 14.0))
//                    },)
//            }
            if (modeDialog.value) {
                SingleSelectDialog(title = "Camera Move options",
                    optionsList = listOf("Normal", "Compasss", "GPS"),
                    onSubmitButtonClick = {
                        selectedMode.value = it
                        setRenderMode(it)

                    },
                    onDismissRequest = { modeDialog.value = false })
            }
            var lat = mapplsMap1?.cameraPosition?.target?.latitude
            if (trackingDialog.value) {
                SingleSelectDialog(title = "Camera Move options",
                    optionsList = listOf(
                        "None",
                        "None Compass",
                        "Non GPS",
                        "Tracking",
                        "Tracking Compass",
                        "Tracking GPS",
                        "Tracking GPS North"
                    ),

                    onSubmitButtonClick = {
                        setCameraMode(it)
                    },
                    onDismissRequest = { trackingDialog.value = false })
            }
        }


        // LaunchedEffect to handle side effects
        LaunchedEffect(mapplsMapState.value, locationState.value) {
            val mapplsMap = mapplsMapState.value
            val location = locationState.value
            if (mapplsMap != null) {
                openGoogleMaps(context, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.9028802,77.5046476)
            }
        }
    }


}



@Composable
fun SingleSelectDialog(
    title: String,
    optionsList: List<String>,
    onSubmitButtonClick: (String) -> Unit,
    onDismissRequest: () -> Unit
) {


    Dialog(onDismissRequest = { onDismissRequest.invoke() }) {
        Surface(
            shape = RoundedCornerShape(10.dp)
        ) {

            Column(modifier = Modifier.padding(10.dp)) {

                Text(text = title)

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(verticalArrangement = Arrangement.Center) {
                    items(optionsList) { item ->
                        Text(
                            text = item,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable(enabled = true) {
                                    onSubmitButtonClick.invoke(item)
                                    onDismissRequest.invoke()
                                },
                        )
                    }

                }

            }

        }
    }
}


fun openGoogleMaps(context: Context, currentLat: Double, currentLng: Double, destinationLat: Double, destinationLng: Double) {
    val uri = "http://maps.google.com/maps?saddr=${currentLat},${currentLng}&daddr=$destinationLat,$destinationLng"
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
    intent.setPackage("com.google.android.apps.maps")
    context.startActivity(intent)
}

fun setRenderMode(mode: String) {
    if (mode.equals("normal", ignoreCase = true)) {
        LocationUtils.locationComponent?.renderMode = RenderMode.NORMAL
    } else if (mode.equals("compass", ignoreCase = true)) {
        LocationUtils.locationComponent?.renderMode = RenderMode.COMPASS
    } else if (mode.equals("GPS", ignoreCase = true)) {
        LocationUtils.locationComponent?.renderMode = RenderMode.GPS
    } else
        LocationUtils.locationComponent?.renderMode = RenderMode.NORMAL

}

private fun setCameraMode(mode: String) {
    if (mode.equals("None", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.NONE
    } else if (mode.equals("None compass", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.NONE_COMPASS
    } else if (mode.equals("None gps", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.NONE_GPS
    } else if (mode.equals("Tracking", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.TRACKING
    } else if (mode.equals("Tracking Compass", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.TRACKING_COMPASS
    } else if (mode.equals("Tracking GPS", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.TRACKING_GPS
    } else if (mode.equals("Tracking GPS North", ignoreCase = true)) {
        LocationUtils.locationComponent?.cameraMode = CameraMode.TRACKING_GPS_NORTH
    } else LocationUtils.locationComponent?.cameraMode = CameraMode.TRACKING
}

