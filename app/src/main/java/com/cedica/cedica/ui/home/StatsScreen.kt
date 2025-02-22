package com.cedica.cedica.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatsScreen() {
    val scrollState = rememberScrollState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(0xFFADD8E6))
        ,
        contentAlignment = Alignment.TopCenter,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            // TODO: Tomar el nombre del alumno
            Text("Progreso del alumno",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
            )

            GameHistory()
        }
    }
}

@Composable
fun GameHistory() {
    // TODO: tomar el historial de partidas
    Column() {
        GameHistoryItem()
    }
}

@Composable
fun GameHistoryItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
            .padding(4.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("Partida")
        Text("Fecha: 25-2-3")
    }
}

//@Preview
//@Composable
//fun PreviewStatsScreen() {
//    StatsScreen()
//}

@Preview
@Composable
fun PreviewGameHistoryItem() {
    GameHistoryItem()
}