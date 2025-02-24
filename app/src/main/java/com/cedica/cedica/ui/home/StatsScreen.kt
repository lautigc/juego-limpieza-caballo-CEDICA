package com.cedica.cedica.ui.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cedica.cedica.core.utils.exportToCSV
import com.cedica.cedica.core.utils.exportToPDF
import com.cedica.cedica.ui.theme.CedicaTheme


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
    Column(
        modifier = Modifier
            .fillMaxSize()
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
            Button(onClick = { exportToPDF(gameSessions, "ruta/al/archivo.pdf")})
            { Text("Exportar PDF") }
            Button(onClick = { exportToCSV(gameSessions, "ruta/al/archivo.csv") })
            { Text("Exportar CSV") }
        }
    }
}

// Gráficos de desempeño
@Composable
fun PerformanceCharts(gameSessions: List<GameSession>) {
    val maxCorrect = gameSessions.maxOfOrNull { it.correctAnswers } ?: 0
    val maxIncorrect = gameSessions.maxOfOrNull { it.incorrectAnswers } ?: 0
    val maxY = maxOf(maxCorrect, maxIncorrect)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        val barWidth = size.width / (gameSessions.size * 2)
        val yScale = size.height / maxY

        gameSessions.forEachIndexed { index, session ->
            val correctBarHeight = session.correctAnswers * yScale
            val incorrectBarHeight = session.incorrectAnswers * yScale

            // Dibujar barras de aciertos
            drawRect(
                color = Color.Green,
                topLeft = Offset(barWidth * index * 2, size.height - correctBarHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, correctBarHeight)
            )

            // Dibujar barras de errores
            drawRect(
                color = Color.Red,
                topLeft = Offset(barWidth * (index * 2 + 1), size.height - incorrectBarHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, incorrectBarHeight)
            )
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
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Fecha: ${session.date}", fontWeight = FontWeight.Bold)
            Text(text = "Nivel: ${session.difficultyLevel}")
            Text(text = "Aciertos: ${session.correctAnswers}")
            Text(text = "Errores: ${session.incorrectAnswers}")
            Text(text = "Tiempo: ${session.timeSpent} segundos")
        }
    }
}


// Preview
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
