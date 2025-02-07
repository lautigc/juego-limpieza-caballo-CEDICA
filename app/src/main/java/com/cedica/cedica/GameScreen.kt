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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
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
    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()


    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var isDraggable by remember { mutableStateOf(false) }
    var messageType by remember { mutableStateOf("selection") }
    var customMessage by remember { mutableStateOf<String?>(null) }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContainerColor = Color(0xFFFFE4B5),
        sheetShadowElevation = 8.dp,
        sheetContent = {
            ImageSelectionList(
                images = images,
                selectedTool = selectedTool,
                onImageSelected = { tool ->
                    if (selectedTool == null) {
                        selectedTool = tool
                        coroutineScope.launch {
                            messageType = "success"
                            delay(5000)
                            customMessage = "¿Qué parte del caballo hay que limpiar ahora?"
                            messageType = "selection"
                        }
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
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Button(onClick = { navigateToMenu() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFADD8E6))) {
                    Text("Volver al menú", color = Color.Black)
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
                Image(
                    painter = painterResource(R.drawable.caballo),
                    contentDescription = "Caballo",
                    modifier = Modifier
                        .size(350.dp)
                        .border(
                            width = if (selectedHorsePart) 4.dp else 0.dp,
                            color = if (selectedHorsePart) Color.Black else Color.Transparent
                        )
                        .clickable {
                            selectedHorsePart = true
                            isDraggable = true
                            customMessage = "¡Excelente! Seleccionaste la parte correcta del caballo"
                            coroutineScope.launch {
                                messageType = "success"
                            }
                        }
                )

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
}

@Composable
fun ImageSelectionList(
    images: List<Int>,
    selectedTool: Int?,
    onImageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
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
        "selection" -> Pair(customMessage ?: "¿Qué herramienta hay que seleccionar ahora?", R.drawable.vault_boy_thinking)
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


@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewGameScreen() {
    GameScreen { println("hola") }
}