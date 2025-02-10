package com.cedica.cedica

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.annotation.RequiresApi
import android.media.SoundPool
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
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


@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(navigateToMenu: () -> Unit) {

    // Esto es para orientar la pantalla en sentido horizontal
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)


    var selectedTool by remember { mutableStateOf<Int?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var messageType by remember { mutableStateOf("selection") }
    var customMessage by remember { mutableStateOf<String?>(null) }
    val gameState = remember { mutableStateOf(GameState()) }
    val stageInfo by remember { mutableStateOf(checkNotNull(getStageInfo(gameState.value.getCurrentStage())) { "No se encontró información para la etapa $gameState.value.getCurrentStage()" }) }
    val correctPart = stageInfo.correctHorsePart
    val correctTool = stageInfo.tool
    val parts = stageInfo.incorrectRandomHorseParts + correctPart


    // para el audio
    val context = LocalContext.current
    val soundPool = remember { SoundPool.Builder().setMaxStreams(1).build() }
    val soundIds = mapOf(
        "success" to soundPool.load(context, R.raw.successed2, 1),
        "snort" to soundPool.load(context, R.raw.horse_snort, 1),
        "trumpets" to soundPool.load(context, R.raw.success_trumpets, 1),
        "wrong" to soundPool.load(context, R.raw.wrong, 1),
    )

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = Color(0xFFFFE4B5),
        sheetShadowElevation = 8.dp,
        sheetContent = {
            ImageSelectionList(
                images = tools,
                selectedTool = selectedTool,
                onImageSelected = { tool ->
                    if (tool.name == correctTool) {
                        // Si la herramienta seleccionada es la correcta
                        selectedTool = tool.imageRes
                        customMessage = "¡Excelente! Seleccionaste la herramienta correcta para la limpieza."
                        messageType = "success"
                        gameState.value.addScore(20)
                        val play = soundIds["success"]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
                    } else {
                        // Si la herramienta seleccionada es incorrecta
                        customMessage = "Ups... Seleccionaste la herramienta incorrecta. Intenta de nuevo."
                        messageType = "error"
                        val play = soundIds["wrong"]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
                    }
                }
            )
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
                HorsePartSelectionRandom(
                    parts = parts,
                    onPartSelected = { part ->
                        if (part == correctPart.name) {
                            gameState.value.addScore(20)
                            coroutineScope.launch {
                                customMessage = "¡Excelente! Seleccionaste la parte correcta del caballo"
                                messageType = "success"
                                val play = soundIds["success"]?.let { soundPool.play(it, 1f, 1f, 1, 0, 1f) }
                                delay(5000)
                                customMessage = "¿Qué herramienta debemos utilizar para limpiarla?"
                                messageType = "selection"
                            }
                        } else {
                            customMessage = "Ups... Seleccionaste la parte incorrecta. Intenta de nuevo."
                            messageType = "error"
                            val play =
                                soundIds["wrong"]?.let { it1 -> soundPool.play(it1, 1f, 1f, 1, 0, 1f) }
                        }
                    })
            }

            // Columna 3: Mensajes y Botón de herramientas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                if (messageType.isNotEmpty()) {
                    MessageBox(
                        messageType = messageType,
                        customMessage = customMessage,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .height(60.dp)
                        .width(80.dp)
                        .border(2.dp, Color.Black, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    selectedTool?.let { tool ->
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                                .background(Color.Transparent)
                                .pointerInput(Unit) {
                                    detectDragGestures { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount.x
                                        offsetY += dragAmount.y
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
            soundPool.release()
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
        "selection" -> Pair(customMessage ?: "¿Qué parte del caballo debemos seleccionar ahora?", R.drawable.vault_boy_thinking)
        "error" -> Pair(customMessage ?: "Ups... la herramienta seleccionada no es la correcta.", R.drawable.vault_boy_thumbs_down)
        "success" -> Pair(customMessage ?: "¡Perfecto! Has seleccionado la herramienta correcta", R.drawable.vault_boy_thumbs_up)
        "complete" -> Pair(customMessage ?: "¡Perfecto! Has completado la limpieza del caballo", R.drawable.vault_boy_rich)
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

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewGameScreen() {
    GameScreen { println("hola") }
}