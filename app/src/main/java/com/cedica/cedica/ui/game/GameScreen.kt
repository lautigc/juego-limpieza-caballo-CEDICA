package com.cedica.cedica.ui.game

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.core.utils.HorsePart
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.isInPreview
import com.cedica.cedica.core.utils.sound.SoundPlayer
import com.cedica.cedica.core.utils.getStageInfo
import com.cedica.cedica.core.utils.groomingStages
import com.cedica.cedica.core.utils.sound.TextToSpeechWrapper
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.utils.view_models.UserViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
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
fun GameScreen(navigateToMenu: () -> Unit,
               viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
               ) {

    val uiState by viewModel.uiState.collectAsState()
    val currentConfiguration = uiState.user.personalConfiguration
    
    // Esto es para orientar la pantalla en sentido horizontal
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val gameState = remember { mutableStateOf(GameState(totalAttempts = currentConfiguration.numberOfAttempts, totalAvailableTime = currentConfiguration.secondsTime)) }
    var stageInfo by remember { mutableStateOf(checkNotNull(getStageInfo(gameState.value.getCurrentStage())) { "No se encontró información para la etapa $gameState.value.getCurrentStage()" }) }
    var parts by remember { mutableStateOf(emptyList<HorsePart>()) }
    var showZoomedView by remember { mutableStateOf(false) }
    var isAdvanceStageEnabled by remember { mutableStateOf(false) }
    var absoluteX by remember { mutableFloatStateOf(0f) }
    var absoluteY by remember { mutableFloatStateOf(0f) }
    var showCompletionDialog by remember { mutableStateOf(false) }
    var showWelcomeDialog by remember { mutableStateOf(true) }
    var showProgress by remember { mutableStateOf(false) }
    var isGameFinished by remember { mutableStateOf(false) }

    LaunchedEffect(stageInfo) {
        parts = stageInfo.incorrectRandomHorseParts + stageInfo.correctHorsePart
    }

    val highlightCorrectTool by remember(gameState.value.getAttemptsLeft()) {
        derivedStateOf { gameState.value.getAttemptsLeft() <= 0 }
    }

    val correctTool = tools.find {it.name == stageInfo.tool.displayName}
    val cantStages = if (currentConfiguration.numberOfImages in 1..groomingStages.size) currentConfiguration.numberOfImages else groomingStages.size
    Log.d("GameDebug", "Cant etapas: ${currentConfiguration.numberOfImages}, $cantStages")
    // para el audio (solo disponible fuera de la preview)
    val context = LocalContext.current
    val soundPlayer: SoundPlayer?
    val speech: TextToSpeechWrapper?
    val voice = uiState.user.personalConfiguration.voiceType
    if(!isInPreview()) {
        soundPlayer = remember { SoundPlayer(context) }
        speech = remember { TextToSpeechWrapper(context, voice) }
    } else {
        soundPlayer = remember { null }
        speech = remember { null }
    }

    LaunchedEffect(Unit) {
        soundPlayer?.loadSound("success", R.raw.successed2)
        soundPlayer?.loadSound("snort", R.raw.snort_cut)
        soundPlayer?.loadSound("wrong", R.raw.wrong)
        soundPlayer?.loadSound("notification", R.raw.new_notification)
        soundPlayer?.loadSound("cleaning", R.raw.scrubbing_brush)
    }

    val isTtsReady = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        val success = speech?.initialize()
        if(success == true) {
            speech.speak("Después de correr por todos lados y ensuciarse, tenemos como desafío limpiar a Coquito, vamos!. Hacé click en el botón para empezar")
        }
        isTtsReady.value = true
    }

    var isLoading = true
    if(showWelcomeDialog) {
        if(isTtsReady.value) {
            isLoading = false
            GameAlertDialog (title="¡A jugar y aprender!", titleButton = "Comenzar", messageType = "start") {
                showWelcomeDialog = false
                speech?.speak("Selecciona la parte del caballo que hay que limpiar en esta etapa")
            }
        }
        LoadingDialog(isLoading)
        gameState.value = gameState.value.copy(
            elapsedTime = 0
        )
    }

    val psViewModel: PlaySessionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val sessionViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
    if (showCompletionDialog) {
        LaunchedEffect(Unit) {
            speech?.speak("Completaste el juego, felicitaciones!!. Hacé click en el botón para volver al menú.")
            val playSession = gameState.value.getCompletionTime()?.seconds?.let {
                val id = sessionViewModel.uiState.value.user.id
                PlaySession(
                    date = Date(),
                    difficultyLevel = gameState.value.getDifficulty(),
                    correctAnswers = gameState.value.getCantSuccess(),
                    incorrectAnswers = gameState.value.getCantErrors(),
                    timeSpent = it,
                    userID = id
                )
            }
            if (playSession != null) {
                psViewModel.insert(playSession)
            }
        }
        GameAlertDialog(
            title = "Juego finalizado",
            messageType = "complete",
            titleButton = "Volver al menú",
            score = gameState.value.getScore(),
            time = gameState.value.getFormattedElapsedTime(),
            onDismiss = { navigateToMenu() }
        )
    }

    if(gameState.value.isTimeUp()){
        GameAlertDialog(
            title = "Juego finalizado",
            messageType = "not complete",
            titleButton = "Volver al menú",
            onDismiss = { navigateToMenu() }
        )
    }

    if (showProgress) {
        ShowCleaningProgressBar(
            currentStep = gameState.value.getCurrentStage(),
            totalSteps = cantStages,
            onDismiss = { showProgress = false }
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
                correctToolId = correctTool?.imageRes,
                highlightCorrectTool = highlightCorrectTool,
                onImageSelected = { tool ->
                    if (showZoomedView && gameState.value.getSelectedTool() == null) {
                        if (tool.name == stageInfo.tool.displayName) {
                            // Si la herramienta seleccionada es la correcta
                            speech?.speak("¡Excelente! Seleccionaste la herramienta correcta para la limpieza.")
                            gameState.value.setSelectedTool(tool.imageRes)
                            gameState.value.setCustomMessage("¡Excelente! Seleccionaste la herramienta correcta para la limpieza.")
                            gameState.value.setMessageType("success")
                            gameState.value.addScore()
                            gameState.value.increaseSuccess()
                            gameState.value.resetAttempts()
                            soundPlayer?.playSound("success")
                        } else {
                            // Si la herramienta seleccionada es incorrecta
                            gameState.value.setCustomMessage("Ups... Seleccionaste la herramienta incorrecta. Intenta de nuevo.")
                            speech?.speak("Ups... Seleccionaste la herramienta incorrecta. Intentá de nuevo.")
                            gameState.value.setMessageType("error")
                            gameState.value.increaseError()
                            gameState.value.decreaseAttempts()
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { navigateToMenu() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)),
                        shape = CircleShape,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Ir al menú"
                        )
                    }

                    Button(
                        onClick = { showProgress = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6)),
                        shape = CircleShape,
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier.padding(2.dp)
                    ) {
                        Icon(
                            Icons.Rounded.Info,
                            contentDescription = "Mostrar progreso"
                        )
                    }
                }

                LaunchedEffect(Unit) {
                    while (!isGameFinished) {
                        delay(1000L) // Espera 1 segundo
                        gameState.value = gameState.value.copy(
                            elapsedTime = gameState.value.getElapsedTime() + 1
                        )
                    }
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFADD8E6)),
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
                            text = "Etapa ${gameState.value.getCurrentStage()}",
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

                        Text(
                            text = "⏳Tiempo restante: ${gameState.value.getFormattedRemainingTime()}",
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
                                gameState.value.addScore()
                                gameState.value.increaseSuccess()
                                gameState.value.resetAttempts()
                                showZoomedView = true
                                coroutineScope.launch {
                                    gameState.value.setCustomMessage("¡Excelente! Seleccionaste la parte correcta del caballo")
                                    speech?.speak("¡Excelente!")
                                    gameState.value.setMessageType("success")
                                    soundPlayer?.playSound("success")
                                    delay(2000)
                                    gameState.value.setCustomMessage("¿Qué herramienta debemos utilizar para limpiarla?")
                                    speech?.speak("¿Qué herramienta debemos utilizar para limpiarla?")
                                    gameState.value.setMessageType("selection")
                                }
                            } else {
                                gameState.value.increaseError()
                                gameState.value.decreaseAttempts()
                                gameState.value.setCustomMessage("Ups... Seleccionaste la parte incorrecta. Intenta de nuevo.")
                                speech?.speak("Ups... Seleccionaste la parte incorrecta. Intenta de nuevo.")
                                gameState.value.setMessageType("error")
                                soundPlayer?.playSound("wrong")
                            }
                        })
                } else {
                    DirtyHorsePart(
                        part = stageInfo.correctHorsePart,
                        toolPosition = Offset(absoluteX,absoluteY),
                        soundManager = soundPlayer,
                        onPartCleaned = { isClean ->
                            if (isClean) {
                                gameState.value.addScore()
                                isAdvanceStageEnabled = gameState.value.advanceStage(cantStages)
                                if (isAdvanceStageEnabled) {
                                    offsetX = 0f
                                    offsetY = 0f
                                    stageInfo = checkNotNull(getStageInfo(gameState.value.getCurrentStage()))
                                    parts = stageInfo.incorrectRandomHorseParts + stageInfo.correctHorsePart
                                } else {
                                    showCompletionDialog = true
                                    isGameFinished = true
                                }
                                coroutineScope.launch {
                                    showZoomedView = false
                                    gameState.value.setCustomMessage("¡La parte está limpia! Avanzando a la siguiente etapa.")
                                    speech?.speak("Excelente")
                                    gameState.value.setMessageType("success")
                                    delay(3000)
                                    gameState.value.setMessageType("selection")
                                    gameState.value.setCustomMessage("¿Qué parte del caballo debemos seleccionar ahora?")
                                    speech?.speak("¿Qué parte del caballo debemos seleccionar ahora?")
                                }
                            }
                        }
                    )
                }

            }

            // Columna 3: Mensajes y Botón de herramientas
            Column(
                modifier = Modifier
                    .weight(1.2f)
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
                                    val positionInRoot = coordinates.positionInRoot()
                                    absoluteX = positionInRoot.x
                                    absoluteY = positionInRoot.y
                                }
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
                    shape = CircleShape,
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        Icons.Rounded.Build,
                        contentDescription = "Herramienta"
                    )
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
    correctToolId: Int?,
    highlightCorrectTool: Boolean,
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
                isHighlighted = (tool.imageRes == correctToolId) && highlightCorrectTool,
                onClick = { onImageSelected(tool) }
            )
        }
    }
}

