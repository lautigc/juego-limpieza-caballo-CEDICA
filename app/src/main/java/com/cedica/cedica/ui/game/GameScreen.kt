package com.cedica.cedica.ui.game

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.annotation.RequiresApi
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import com.cedica.cedica.core.utils.HorsePart
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.isInPreview
import com.cedica.cedica.core.utils.sound.SoundPlayer
import com.cedica.cedica.core.utils.getStageInfo
import com.cedica.cedica.core.utils.sound.TextToSpeechWrapper
import com.cedica.cedica.core.utils.stages
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

data class Tool(val imageRes: Int, val name: String)

val tools = listOf(
    Tool(R.drawable.cepillo_blando, "Cepillo blando"),
    Tool(R.drawable.cepillo_duro, "Cepillo duro"),
    Tool(R.drawable.escarba_vasos, "Escarba vasos"),
    Tool(R.drawable.rasqueta_blanda, "Rasqueta blanda"),
    Tool(R.drawable.rasqueta_dura, "Rasqueta dura")
)

fun isOverZoomedImage(
    toolX: Float,
    toolY: Float,
    imagePosition: IntOffset,
    imageSize: IntSize
): Boolean {

    val imageLeft = imagePosition.x.toFloat()
    val imageTop = imagePosition.y.toFloat()
    val imageRight = imageLeft + imageSize.width.toFloat()
    val imageBottom = imageTop + imageSize.height.toFloat()

    return toolX in imageLeft..imageRight && toolY in imageTop..imageBottom
}


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navigateToMenu: () -> Unit) {

    // Esto es para orientar la pantalla en sentido horizontal
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val gameState = remember { mutableStateOf(GameState()) }
    var stageInfo by remember { mutableStateOf(checkNotNull(getStageInfo(gameState.value.getCurrentStage())) { "No se encontró información para la etapa $gameState.value.getCurrentStage()" }) }
    var parts by remember { mutableStateOf(emptyArray<HorsePart>()) }
    var showZoomedView by remember { mutableStateOf(false) }
    var isAdvanceStageEnabled by remember { mutableStateOf(false) }
    var zoomedImageSize by remember { mutableStateOf(IntSize.Zero) }
    var zoomedImagePosition by remember { mutableStateOf(IntOffset.Zero) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showWelcomeDialog by remember { mutableStateOf(true) }

    LaunchedEffect(stageInfo) {
        parts = stageInfo.incorrectRandomHorseParts + stageInfo.correctHorsePart
    }

    val cantStages = stages.size

    // para el audio (solo disponible fuera de la preview)
    val context = LocalContext.current
    val soundPlayer: SoundPlayer?
    val speech: TextToSpeechWrapper?
    if(!isInPreview()) {
        soundPlayer = remember { SoundPlayer(context) }
        speech = remember { TextToSpeechWrapper(context) }
    } else {
        soundPlayer = remember { null }
        speech = remember { null }
    }

    LaunchedEffect(Unit) {
        // TODO: cargar a demanda y en el init del wrapper
        soundPlayer?.loadSound("success", R.raw.successed2)
        soundPlayer?.loadSound("snort", R.raw.snort_cut)
        soundPlayer?.loadSound("wrong", R.raw.wrong)
        soundPlayer?.loadSound("notification", R.raw.new_notification)
        speech?.initialize()
    }

    if(showWelcomeDialog) {
        WelcomeDialog() { showWelcomeDialog = false }
        gameState.value = gameState.value.copy(
            elapsedTime = 0
        )
    }

    if (showCompletionDialog) {
        CompletionDialog(
            score = gameState.value.getScore(),
            time = gameState.value.getFormattedElapsedTime(),
            onDismiss = { navigateToMenu() }
        )
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = Color(0xFFFFE4B5),
        sheetShadowElevation = 8.dp,
        sheetContent = {
            ImageSelectionList(
                images = tools,
                selectedTool = gameState.value.getSelectedTool(),
                onImageSelected = { tool ->
                    if (showZoomedView && gameState.value.getSelectedTool() == null) {
                        if (tool.name == stageInfo.tool) {
                            // Si la herramienta seleccionada es la correcta
                            gameState.value.setSelectedTool(tool.imageRes)
                            gameState.value.setCustomMessage("¡Excelente! Seleccionaste la herramienta correcta para la limpieza.")
                            gameState.value.setMessageType("success")
                            gameState.value.addScore(20)
                            soundPlayer?.playSound("success")
                        } else {
                            // Si la herramienta seleccionada es incorrecta
                            gameState.value.setCustomMessage("Ups... Seleccionaste la herramienta incorrecta. Intenta de nuevo.")
                            gameState.value.setMessageType("error")
                            soundPlayer?.playSound("wrong")
                        }
                    }
                })
        },
        sheetPeekHeight = 0.dp,
    ) {
        // Row para organizar las columnas
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFE4B5))
                .padding(16.dp)
        ) {
            // Columna 1: Botón "Volver al menú"
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                Button(onClick = { speech?.speak("hola") }) {
                    Text("Hablá")
                }

                Button(onClick = { navigateToMenu() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6))) {
                    Text("Volver al menú", color = Color.Black)
                }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(1000L) // Espera 1 segundo
                        gameState.value = gameState.value.copy(
                            elapsedTime = gameState.value.getElapsedTime() + 1
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFADD8E6)), // Azul más vibrante
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 16.dp)
                        .shadow(10.dp, shape = RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Etapa actual
                        Text(
                            text = "Etapa ${gameState.value.getCurrentStage()}: ${getStageInfo(gameState.value.getCurrentStage())?.correctHorsePart?.name ?: "Desconocida"}",
                            style = TextStyle(
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )

                        // Tiempo actual
                        Text(
                            text = "⏳Tiempo: ${gameState.value.getFormattedElapsedTime()}",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        )

                        // Puntaje
                        Text(
                            text = "\uD83C\uDFC6Puntaje: ${gameState.value.getScore()}",
                            style = TextStyle(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Yellow,
                                textAlign = TextAlign.Center
                            )
                        )
                    }
                }
            }

            // Columna 2: Imagen del caballo (centrada)
            Column(
                modifier = Modifier
                    .weight(3f)  // El caballo ocupa más espacio
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(!showZoomedView){
                    HorsePartSelectionRandom(
                        parts = parts,
                        onPartSelected = { part ->
                            if (part == stageInfo.correctHorsePart.name) {
                                gameState.value.addScore(20)
                                showZoomedView = true
                                coroutineScope.launch {
                                    gameState.value.setCustomMessage("¡Excelente! Seleccionaste la parte correcta del caballo")
                                    gameState.value.setMessageType("success")
                                    soundPlayer?.playSound("success")
                                    delay(2000)
                                    gameState.value.setCustomMessage("¿Qué herramienta debemos utilizar para limpiarla?")
                                    gameState.value.setMessageType("selection")
                                }
                            } else {
                                gameState.value.setCustomMessage("Ups... Seleccionaste la parte incorrecta. Intenta de nuevo.")
                                gameState.value.setMessageType("error")
                                soundPlayer?.playSound("wrong")
                            }
                        })
                } else {
                    ZoomedHorsePart(
                        part = stageInfo.correctHorsePart,
                        onImagePositioned = { size, position ->
                            zoomedImageSize = size
                            zoomedImagePosition = position

                        }
                    )
                }

            }

            // Columna 3: Mensajes y Botón de herramientas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (gameState.value.getMessageType().isNotEmpty()) {
                    MessageBox(
                        messageType = gameState.value.getMessageType(),
                        customMessage = gameState.value.getCustomMessage(),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                var absoluteX by remember { mutableStateOf(0f) }
                var absoluteY by remember { mutableStateOf(0f) }

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(80.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    gameState.value.getSelectedTool()?.let { tool ->
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .background(Color.Transparent)
                                .onGloballyPositioned { coordinates ->
                                    val positionInRoot =
                                        coordinates.positionInRoot() // Obtienes la posición global de la herramienta
                                    Log.d(
                                        "GameDebug",
                                        "Tool Absolute Position: x=${positionInRoot.x}, y=${positionInRoot.y}"
                                    )
                                    absoluteX = positionInRoot.x
                                    absoluteY = positionInRoot.y
                                }
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y

                                        Log.d(
                                            "GameDebug",
                                            "Fun = ${
                                                isOverZoomedImage(
                                                    absoluteX,
                                                    absoluteY,
                                                    zoomedImagePosition,
                                                    zoomedImageSize
                                                )
                                            }"
                                        )
                                        if (isOverZoomedImage(
                                                absoluteX,
                                                absoluteY,
                                                zoomedImagePosition,
                                                zoomedImageSize
                                            )
                                        ) {
                                            gameState.value.reduceDirtLevel(2)
                                            Log.d(
                                                "GameDebug",
                                                "Cant suciedad = ${gameState.value.getAmountDirtyPart()}"
                                            )

                                            if (gameState.value.getAmountDirtyPart() <= 0) {
                                                gameState.value.addScore(20)
                                                isAdvanceStageEnabled =
                                                    gameState.value.advanceStage(cantStages)
                                                if (isAdvanceStageEnabled) {
                                                    offsetX = 0f
                                                    offsetY = 0f
                                                    stageInfo =
                                                        checkNotNull(getStageInfo(gameState.value.getCurrentStage()))
                                                    parts =
                                                        stageInfo.incorrectRandomHorseParts + stageInfo.correctHorsePart
                                                } else {
                                                    showCompletionDialog = true
                                                }
                                                coroutineScope.launch {
                                                    showZoomedView = false
                                                    gameState.value.setCustomMessage("¡La parte está limpia! Avanzando a la siguiente etapa.")
                                                    gameState.value.setMessageType("success")
                                                    delay(3000)
                                                    gameState.value.setMessageType("selection")
                                                    gameState.value.setCustomMessage("¿Qué parte del caballo debemos seleccionar ahora?")
                                                }
                                            }
                                        }
                                    }
                                }
                        ) {
                            Image(
                                painter = painterResource(tool),
                                contentDescription = "Herramienta arrastrable",
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Herramienta seleccionada",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                Button(
                    onClick = { coroutineScope.launch { scaffoldState.bottomSheetState.expand() } },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)),
                ) {
                    Text("Herramientas", color = Color.Black)
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundPlayer?.release()
            speech?.release()
        }
    }
}

