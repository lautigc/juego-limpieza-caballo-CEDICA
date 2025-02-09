package com.cedica.cedica

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
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
        Text(text = label, style = MaterialTheme.typography.titleSmall)
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
    Column {
        Text("Volúmen", style = MaterialTheme.typography.titleMedium)
        VolumeSlider(label = "General")
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
        Button(
            onClick = { navigateToMenu() },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text("Volver")
        }
        Button(
            onClick = { navigateToMenu() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
        ) {
            Text("Guardar")
        }
    }
}

@Composable
fun AccessibilityConfiguration() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Accesibilidad", style = MaterialTheme.typography.titleMedium)

        var selectedVoice by remember { mutableStateOf("Masculino") }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Voz:", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
            Spacer(modifier = Modifier.weight(1f))

            RadioButton(
                selected = selectedVoice == "Masculino",
                onClick = { selectedVoice = "Masculino" }
            )
            Text("Masculino", modifier = Modifier.clickable { selectedVoice = "Masculino" })

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = selectedVoice == "Femenino",
                onClick = { selectedVoice = "Femenino" }
            )
            Text("Femenino", modifier = Modifier.clickable { selectedVoice = "Femenino" })
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            var hintsEnabled by remember { mutableStateOf(true) }
            Text("Pistas:", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = hintsEnabled, onCheckedChange = { hintsEnabled = it })
        }

    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfigurationScreen() {
    ConfigurationScreen(navigateToMenu = {
        println("Navigating to menu")
    })
}