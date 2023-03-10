package com.rzmmzdh.sima

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.rzmmzdh.sima.feature_sim.core.theme.SimaTheme
import com.rzmmzdh.sima.feature_sim.ui.Destination
import com.rzmmzdh.sima.feature_sim.ui.SimScreen
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFirebase()
        setContent {
            val navController = rememberNavController()
            val readPhoneStatePermission =
                rememberPermissionState(
                    android.Manifest.permission.READ_PHONE_STATE,
                )

            SimaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LaunchedEffect(key1 = readPhoneStatePermission) {
                        if (!readPhoneStatePermission.status.isGranted) {
                            readPhoneStatePermission.launchPermissionRequest()
                        }
                    }


                    NavHost(
                        navController = navController,
                        startDestination = Destination.SimScreen.route
                    ) {
                        composable(route = Destination.SimScreen.route) {
                            SimScreen(readPhoneStatePermission = readPhoneStatePermission)
                        }
                    }
                }
                TransparentSystemUiBars()
            }
        }
    }

    private fun initFirebase() {
        analytics = Firebase.analytics
    }

    @Composable
    private fun TransparentSystemUiBars() {
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()

        DisposableEffect(systemUiController, useDarkIcons) {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons
            )
            onDispose {}
        }
    }
}