@Composable
fun SelectableImage(imageRes: Int, isSelected: Boolean, isHighlighted: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .padding(8.dp)
            .border(
                width = if (isSelected || isHighlighted) 4.dp else 2.dp,
                color = if (isHighlighted && !isSelected) Color.Red else if (isSelected) Color(0xFFADD8E6) else Color.Black,
                shape = RoundedCornerShape(16.dp)
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
        "start" -> Pair(customMessage ?: "Después de correr por todos lados y ensuciarse, tenemos como desafío limpiar a Coquito. ¿Podras completar todos los pasos para limpiarlo?",
            R.drawable.vault_boy_thinking
        )
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
        "not complete" -> Pair(customMessage ?: "Ups... se te ha acabado el tiempo.",
            R.drawable.vault_boy_thumbs_down
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
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
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
fun GameAlertDialog(title: String, messageType: String? = null, titleButton: String, score: Int? = null, time: String? = null, onDismiss: () -> Unit) {
    val scrollState = rememberScrollState()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
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
                // Mensaje
                messageType?.let{
                    MessageBox(
                        messageType = messageType,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                // Puntaje final
                score?.let{
                    Text(
                        text = "Puntaje Final: $score",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    )
                }

                // Tiempo final
                time?.let {
                    Text(
                        text = "Tiempo Final: $time",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    )
                }
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
                    Text(titleButton, color = Color.Black)
                }
            }
        },
        containerColor = Color(0xFFFFE4B5) // Color de fondo del diálogo
    )
}

@Composable
fun LoadingDialog(
    isLoading: Boolean,
    onDismissRequest: () -> Unit = {} // Opcional, por si necesitas manejar el cierre
) {
    if (isLoading) {
        Dialog(
            onDismissRequest = onDismissRequest, // Esto se ejecuta si el usuario intenta cerrar el diálogo (puede estar vacío si no querés permitir cerrar)
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false) // Evita que se cierre accidentalmente
        ) {
            // Contenido del diálogo
            Box(
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(32.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Cargando...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun ShowCleaningProgressBar(currentStep: Int, totalSteps: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Progreso actual",
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
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(totalSteps) { step ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        if (step + 1 <= currentStep) Color(0xFFADD8E6) else Color.Gray,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (step + 1 < currentStep) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.Black
                                    )
                                } else {
                                    Text(text = (step + 1).toString(), color = Color.Black)
                                }
                            }
                        }
                    }
                }
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
                    Text("Cerrar", color = Color.Black)
                }
            }
        },
        containerColor = Color(0xFFFFE4B5)
    )
}

@RequiresApi(Build.VERSION_CODES.S)
@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewGameScreen() {
    GameScreen({ println("hola") })
}

@Preview
@Composable
fun PreviewGameAlertDialog() {
    GameAlertDialog (title = "A jugar y a aprender!", messageType = "start", titleButton = "Comenzar", onDismiss = {})
}

@Preview
@Composable
fun PreviewScrollableCleaningProgressBar(){
    ShowCleaningProgressBar(2, 6, onDismiss = {})
}