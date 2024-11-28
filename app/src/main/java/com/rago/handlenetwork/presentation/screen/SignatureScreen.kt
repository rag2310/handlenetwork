package com.rago.handlenetwork.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.rago.handlenetwork.R
import com.rago.handlenetwork.presentation.uistate.SignatureUIState

@Composable
fun SignatureScreen(signatureUIState: SignatureUIState) {

    SignatureScreenContent(signatureUIState)
}

@Composable
@Preview(showBackground = true)
private fun SignatureScreenContent(signatureUIState: SignatureUIState = SignatureUIState()) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .weight(1f, true)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, shape = RoundedCornerShape(8.dp), color = Color.Black)
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(signatureUIState.path)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.placeholder),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        Button(
            onClick = {
                signatureUIState.onNavSignaturePad()
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("Firmar")
        }
    }
}

