package com.cedica.cedica.ui.game

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.HorsePart
import com.cedica.cedica.core.utils.horseParts
import com.cedica.cedica.core.utils.isPointInPolygon
import com.cedica.cedica.core.utils.selectRandomParts
import com.cedica.cedica.core.utils.smoothedHorseParts
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

const val originalImageWidth = 560f
const val originalImageHeight = 445f

@Composable
fun HorseImage() {
    Image(
        painter = painterResource(R.drawable.caballo),
        contentDescription = "Caballo",
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(originalImageWidth / originalImageHeight)
    )
}

@Composable
fun HorseNormalState() {
    Box(
        modifier = Modifier
            .size(350.dp)
            .background(Color(0xFFFFE4B5)),
        contentAlignment = Alignment.Center
    ) {
        HorseImage()
    }
}

@Composable
fun HorsePartSelection() {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val animatedColor = remember { Animatable(Color.Red) }

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
                .aspectRatio(originalImageWidth / originalImageHeight)
                .onGloballyPositioned { coordinates ->
                    imageSize = coordinates.size
                }
        )

        LaunchedEffect(Unit) {
            while (true) {
                animatedColor.animateTo(Color.Yellow, animationSpec = tween(500))
                animatedColor.animateTo(Color.LightGray, animationSpec = tween(500))
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight)
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
                                delay(500)
                                snackbarJob.cancel()
                            }
                        }

                    }
                })
        {
            smoothedHorseParts.forEach { part ->
                drawPath(
                    path = Path().apply {
                        part.polygon.forEachIndexed { index, (x, y) ->
                            val scaledX = x * imageSize.width
                            val scaledY = y * imageSize.height
                            if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                        }
                        close()
                    },
                    color = animatedColor.value.copy(alpha = 0.3f),
                    style = Fill
                )
                drawPath(
                    path = Path().apply {
                        part.polygon.forEachIndexed { index, (x, y) ->
                            val scaledX = x * imageSize.width
                            val scaledY = y * imageSize.height
                            if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                        }
                        close()
                    },
                    color = animatedColor.value,
                    style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )

            }
        }
    }
}

@Composable
fun HorsePartSelectionRandom(parts: Array<HorsePart>, onPartSelected: (String) -> Unit) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    val animatedColor = remember { Animatable(Color.Red) }
    var displayedParts by remember { mutableStateOf(parts.toList()) }

    LaunchedEffect(parts) {
        displayedParts = parts.toList()  // Asegura que se actualiza con cada etapa
    }
    /*
    val parts by remember {
        mutableStateOf(selectRandomParts(randomPartsNumber, null))
    }
    */

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
                .aspectRatio(originalImageWidth / originalImageHeight)
                .onGloballyPositioned { coordinates ->
                    imageSize = coordinates.size
                    Log.d("GameDebug", "Ancho:${imageSize.width}, alto:${imageSize.height}")

                }
        )

        LaunchedEffect(Unit) {
            while (true) {
                animatedColor.animateTo(Color.Yellow, animationSpec = tween(500))
                delay(500)
                animatedColor.animateTo(Color.LightGray, animationSpec = tween(500))
                delay(500)
            }
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight)
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val originalX = offset.x / imageSize.width
                        val originalY = offset.y / imageSize.height

                        val selectedPart = displayedParts.firstOrNull { part ->
                            isPointInPolygon(originalX, originalY, part.polygon)
                        }

                        selectedPart?.let { part ->
                            onPartSelected(part.name)
                        }

                    }
                })
        {
            parts.forEach { part ->
                drawPath(
                    path = Path().apply {
                        part.polygon.forEachIndexed { index, (x, y) ->
                            val scaledX = x * imageSize.width
                            val scaledY = y * imageSize.height
                            if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                        }
                        close()
                    },
                    color = animatedColor.value.copy(alpha = 0.3f),
                    style = Fill
                )
                drawPath(
                    path = Path().apply {
                        part.polygon.forEachIndexed { index, (x, y) ->
                            val scaledX = x * imageSize.width
                            val scaledY = y * imageSize.height
                            if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                        }
                        close()
                    },
                    color = animatedColor.value,
                    style = Stroke(width = 2.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f))
                )

            }
        }
    }
}


