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

    val droppedImages by remember { mutableStateOf(mutableListOf<Int>()) }
    var backgroundColor by remember { mutableStateOf(Color(0xFFE5E4E2)) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val data = event.toAndroidDragEvent().clipData.getItemAt(0).text.toString().toInt()
                droppedImages.add(data)
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                backgroundColor = Color(0xFFD3D3D3)
            }

            override fun onExited(event: DragAndDropEvent) {
                backgroundColor = Color(0xFFE5E4E2)
            }

            override fun onEnded(event: DragAndDropEvent) {
                backgroundColor = Color(0xFFE5E4E2)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE4B5))
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
                    .padding(bottom = 16.dp)
            )
        }

        // Acá defino el área donde se dropean los elementos para probar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(backgroundColor)
                .dragAndDropTarget(
                    shouldStartDragAndDrop = { event ->
                        event.mimeTypes().contains(ClipDescription.MIMETYPE_TEXT_PLAIN)
                    },
                    target = dragAndDropTarget
                )
        ) {
            droppedImages.forEach { imageRes ->
                DraggableImage(imageRes = imageRes)
            }
        }

        // Listado de herramientas a seleccionar
        ImageSelectionList(
            images = images,
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
            .size(150.dp)
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
            modifier = Modifier.size(125.dp)
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
            modifier = Modifier.size(125.dp)
        )
    }
}
