package com.cedica.cedica

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PreviewHorsePolygons() {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Tamaño original de la imagen (560x445)
    val originalImageWidth = 560f
    val originalImageHeight = 445f

    Box(
        modifier = Modifier
            .size(350.dp)
            .background(Color(0xFFFFE4B5)),
        contentAlignment = Alignment.Center
    ) {
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.TopCenter))
        Image(
            painter = painterResource(R.drawable.caballo),
            contentDescription = "Caballo",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight) // Mantener relación de aspecto
                .onGloballyPositioned { coordinates ->
                    imageSize = coordinates.size
                }
        )

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight) // Mantener relación de aspecto
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val originalX = offset.x / imageSize.width
                        val originalY = offset.y / imageSize.height

                        val selectedPart = horseParts.firstOrNull { part ->
                            isPointInPolygon(originalX, originalY, part.polygon)
                        }

                        selectedPart?.let { part ->
                            coroutineScope.launch {
                                val snackbarJob = launch {
                                    snackbarHostState.showSnackbar("Parte seleccionada: ${part.name}")
                                }
                                delay(500) // Reduce la duración manualmente (1 segundo)
                                snackbarJob.cancel()
                            }
                        }

                    }
                }
        ) {
            horseParts.forEach { part ->
                drawPath(
                    path = Path().apply {
                        part.polygon.forEachIndexed { index, (x, y) ->
                            // Escalar las coordenadas al tamaño renderizado de la imagen
                            val scaledX = x * imageSize.width
                            val scaledY = y * imageSize.height
                            if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                        }
                        close()
                    },
                    color = Color.Red.copy(alpha = 0.5f),
                    style = Stroke(width = 2.dp.toPx())
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewHorsePart() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Vista previa de los polígonos",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        PreviewHorsePolygons()
    }
}
