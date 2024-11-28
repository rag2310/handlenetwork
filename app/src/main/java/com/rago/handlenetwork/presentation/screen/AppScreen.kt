package com.rago.handlenetwork.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.rago.handlenetwork.data.utils.Constants
import com.rago.handlenetwork.data.utils.network.RetrofitUIState
import com.rago.handlenetwork.presentation.composable.LoadingDialog
import com.rago.handlenetwork.presentation.uistate.AppUIState
import com.rago.handlenetwork.presentation.uistate.SignaturePadUIState
import com.rago.handlenetwork.presentation.uistate.TaskListUIState
import com.rago.handlenetwork.presentation.viewmodel.SignaturePadViewModel
import com.rago.handlenetwork.presentation.viewmodel.TaskListViewModel


@Composable
fun AppScreen(
    appUIState: AppUIState,
    retrofitUIState: RetrofitUIState,
    navController: NavHostController,
    currentRoute: String?
) {
    if (retrofitUIState.showDialogAgain) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text("Desea ejecutar de nuevo?")
            },
            text = {

            },
            confirmButton = {
                TextButton(onClick = {
                    retrofitUIState.onAgainApi()
                }) {
                    Text("De nuevo")
                }

            }
        )
    }
    LoadingDialog(showDialog = retrofitUIState.loading, retrofitUIState)
    AppScreenContent(appUIState, navController, currentRoute, retrofitUIState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppScreenContent(
    appUIState: AppUIState = AppUIState(),
    navController: NavHostController,
    currentRoute: String?,
    retrofitUIState: RetrofitUIState
) {
    Scaffold(Modifier.fillMaxSize(), topBar = {
        CenterAlignedTopAppBar(
            title = {
                Text(currentRoute?.uppercase() ?: "")
            },
            navigationIcon = {
                AnimatedVisibility(currentRoute != Constants.HOME_ROUTE && currentRoute != null) {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            }
        )
    }) { innerPadding ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
            startDestination = Constants.HOME_ROUTE
        ) {
            composable(Constants.HOME_ROUTE) {
                HomeScreen(navController)
            }

            composable(Constants.TASK_LIST_ROUTE) {
                val taskListViewModel: TaskListViewModel = hiltViewModel()
                val taskListUIState: TaskListUIState by taskListViewModel.uiState.collectAsState()
                LaunchedEffect(key1 = Unit) {
                    taskListUIState.apply {
                        setOnHideLoading(appUIState.onHideLoading)
                        setOnShowLoading(appUIState.onShowLoading)
                    }
                }
                TaskListScreen(taskListUIState, retrofitUIState)
            }

            composable(Constants.SIGNATURE_PAD){
                val signaturePadViewModel: SignaturePadViewModel = hiltViewModel()
                val signaturePadUIState: SignaturePadUIState by signaturePadViewModel.uiState.collectAsState()
                SignaturePadScreen(signaturePadUIState)
            }

            composable(Constants.SIGNATURE){
                SignatureScreen()
            }
        }
    }
}
