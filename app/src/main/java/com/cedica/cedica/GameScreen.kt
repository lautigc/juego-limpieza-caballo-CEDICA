package com.cedica.cedica

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun GameScreen(navigateToMenu: () -> Unit) {
    val images = listOf(
        R.drawable.cepillo_blando,
        R.drawable.cepillo_duro,
        R.drawable.escarba_vasos,
        R.drawable.rasqueta_blanda,
        R.drawable.rasqueta_dura
    )

    var selectedTool by remember { mutableStateOf<Int?>(null) }
    var selectedHorsePart by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var offsetX by remember { mutableFloatStateOf(-150f) }
    var offsetY by remember { mutableFloatStateOf(-150f) }
    var isDraggable by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE4B5))
    ) {
        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.align(Alignment.TopCenter))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Text(
                text = "Volver al Menú",
                color = Color.Blue,
                modifier = Modifier.clickable { navigateToMenu() }
            )
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.caballo),
                    contentDescription = "Caballo",
                    modifier = Modifier
                        .size(350.dp)
                        .border(
                            width = if (selectedHorsePart) 4.dp else 0.dp,
                            color = if (selectedHorsePart) Color.Green else Color.Transparent
                        )
                        .clickable {
                            selectedHorsePart = true
                            isDraggable = true
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Parte del caballo seleccionada correctamente")
                            }
                        }
                )

                selectedTool?.let { tool ->
                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .background(Color.Transparent)
                            .size(100.dp)
                            .pointerInput(Unit) {
                                detectDragGestures { change, dragAmount ->
                                    if (isDraggable) {
                                        change.consume()
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y
                                    }
                                }
                            }
                    ) {
                        Image(
                            painter = painterResource(tool),
                            contentDescription = "Herramienta arrastrable",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                ImageSelectionList(
                    images = images,
                    selectedTool = selectedTool,
                    onImageSelected = { tool ->
                        if (selectedTool == null) {
                            selectedTool = tool
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Herramienta seleccionada correctamente")
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ImageSelectionList(
    images: List<Int>,
    selectedTool: Int?,
    onImageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { imageRes ->
            SelectableImage(
                imageRes = imageRes,
                isSelected = selectedTool == imageRes,
                onClick = { onImageSelected(imageRes) }
            )
        }
    }
}

@Composable
fun SelectableImage(imageRes: Int, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .border(
                width = if (isSelected) 4.dp else 2.dp,
                color = if (isSelected) Color.Blue else Color.Black
            )
            .clickable { onClick() },
        contentAlignment = Alignment.BottomStart
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Imagen seleccionable",
            modifier = Modifier.size(80.dp)
        )
    }
}