package com.cedica.cedica

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun ConfigurationScreen(navigateToMenu: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6)) // Fondo azul claro
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "Configuración", style = MaterialTheme.typography.titleLarge)
        HorizontalDivider(modifier = Modifier.padding(16.dp), color = Color.Black)
        VolumeConfiguration()
        HorizontalDivider(modifier = Modifier.padding(16.dp), color = Color.Black)
        AccessibilityConfiguration()
        ConfigButtons(navigateToMenu)
    }
}

@Composable
fun VolumeSlider(
    label: String,
    modifier: Modifier = Modifier,
    initialVolume: Float = 50f
) {
    var sliderPosition by remember { mutableFloatStateOf(initialVolume) }
    val volumePercentage = sliderPosition.roundToInt()
    var isMuted = volumePercentage == 0

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(16.dp)
    ){
        Text(text = label, style = MaterialTheme.typography.titleMedium)
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = if(isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = if(isMuted) "Mute" else "Volume",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        isMuted = !isMuted
                        sliderPosition = if (isMuted) 0f else initialVolume
                    }
            )
            Slider(
                value = sliderPosition,
                onValueChange = { sliderPosition = it },
                valueRange = 0f..100f
            )
        }
        Text(text = if (isMuted) "Mute" else "$volumePercentage%")
    }
}

@Composable
fun VolumeConfiguration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        VolumeSlider(label = "Música")
        VolumeSlider(label = "Efectos")
    }
}

@Composable
fun ConfigButtons(navigateToMenu: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = { navigateToMenu() }) {
            Text("Volver")
        }
        Button(onClick = { /* Acción de guardar */ }) {
            Text("Guardar")
        }
    }
}

@Composable
fun AccessibilityConfiguration() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfigurationScreen() {
    ConfigurationScreen(navigateToMenu = {
        println("Navigating to menu")
    })
}