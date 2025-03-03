package com.rago.handlenetwork

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rago.handlenetwork.data.utils.network.NetworkUtils
import com.rago.handlenetwork.data.utils.network.RetrofitUIState
import com.rago.handlenetwork.data.utils.network.RetrofitUtils
import com.rago.handlenetwork.presentation.screen.AppScreen
import com.rago.handlenetwork.presentation.uistate.AppUIState
import com.rago.handlenetwork.presentation.viewmodel.AppViewModel
import com.rago.handlenetwork.ui.theme.HandleNetworkTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var retrofitUtils: RetrofitUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val appUIState: AppUIState by appViewModel.uiState.collectAsState()
            val retrofitUIState: RetrofitUIState by retrofitUtils.uiState.collectAsState()
            val navController = rememberNavController()

            val currentRoute =
                navController.currentBackStackEntryAsState().value?.destination?.route
            LaunchedEffect(key1 = currentRoute) {
                Log.i("TAG", "AppScreen: $currentRoute")
            }
            HandleNetworkTheme {
                AppScreen(appUIState, retrofitUIState, navController, currentRoute)
            }
        }

        NetworkUtils.getNetworkRequest(this, onSuccess = {
            Log.d("Network", "Conectado a la red")
        }, onFailure = {
            Log.d("Network", "Desconectado de la red")
        })

        GlobalScope.launch(Dispatchers.IO) {
            getNetworkClass()
        }
    }


    private suspend fun getNetworkClass() {
        delay(1000)
        getNetworkClass()
    }
}