@Composable
fun ZoomedHorsePart(part: HorsePart) {

    Box(
        modifier = Modifier
            .size(350.dp)
            .background(Color(0xFFFFE4B5))
        ,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(part.drawableRes),
            contentDescription = "Parte del caballo: ${part.name}",
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight)
                .onGloballyPositioned { coordinates ->
                    val size = coordinates.size
                    Log.d("PreviewDebug", "Image Size: ${size.width} x ${size.height}")
                }
        )
    }
}

@Composable
fun DirtyHorsePart(part: HorsePart = horseParts[0], toolPosition: Offset, onPartCleaned: (Boolean) -> Unit) {
    val context = LocalContext.current

    val horseBitmap = remember {
        BitmapFactory.decodeResource(context.resources, part.drawableRes)
    }
    val horseImage = remember { horseBitmap.asImageBitmap() }

    val dirtBitmap = remember {
        BitmapFactory.decodeResource(context.resources, R.drawable.suciedad)
    }

    val dirtImage = remember { dirtBitmap.asImageBitmap() }
    val dirtAlpha = remember { mutableFloatStateOf(1f) }

    LaunchedEffect(toolPosition) {
        if (isToolOverHorse(toolPosition, horseBitmap)) {
            dirtAlpha.floatValue = (dirtAlpha.floatValue - 0.005f).coerceAtLeast(0f)
        }
    }

    LaunchedEffect(dirtAlpha.floatValue) {
         onPartCleaned(dirtAlpha.floatValue == 0f)
    }

    val dirtPositions = remember {
        List(5) {
            val maxX = (horseBitmap.width - dirtBitmap.width).coerceAtLeast(1)
            val maxY = (horseBitmap.height - dirtBitmap.height).coerceAtLeast(1)

            Offset(
                x = Random.nextInt(0, maxX).toFloat(),
                y = Random.nextInt(0, maxY).toFloat()
            )
        }
    }


    Box(
        modifier = Modifier
            .size(350.dp)
            .background(Color(0xFFFFE4B5)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(originalImageWidth / originalImageHeight)
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val imageWidth = horseImage.width.toFloat()
            val imageHeight = horseImage.height.toFloat()

            val offsetX = (canvasWidth - imageWidth) / 2
            val offsetY = (canvasHeight - imageHeight) / 2

            drawImage(
                image = horseImage,
                topLeft = Offset(offsetX, offsetY)
            )

            val clipPath = Path().apply {
                part.zoomedPolygon.forEachIndexed { index, (x, y) ->
                    val scaledX = x * imageWidth + offsetX
                    val scaledY = y * imageHeight + offsetY
                    if (index == 0) moveTo(scaledX, scaledY) else lineTo(scaledX, scaledY)
                }
                close()
            }

            clipPath(clipPath) {
                dirtPositions.forEach { position ->
                    drawImage(
                        dirtImage,
                        topLeft = Offset(position.x + offsetX, position.y + offsetY),
                        blendMode = BlendMode.SrcAtop
                    )
                }
            }
        }
    }
}

fun isToolOverHorse(toolPosition: Offset, horseBitmap: Bitmap): Boolean {
    Log.d("GameDebug", "X:${toolPosition.x} Y:${toolPosition.y} Ancho imagen:${horseBitmap.width.toFloat()} Altura:${horseBitmap.height.toFloat()}")
    return toolPosition.x in 0f..horseBitmap.width.toFloat() &&
            toolPosition.y in 0f..horseBitmap.height.toFloat()
}

@Preview
@Composable
fun PreviewHorsePart() {
    HorsePartSelection()
}

@Preview
@Composable
fun PreviewNormalHorse() {
    HorseNormalState()
}

@Preview
@Composable
fun PreviewRandomPartsHorse() {
    HorsePartSelectionRandom(selectRandomParts(3, horseParts[0])) { println("a") }
}

@Preview
@Composable
fun PreviewZoomedHorsePart() {
    ZoomedHorsePart(horseParts[3])
}

@Preview
@Composable
fun PreviewDirtyHorsePart() {
    DirtyHorsePart(horseParts[5], Offset.Zero, onPartCleaned = {})
}
