package com.cedica.cedica

import android.content.ClipData
import android.content.ClipDescription
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.mimeTypes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Preview
@Composable
fun PreviewGameScreen() {
    GameScreen { Log.d("GameScreen", "Volver al menú") }
}

@OptIn(ExperimentalFoundationApi::class)
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
    var isDraggingOver by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var selectedPart by remember { mutableStateOf<String?>(null) }

    var toolPosition by remember { mutableStateOf(IntOffset(0, 0)) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val androidDragEvent = event.toAndroidDragEvent()
                toolPosition = IntOffset(androidDragEvent.x.toInt(), androidDragEvent.y.toInt())

                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Herramienta utilizada correctamente")
                }

                isDraggingOver = false
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                isDraggingOver = true
            }

            override fun onExited(event: DragAndDropEvent) {
                isDraggingOver = false
            }
        }
    }

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
                    .weight(3f)
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            val relativeX = offset.x / imageSize.width
                            val relativeY = offset.y / imageSize.height

                            val touchedPart = horseParts.find {
                                isPointInPolygon(relativeX, relativeY, it.polygon)
                            }

                            selectedPart = touchedPart?.name

                            touchedPart?.let {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Seleccionaste: ${it.name}")
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.caballo),
                    contentDescription = "Caballo",
                    modifier = Modifier
                        .size(350.dp)
                        .graphicsLayer(alpha = if (isDraggingOver) 0.5f else 1f)
                        .dragAndDropTarget(
                            shouldStartDragAndDrop = { event ->
                                event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                            },
                            target = dragAndDropTarget
                        )
                        .onGloballyPositioned { coordinates ->
                            imageSize = coordinates.size
                        }
                )

                selectedPart?.let {
                    Text(
                        text = "Seleccionaste: $it",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.TopCenter)
                    )
                }

                selectedTool?.let { tool ->
                    Image(
                        painter = painterResource(tool),
                        contentDescription = "Herramienta dropeada",
                        modifier = Modifier
                            .size(100.dp)
                            .offset { toolPosition }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                ImageSelectionList(images = images, onImageSelected = { selectedTool = it })
            }
        }
    }
}

@Composable
fun ImageSelectionList(
    images: List<Int>,
    onImageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { imageRes ->
            SelectableImage(imageRes = imageRes, onClick = { onImageSelected(imageRes) })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableImage(imageRes: Int, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .border(
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() }
            .dragAndDropSource {
                detectTapGestures(
                    onLongPress = {
                        startTransfer(
                            DragAndDropTransferData(
                                ClipData.newPlainText("imageRes", imageRes.toString())
                            )
                        )
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Imagen seleccionable",
            modifier = Modifier.size(80.dp)
        )
    }
}

@Composable
fun PreviewHorsePolygons() {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }

    // Tamaño original de la imagen (560x445)
    val originalImageWidth = 560f
    val originalImageHeight = 445f

    Box(
        modifier = Modifier
            .size(350.dp)
            .background(Color(0xFFFFE4B5)),
        contentAlignment = Alignment.Center
    ) {
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
        ) {
            if (imageSize.width > 0 && imageSize.height > 0) {
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
}

@Preview
@Composable
fun TestScreen() {
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