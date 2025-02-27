package com.cedica.cedica.ui.home

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.cedica.cedica.core.utils.exportToCSV
import com.cedica.cedica.core.utils.exportToPDF
import com.cedica.cedica.ui.theme.CedicaTheme
import kotlinx.coroutines.launch

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
    )
)

// Pantalla de estadísticas
@Composable
fun StatisticsScreen(studentName: String, gameSessions: List<GameSession>) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(Color(0xFFADD8E6))
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Título
            Text(
                text = "Progreso de $studentName",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Gráficos
            PerformanceCharts(gameSessions)

            // Historial de partidas
            Text(
                text = "Historial de Partidas",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                GameHistoryList(gameSessions)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                Button(onClick = {
                    exportToPDF(gameSessions)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("El pdf se guardó en descargas")
                    }
                })
                { Text("Exportar PDF") }
                Button(onClick = {
                    exportToCSV(gameSessions)
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("El csv se guardó en descargas")
                    }
                })
                { Text("Exportar CSV") }
            }
        }
    }
}

@Composable
fun PerformanceCharts(gameSessions: List<GameSession>) {
    Column {
        // Canvas for the chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            // Calculate maximum y-value for scaling
            val maxCorrect = gameSessions.maxOfOrNull { it.correctAnswers } ?: 0
            val maxIncorrect = gameSessions.maxOfOrNull { it.incorrectAnswers } ?: 0
            val maxY = maxOf(maxCorrect, maxIncorrect)
            val yScale = if (maxY > 0) size.height / maxY else 0f

            // Define spacing and padding in dp, converted to pixels
            val smallGapDp = 4.dp    // Gap between correct and incorrect bars
            val largeGapDp = 16.dp   // Gap between sessions
            val leftPaddingDp = 32.dp // Space for y-axis
            val smallGapPx = with(density) { smallGapDp.toPx() }
            val largeGapPx = with(density) { largeGapDp.toPx() }
            val leftPaddingPx = with(density) { leftPaddingDp.toPx() }

            // Calculate bar width based on available space
            val n = gameSessions.size
            val totalGapsWidth = if (n > 0) n * smallGapPx + (n - 1) * largeGapPx else 0f
            val availableWidth = size.width - leftPaddingPx
            val totalBarsWidth = availableWidth - totalGapsWidth
            val barWidth = if (n > 0 && totalBarsWidth > 0) totalBarsWidth / (2 * n) else 10f

            // Draw y-axis line
            drawLine(
                start = Offset(leftPaddingPx, 0f),
                end = Offset(leftPaddingPx, size.height),
                color = Color.Gray,
                strokeWidth = 2f
            )

            // Draw y-axis ticks and labels
            val numTicks = 5
            for (k in 0..numTicks) {
                val fraction = k / numTicks.toFloat()
                val y = size.height * (1 - fraction)
                // Tick mark
                drawLine(
                    start = Offset(leftPaddingPx - 8f, y),
                    end = Offset(leftPaddingPx, y),
                    color = Color.Gray,
                    strokeWidth = 2f
                )
                // Tick label
                drawIntoCanvas {
                    it.nativeCanvas.drawText(
                        String.format("%.1f", fraction * maxY),
                        leftPaddingPx - 12f,
                        y + 4f,
                        Paint().apply {
                            color = Color.Black.toArgb()
                            textSize = with(density) { 12.sp.toPx() }
                            textAlign = Paint.Align.RIGHT
                        }
                    )
                }
            }

            // Draw bars
            var currentX = leftPaddingPx
            gameSessions.forEach { session ->
                val correctBarHeight = session.correctAnswers * yScale
                val incorrectBarHeight = session.incorrectAnswers * yScale

                // Correct answers bar (green)
                drawRect(
                    color = Color.Green,
                    topLeft = Offset(currentX, size.height - correctBarHeight),
                    size = Size(barWidth, correctBarHeight)
                )

                // Incorrect answers bar (red)
                val rightBarX = currentX + barWidth + smallGapPx
                drawRect(
                    color = Color.Red,
                    topLeft = Offset(rightBarX, size.height - incorrectBarHeight),
                    size = Size(barWidth, incorrectBarHeight)
                )

                // Update position for next session
                currentX = rightBarX + barWidth + largeGapPx
            }
        }

        // Legend below the chart
        Row(
            modifier = Modifier.padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(16.dp).background(Color.Green))
            Text("Aciertos", modifier = Modifier.padding(start = 4.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Box(modifier = Modifier.size(16.dp).background(Color.Red))
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
        StatisticsScreen(
            studentName = "Juan Pérez",
            gameSessions = sampleGameSessions
        )
    }
}
