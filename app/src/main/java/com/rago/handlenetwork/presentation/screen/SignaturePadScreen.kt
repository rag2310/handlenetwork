package com.rago.handlenetwork.presentation.screen

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Environment
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rago.handlenetwork.presentation.uistate.SignaturePadUIState
import java.io.File
import java.io.FileOutputStream

@Composable
fun SignaturePadScreen(signaturePadUIState: SignaturePadUIState) {
    SignaturePadScreenContent(signaturePadUIState)

}

@Composable
@Preview(showBackground = true)
private fun SignaturePadScreenContent(signaturePadUIState: SignaturePadUIState = SignaturePadUIState()) {
    Column(
        Modifier
            .fillMaxSize()
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f, true)
                .background(Color.Red, RoundedCornerShape(8.dp))
                .border(1.dp, shape = RoundedCornerShape(8.dp), color = Color.Black)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { signaturePadUIState.onAddPoint(it) },
                            onDrag = { change, _ -> signaturePadUIState.onAddPoint(change.position) },
                            onDragEnd = { signaturePadUIState.onEndDraw() }
                        )
                    }
            ) {
                signaturePadUIState.setCanvasSize(size)
                for (i in 1 until signaturePadUIState.points.size) {
                    drawLine(
                        color = Color.Black,
                        start = signaturePadUIState.points[i - 1],
                        end = signaturePadUIState.points[i],
                        strokeWidth = 4f,
                        cap = StrokeCap.Round
                    )
                }

                for (stroke in signaturePadUIState.draw) {
                    for (i in 1 until stroke.size) {
                        drawLine(
                            color = Color.Black,
                            start = stroke[i - 1],
                            end = stroke[i],
                            strokeWidth = 4f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Firme Aqui", textDecoration = TextDecoration.Underline)
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Filled.Create, contentDescription = null)
        }
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            OutlinedButton(onClick = { signaturePadUIState.onClearPoints() }) {
                Icon(Icons.Filled.Clear, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text("Borrar")
            }

            Button(onClick = {
                saveSignatureToPNG(
                    points = signaturePadUIState.draw,
                    canvasWidth = signaturePadUIState.canvasSize.width.toInt(),
                    canvasHeight = signaturePadUIState.canvasSize.height.toInt()
                ) { file ->
                    Log.i("<--Signature-->", "SignaturePadScreenContent: [${file.absolutePath}]")
                    signaturePadUIState.onClearPoints()
                    signaturePadUIState.onReturnFile(file)
                }
            }) {
                Icon(Icons.Filled.Done, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text("Guardar")
            }
        }
    }
}

fun saveSignatureToPNG(
    points: List<List<Offset>>, // Lista de trazos
    canvasWidth: Int,
    canvasHeight: Int,
    onComplete: (File) -> Unit
) {
    val bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = android.graphics.Paint().apply {
        color = android.graphics.Color.BLACK
        strokeWidth = 4f
        style = android.graphics.Paint.Style.STROKE
        isAntiAlias = true
    }

    canvas.drawColor(android.graphics.Color.WHITE)

    // Dibujar cada trazo
    for (stroke in points) {
        val androidPath = android.graphics.Path()
        if (stroke.isNotEmpty()) {
            androidPath.moveTo(stroke[0].x, stroke[0].y)
            for (i in 1 until stroke.size) {
                androidPath.lineTo(stroke[i].x, stroke[i].y)
            }
        }
        canvas.drawPath(androidPath, paint)
    }

    val fileName = "signature_${System.currentTimeMillis()}.png"
    val file = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
        fileName
    )
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()

    onComplete(file)
}