@Composable
fun ImageSelectionList(
    images: List<Tool>,
    selectedTool: Int?,
    onImageSelected: (Tool) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        items(images) { tool ->
            SelectableImage(
                imageRes = tool.imageRes,
                isSelected = selectedTool == tool.imageRes,
                onClick = { onImageSelected(tool) }
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
                color = if (isSelected) Color(0xFFADD8E6) else Color.Black,
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Imagen seleccionable",
            modifier = Modifier.size(70.dp),
            alignment = Alignment.Center
        )
    }
}

@Composable
fun MessageBox(messageType: String, customMessage: String? = null, modifier: Modifier = Modifier) {
    val (message, image) = when (messageType) {
        "selection" -> Pair(customMessage ?: "¿Qué parte del caballo debemos seleccionar ahora?",
            R.drawable.vault_boy_thinking
        )
        "error" -> Pair(customMessage ?: "Ups... la herramienta seleccionada no es la correcta.",
            R.drawable.vault_boy_thumbs_down
        )
        "success" -> Pair(customMessage ?: "¡Perfecto! Has seleccionado la herramienta correcta",
            R.drawable.vault_boy_thumbs_up
        )
        "complete" -> Pair(customMessage ?: "¡Felicitaciones! Has completado la limpieza del caballo",
            R.drawable.vault_boy_rich
        )
        else -> Pair("", 0)
    }

    Column(
        modifier = modifier
            .background(Color.Transparent, shape = RoundedCornerShape(16.dp))
            .border(2.dp, Color.Transparent, RoundedCornerShape(16.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = "Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Black, CircleShape)
                .background(Color(0xFFADD8E6))
        )

        Text(
            text = message,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@SuppressLint("ContextCastToActivity")
@Composable
fun LockScreenOrientation(orientation: Int) {
    val activity = LocalContext.current as? Activity
    LaunchedEffect(Unit) {
        activity?.requestedOrientation = orientation
    }
    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}

@Composable
fun CompletionDialog(score: Int, time: String, onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss, // Cierra el diálogo al tocar fuera de él
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "¡Juego Completado!",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                // Mensaje de finalización
                MessageBox(
                    messageType = "complete",
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Puntaje final
                Text(
                    text = "Puntaje Final: $score",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )

                // Tiempo final
                Text(
                    text = "Tiempo Final: $time",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6))
                ) {
                    Text("Volver al Menú", color = Color.Black)
                }
            }
        },
        containerColor = Color(0xFFFFE4B5) // Color de fondo del diálogo
    )
}


@Composable
fun WelcomeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss, // Cierra el diálogo al tocar fuera de él
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "A jugar y a aprender!",
                    style = TextStyle(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = "Después de correr por todos lados y ensuciarse, tenemos como desafío limpiar a Coquito",
                    color = Color.Black
                )
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6))
                ) {
                    Text("Vamos", color = Color.Black)
                }
            }
        },
        containerColor = Color(0xFFFFE4B5) // Color de fondo del diálogo
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewGameScreen() {
    GameScreen { println("hola") }
}

@Preview
@Composable
fun PreviewDialog() {
    WelcomeDialog() { println() }
}