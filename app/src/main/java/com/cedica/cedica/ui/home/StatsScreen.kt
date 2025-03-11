package com.cedica.cedica.ui.home

import android.content.pm.ActivityInfo
import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.core.utils.exportToCSV
import com.cedica.cedica.core.utils.exportToPDF
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.game.LockScreenOrientation
import com.cedica.cedica.ui.game.PlaySessionViewModel
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.view_models.UserUiState
import com.cedica.cedica.ui.utils.view_models.UserViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.stream.Collectors

// Modelo de datos
data class GameSession(
    val date: String,
    val difficultyLevel: String,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val timeSpent: Int // Tiempo en segundos
)


// Datos de prueba
val sampleGameSessions = listOf(
    GameSession(
        date = "2023-10-01",
        difficultyLevel = "Fácil",
        correctAnswers = 8,
        incorrectAnswers = 2,
        timeSpent = 120
    ),
    GameSession(
        date = "2023-10-02",
        difficultyLevel = "Medio",
        correctAnswers = 6,
        incorrectAnswers = 4,
        timeSpent = 150
    ),
    GameSession(
        date = "2023-10-03",
        difficultyLevel = "Difícil",
        correctAnswers = 5,
        incorrectAnswers = 5,
        timeSpent = 180
    ),
    GameSession(
        date = "2023-10-01",
        difficultyLevel = "Fácil",
        correctAnswers = 8,
        incorrectAnswers = 2,
        timeSpent = 120
    ),
    GameSession(
        date = "2023-10-02",
        difficultyLevel = "Medio",
        correctAnswers = 6,
        incorrectAnswers = 4,
        timeSpent = 150
    ),
    GameSession(
        date = "2023-10-03",
        difficultyLevel = "Difícil",
        correctAnswers = 5,
        incorrectAnswers = 5,
        timeSpent = 180
    )

)

@Composable
fun StatisticsScreen(
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsState()
    val patientViewModel: PatientViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val patientUiState  by patientViewModel.playSessions.collectAsState()

    StaticStatisticScreen(uiState.user.firstName, patientUiState.playSessions)
}

@Composable
private fun StaticStatisticScreen(
    username: String,
    gameSessions: List<PlaySession>
) {
    // Esto es para orientar la pantalla en sentido vertical
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    var showChart by remember { mutableStateOf(true) } // Estado para controlar la visibilidad del gráfico

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color(0xFFADD8E6)) // Fondo azul claro
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {


            // Título
            Text(
                text = "Progreso de $username",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Botón para alternar la visibilidad del gráfico
            Button(
                onClick = { showChart = !showChart },
                modifier = Modifier
                    .padding(bottom = 8.dp)
            )
            {
                Text(
                    text = if (showChart) "Ocultar gráfico de partidas recientes" else "Mostrar gráfico de partidas recientes",
                )
            }

            // Gráfico de partidas recientes (visible solo si showChart es true)
            if (showChart) {
                Box(
                    modifier = Modifier
                        .background(Color.LightGray)
                        .border(2.dp, Color.Black)
                        .padding(16.dp)
                ) {
                    PerformanceCharts(gameSessions)
                }
            }

            // Historial de partidas
            Text(
                text = "Historial de Partidas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f) // Ocupa el espacio restante
                    .fillMaxWidth()
            ) {
                GameHistoryList(gameSessions)
            }

            // Botones de exportación
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    exportToPDF(gameSessions)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("El pdf se guardó en descargas")
                    }
                }) { Text("Exportar PDF") }
                Button(onClick = {
                    exportToCSV(gameSessions)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("El csv se guardó en descargas")
                    }
                }) { Text("Exportar CSV") }
            }
        }
    }
}

