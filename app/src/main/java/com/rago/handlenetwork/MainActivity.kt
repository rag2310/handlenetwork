package com.rago.handlenetwork

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rago.handlenetwork.presentation.screen.AppScreen
import com.rago.handlenetwork.presentation.uistate.AppUIState
import com.rago.handlenetwork.presentation.viewmodel.AppViewModel
import com.rago.handlenetwork.ui.theme.HandleNetworkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val appUIState:AppUIState by appViewModel.uiState.collectAsState()
            val navController = rememberNavController()

            val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
            LaunchedEffect(key1 = currentRoute) {
                Log.i("TAG", "AppScreen: $currentRoute")
            }
            HandleNetworkTheme {
                AppScreen(appUIState, navController, currentRoute)
            }
        }
    }
}