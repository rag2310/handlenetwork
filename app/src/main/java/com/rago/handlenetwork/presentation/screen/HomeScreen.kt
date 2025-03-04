package com.rago.handlenetwork.presentation.screen

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.rago.handlenetwork.data.utils.Constants
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission

@Composable
fun HomeScreen(navController: NavHostController) {
    HomeScreenContent(navController)
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenContent(navController: NavHostController? = null) {
    val context = LocalContext.current as Activity
    val hasRecordAudioPermission = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    if (!hasRecordAudioPermission.value) {
        RequestPermission { isGranted ->
            hasRecordAudioPermission.value = isGranted
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            navController?.navigate(Constants.TASK_LIST_ROUTE)
        }) {
            Text("Tasks")
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            navController?.navigate(Constants.SIGNATURE)
        }) {
            Text("Signature")
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            navController?.navigate(Constants.MARKDOWN)
        }) {
            Text("Markdown")
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            navController?.navigate(Constants.VOICE_TO_TEXT)
        }) {
            Text("Voice to Text")
        }

    }
}

@Composable
fun RequestPermission(onPermissionResult: (Boolean) -> Unit) {
    val context = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(RequestPermission()) { isGranted ->
        onPermissionResult(isGranted)
    }

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
    }
}


