package com.rago.handlenetwork.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.rago.handlenetwork.data.utils.Constants

@Composable
fun HomeScreen(navController: NavHostController) {
    HomeScreenContent(navController)
}

@Composable
@Preview(showBackground = true)
private fun HomeScreenContent(navController: NavHostController? = null) {
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
            navController?.navigate(Constants.SIGNATURE_PAD)
        }) {
            Text("Signature")
        }
    }
}
