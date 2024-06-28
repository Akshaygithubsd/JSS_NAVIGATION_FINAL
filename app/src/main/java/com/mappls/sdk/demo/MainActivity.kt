package com.mappls.sdk.demo

import MapplePinCameraFeatureActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mappls.sdk.demo.sample.activities.camera.B_activity
import com.mappls.sdk.demo.sample.activities.camera.C_Activity
import com.mappls.sdk.demo.sample.activities.camera.CameraFeaturesActivity
import com.mappls.sdk.demo.sample.activities.camera.DORMITORY
import com.mappls.sdk.demo.sample.activities.camera.Jeweleery
import com.mappls.sdk.demo.sample.activities.camera.Library_Activity
import com.mappls.sdk.demo.sample.activities.camera.Main_Gate
//import kotlinx.coroutines.DefaultExecutor.delay
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MyApp()
        }

        lifecycleScope.launch {
            delay(3000L) // Splash screen duration
            setContent {
                  MainScreen()
            }
        }
    }
}

@Composable
fun MyApp() {
    SplashScreen()
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.finallogo),
                contentDescription = "Logo",
                modifier = Modifier.size(900.dp).padding(start=280.dp)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "Welcome to JSS",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun MainScreen() {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                "JSSATE-B NAVIGATION",
                onNavigationIconClick = {
//                    scope.launch {
//                        scaffoldState.drawerState.open()
//                    }
                }
            )
        }
//        ,
//        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
//        drawerContent = {
//            DrawerHeader()
//            DrawerBody(
//                items = listOf(
//                    MenuItem.MapEvents,
//                    MenuItem.MapLayers,
//                    MenuItem.Camera,
//                    MenuItem.Marker,
//                    MenuItem.Location,
//                    MenuItem.PolyLines,
//                    MenuItem.RestAPI,
//                    MenuItem.Animations,
//                    MenuItem.CustomWidgets
//                ),
//                onItemClick = {
//                    Log.e("MainScreen", "title: ${it.title}")
//                    navController.navigate(it.route) {
//                        navController.graph.startDestinationRoute?.let { route ->
//                            popUpTo(route) {
//                                saveState = true
//                            }
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//
//                    scope.launch {
//                        scaffoldState.drawerState.close()
//                    }
//
//                },
//                navController = navController,
//            )
//        }
    ) {

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {
            Image(
                painter = painterResource(id = R.drawable.whiteplain), // Replace with your background image resource
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
        Navigation(navController = navController)
    }


}

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = MenuItem.MapEvents.route) {

        composable(MenuItem.MapEvents.route) {
            val list = listOf(
                //ListItem("Current Location","", LocationCameraOptionActivity::class.java),
                ListItem("MAIN-GATE","", Main_Gate ::class.java),
                ListItem("A-BLOCK","", CameraFeaturesActivity::class.java),
                ListItem("B-BLOCK","", B_activity::class.java),
                ListItem("C-BLOCK","", C_Activity::class.java),
                ListItem("LIBRARY","", Library_Activity::class.java),
                ListItem("JEWELLERY-BLOCK","",Jeweleery::class.java),
                ListItem("DORMITORY-BLOCK","", DORMITORY ::class.java),



                )
            UiInit(list)
        }
    }

}
@Composable
fun UiInit(list: List<ListItem>) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(list) { item ->
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = Color(0xFFFFA500), shape = RoundedCornerShape(8.dp)),
                    onClick = {
                        context.startActivity(Intent(context, item.destination))
                    },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFFA500),
                        )
                ) {
                    Text(text = item.name, color = Color.Black, fontSize = 18.sp, modifier = Modifier.padding(vertical = 32.dp))
                }
            }
        }
    }
}


data class  ListItem(val name: String, val description: String, val destination:  Class<*>)
        //                Text(text = "Library", color = Color.White,modifier = Modifier
//                    .clickable(enabled = true) {
//                        mapplsMap1?.animateCamera(
//                            CameraUpdateFactory.newLatLngZoom(
//                                LatLng(
//                            28.698791, 77.121243), 14.0))
//                    },)


//        composable(MenuItem.MapLayers.route) {
//            val list = listOf(
//                ListItem("Show Heatmap data","", HeatmapActivity::class.java),
//                ListItem("Indoor","", IndoorActivity::class.java),
//                ListItem("Map Scale bar","", ScalebarActivity::class.java),
//                ListItem("Map Safety Strip","", MapActivity::class.java),
//                ListItem("Geo analytics plugin","", MapActivity::class.java),
//                ListItem("Driving Range Plugin","", MapActivity::class.java),
//            )
//            UiInit(list)
//        }

//        composable(MenuItem.Camera.route) {
//            val list = listOf(
//                ListItem("Camera Features","", CameraFeaturesActivity::class.java),
//                ListItem("Location Camera Options","", LocationCameraOptionActivity::class.java),
//                ListItem("Camera Features in Mappls Pin","", MapplePinCameraFeatureActivity::class.java),
//            )
//            UiInit(list)        }


