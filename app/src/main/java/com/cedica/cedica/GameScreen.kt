package com.cedica.cedica

import android.content.ClipData
import android.content.ClipDescription
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
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.graphicsLayer
import kotlin.math.roundToInt

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

    var droppedImage by remember { mutableStateOf<Int?>(null) }
    var isDraggingOver by remember { mutableStateOf(false) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val data = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString().toInt()
                droppedImage = data
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
            .background(Color(0xFFFFE4B5)),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Volver al Menú",
                color = Color.Blue,
                modifier = Modifier
                    .clickable { navigateToMenu() }
                    .padding(16.dp)
            )
        }

        // Imagen del caballo como área de drop

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.caballo),
                contentDescription = "Caballo",
                modifier = Modifier
                    .size(350.dp)
                    .align(Alignment.Center)
                    .graphicsLayer(alpha = if (isDraggingOver) 0.8f else 1f)
                    .dragAndDropTarget(
                        shouldStartDragAndDrop = { event ->
                            event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                        },
                        target = dragAndDropTarget
                    )
            )
            droppedImage?.let { imageRes ->
                DraggableImage(imageRes = imageRes)
            }
        }

        // Listado de herramientas a seleccionar
        ImageSelectionList(
            images = images,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .height(120.dp)
                .padding(vertical = 16.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageSelectionList(
    images: List<Int>,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { imageRes ->
            SelectableImage(imageRes = imageRes)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SelectableImage(imageRes: Int) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .border(
                border = androidx.compose.foundation.BorderStroke(2.dp, Color.Black),
                shape = RoundedCornerShape(8.dp)
            )
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

// Este composable es para definir lo que sería un elemento "arrastrable"
@Composable
fun DraggableImage(imageRes: Int) {
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
            .size(150.dp)
            .pointerInput(Unit) {
                detectTapGestures { offset += Offset(10f, 10f) }
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Imagen generada",
            modifier = Modifier.size(80.dp)
        )
    }
}