@Composable
fun PerformanceCharts(gameSessions: List<GameSession>) {
    Column {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // Aumentamos la altura para dar espacio a las etiquetas del eje x
        ) {
            var fGameSessions = gameSessions.toMutableList()
            val len = fGameSessions.size
            val cut = 5 // valor de corte
            if (len > cut) {
                fGameSessions = fGameSessions.slice(IntRange(len - cut, len - 1)).toMutableList()
                Log.d("longitud", "sesiones les ${fGameSessions.size}")
            }

            val maxCorrect = fGameSessions.maxOfOrNull { it.correctAnswers } ?: 0
            val maxIncorrect = fGameSessions.maxOfOrNull { it.incorrectAnswers } ?: 0
            val maxY = maxOf(maxCorrect, maxIncorrect)
            val yScale = if (maxY > 0) size.height / maxY else 0f

            val smallGapDp = 4.dp    // Espacio entre correctas e incorrectas
            val largeGapDp = 16.dp   // Espacio entre sesiones
            val leftPaddingDp = 8.dp // Espacio para el eje y

            val smallGapPx = smallGapDp.toPx()
            val largeGapPx = largeGapDp.toPx()
            val leftPaddingPx = leftPaddingDp.toPx()
            val moveBarsFromLeft = 10

            // Calcular el ancho de la barra en base al espacio disponible
            val n = fGameSessions.size
            val totalGapsWidth = if (n > 0) n * smallGapPx + (n - 1) * largeGapPx else 0f
            val availableWidth = size.width - leftPaddingPx
            val totalBarsWidth = availableWidth - totalGapsWidth
            val barWidth = if (n > 0 && totalBarsWidth > 0) totalBarsWidth / (2 * n) else 10f

            // Línea del eje x
            drawLine(
                start = Offset(leftPaddingPx - 2, size.height + 2),
                end = Offset(size.width + 12, size.height + 2),
                color = Color.Gray,
                strokeWidth = 4f
            )

            // Línea del eje y
            drawLine(
                start = Offset(leftPaddingPx, 0f),
                end = Offset(leftPaddingPx, size.height),
                color = Color.Gray,
                strokeWidth = 4f
            )

            // Etiquetas y marcas del eje y
            val numTicks = 5
            for (k in 0..numTicks) {
                val fraction = k / numTicks.toFloat()
                val y = size.height * (1 - fraction)
                // Marca
                drawLine(
                    start = Offset(leftPaddingPx - 8f, y),
                    end = Offset(leftPaddingPx, y),
                    color = Color.Gray,
                    strokeWidth = 2f
                )
                // Etiqueta
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        String.format(Locale.getDefault(), "%.0f", fraction * maxY),
                        leftPaddingPx - 12f,
                        y + 4f,
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 12.sp.toPx()
                            textAlign = Paint.Align.RIGHT
                        }
                    )
                }
            }

            // Dibujar barras y etiquetas del eje x
            var currentX = leftPaddingPx + moveBarsFromLeft
            fGameSessions.forEachIndexed { index, session ->
                val correctBarHeight = session.correctAnswers * yScale
                val incorrectBarHeight = session.incorrectAnswers * yScale

                // Barra de aciertos (verde)
                drawRect(
                    color = Color.Green,
                    topLeft = Offset(currentX, size.height - correctBarHeight),
                    size = Size(barWidth, correctBarHeight),
                )

                // Barra de errores (roja)
                val rightBarX = currentX + barWidth + smallGapPx
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(rightBarX, size.height - incorrectBarHeight),
                    size = Size(barWidth, incorrectBarHeight)
                )

                // Calcular posición de la etiqueta del eje x
                val labelX = currentX + barWidth + smallGapPx / 2 // Centro del par de barras
                val labelY = size.height + 20.sp.toPx() // Debajo del eje x

                // Dibujar etiqueta del eje x
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        "Sesión ${index + 1}", // Etiqueta como "Sesión 1", "Sesión 2", etc.
                        labelX,
                        labelY,
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = 12.sp.toPx()
                            textAlign = Paint.Align.CENTER // Centrar el texto en labelX
                        }
                    )
                }

                // Actualizar posición para la próxima sesión
                currentX = rightBarX + barWidth + largeGapPx
            }
        }

        // Leyenda debajo del gráfico
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 23.dp),
        ) {
            Box(modifier = Modifier.size(8.dp).background(Color.Green))
            Text("Aciertos", modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).background(Color.Red))
            Text("Errores", modifier = Modifier.padding(start = 4.dp))
        }
    }
}

// Historial de partidas
@Composable
fun GameHistoryList(gameSessions: List<GameSession>) {
        LazyColumn {
            items(gameSessions) { session ->
                GameSessionItem(session)
                GameSessionItem(session)
            }
        }
}

// Item de sesión de juego
@Composable
fun GameSessionItem(session: GameSession) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = "Fecha: ${session.date}", fontWeight = FontWeight.Bold)
            Text(text = "Nivel: ${session.difficultyLevel}")
            Text(text = "Aciertos: ${session.correctAnswers}")
            Text(text = "Errores: ${session.incorrectAnswers}")
            Text(text = "Tiempo: ${session.timeSpent} segundos")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun StatisticsScreenPreview() {
    CedicaTheme {
        StaticStatisticScreen(
            username = "Juan Pérez",
            gameSessions = sampleGameSessions
        )
    }
}
