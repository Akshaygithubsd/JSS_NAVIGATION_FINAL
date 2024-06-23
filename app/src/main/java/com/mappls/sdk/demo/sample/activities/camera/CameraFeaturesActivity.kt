package com.mappls.sdk.demo.sample.activities.camera

import CurrentLocation
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
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
import androidx.core.content.ContextCompat.startActivity
import com.mappls.sdk.demo.MainActivity
import com.mappls.sdk.demo.MapplsMap
import com.mappls.sdk.demo.R
import com.mappls.sdk.maps.MapplsMap
import com.mappls.sdk.maps.Style
import com.mappls.sdk.maps.camera.CameraPosition
import com.mappls.sdk.maps.camera.CameraUpdateFactory
import com.mappls.sdk.maps.geometry.LatLng
import com.mappls.sdk.maps.annotations.MarkerOptions
import com.mappls.sdk.maps.location.engine.LocationEngineCallback
import com.mappls.sdk.maps.location.engine.LocationEngineRequest
import com.mappls.sdk.maps.location.engine.LocationEngineResult
//import kotlinx.coroutines.flow.internal.NoOpContinuation.context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class CameraFeaturesActivity : ComponentActivity() {

    private val locationEngineResult = object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(p0: LocationEngineResult?) {
            Log.e("TAG", "onSuccess: ")
            Log.e("Latitude", p0?.locations?.first()?.latitude.toString() ?: "NO")

        }

        override fun onFailure(p0: Exception) {
            Log.e("TAG", "Exception: ")

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CameraFeaturesScreen(locationEngineResult)
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}

@Composable
fun CameraFeaturesScreen(locationEngineResult: LocationEngineCallback<LocationEngineResult>) {
    lateinit var currentlocation: CurrentLocation;
    var mapplsMap1: MapplsMap? = null
    val context = LocalContext.current
    val mapplsMapState = remember { mutableStateOf<MapplsMap?>(null) }
    val locationState = remember { mutableStateOf<Location?>(null) }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "CURRENT LOCATION") })
    }) {

        Column(

        ) {

            MapplsMap(
               modifier = Modifier.weight(1f),
                onSuccess = { mapView, mapplsMap ->
                    mapplsMapState.value = mapplsMap
                    mapplsMap1 = mapplsMap
                    val currentLocation = mapplsMap.locationComponent

                    val cameraPosition = CameraPosition.Builder().target(
                        LatLng(
                            12.9028802,77.5046476
                        )
                    ).zoom(15.0).tilt(0.0).build()
                    mapplsMap.cameraPosition = cameraPosition
                    mapplsMap.getStyle {
                        LocationUtils.enableLocation(
                            mapplsMap,
                            it,
                            context,
                            locationEngineResult

                            ) {}
                    }
                    var Lati  = LocationUtils.locationComponent?.lastKnownLocation?.latitude ?: 0.0
                    if (LocationUtils.locationComponent != null) {
                        val currentLatLng = locationComponent!!.lastKnownLocation?.latitude?.let { it1 ->
                            locationComponent!!.lastKnownLocation?.longitude?.let { it2 ->
                                LatLng(
                                    it1, it2
                                )
                            }
                        }
                        if (currentLatLng != null) {
                            showNavigation(currentLatLng, LatLng(12.9028802, 77.5046476),context)
                        } // Pass to navigation function
                    } else {
                        // Handle case where location is not available
                        Toast.makeText(context, "NAVIGATING TO A-BLOCK", Toast.LENGTH_SHORT).show()
                    }
                    // Add a marker at the specified location
                    val markerOptions = MarkerOptions()
                        .position(LatLng(12.9028802, 77.5046476))
                        .title("A Block")
                    mapplsMap.addMarker(markerOptions)
                },

                onError = { _, _ ->

                })

        }
        // LaunchedEffect to handle side effects
        LaunchedEffect(mapplsMapState.value, locationState.value) {
            val mapplsMap = mapplsMapState.value
            val location = locationState.value
            if (mapplsMap != null) {
                if(mapplsMap != null){
                    openGoogleMaps(context, LocationUtils.globallatitude, LocationUtils.globallongitude, 12.903107560721391, 77.50474336443632)
                }         }
        }
    }

}
private fun showNavigation(currentLatLng: LatLng, destinationLatLng: LatLng,context:Context) {
    val uri = Uri.parse("http://maps.google.com/maps?daddr=${destinationLatLng.latitude},${destinationLatLng.longitude}" +
            "&saddr=${currentLatLng.latitude},${currentLatLng.longitude}")
    //val PackageManager =.packageManager
    //val context = LocalContext.current
    val intent = Intent(Intent.ACTION_VIEW, uri)
    if (intent.resolveActivity(context.packageManager) != null) {
        Toast.makeText(context, "maps app found on your device.", Toast.LENGTH_SHORT).show()
        context.startActivity(intent)

        // Launch MainActivity after Google Maps is closed
//        val mainActivityIntent = Intent(context, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
//        }
        //context.startActivity(mainActivityIntent)
       // startActivity(context, intent)
    } else {
        Toast.makeText(context, "No maps app found on your device.", Toast.LENGTH_SHORT).show()
    }
}


//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(colorResource(id = R.color.purple_500)).padding(8.dp),
//                horizontalArrangement = Arrangement.SpaceAround
//            ) {
//                Text(text = "Move Camera", color = Color.White, modifier = Modifier
//                    .clickable(enabled = true) {
//                        mapplsMap1?.moveCamera(
//                            CameraUpdateFactory.newLatLngZoom(LatLng(
//                                22.553147478403194,
//                                77.23388671875), 14.0))
//                    },)
//                Text(text = "Ease Camera", color = Color.White,modifier = Modifier
//                    .clickable(enabled = true) {
//                        mapplsMap1?.easeCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
//                            28.704268, 77.103045), 14.0))
//                    },)
//                Text(text = "Animate Camera", color = Color.White,modifier = Modifier
//                    .clickable(enabled = true) {
//                        mapplsMap1?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
//                            28.698791, 77.121243), 14.0))
//                    },)
//            }



