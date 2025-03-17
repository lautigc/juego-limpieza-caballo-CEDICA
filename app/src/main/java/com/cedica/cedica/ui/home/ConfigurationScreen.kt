package com.cedica.cedica.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.profile.configuration.LevelSelector
import com.cedica.cedica.ui.profile.configuration.NumberInput
import com.cedica.cedica.ui.profile.configuration.SettingOption
import com.cedica.cedica.ui.profile.configuration.UserSettingUiState
import com.cedica.cedica.ui.profile.configuration.UserSettingViewModel
import com.cedica.cedica.ui.profile.configuration.VoiceSelector
import com.cedica.cedica.ui.theme.CedicaTheme
import kotlin.math.roundToInt

@Composable
fun ConfigurationScreen(
    viewModelGlobalConfig: ConfigurationScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToMenu: () -> Unit
) {
    val viewModelPersonalConfig: UserSettingViewModel = viewModel(factory = AppViewModelProvider
        .FactoryWithArgs
        .userSetting(
            userID = viewModelGlobalConfig.getUserID(),
        )
    )

    val uiStateGlobalConfig by viewModelGlobalConfig.uiState.collectAsState()
    val uiStatePersonalConfig by viewModelPersonalConfig.uiState.collectAsState()

    ConfigurationScreenContent(
        navigateToMenu = navigateToMenu,
        globalConfig = uiStateGlobalConfig,
        personalConfig = uiStatePersonalConfig,
        onChangeGlobalConfig = viewModelGlobalConfig::saveConfiguration,
        onChangePersonalConfig = viewModelPersonalConfig::onSave,
    )
}

@Composable
private fun ConfigurationScreenContent(
    navigateToMenu: () -> Unit = {},
    globalConfig: ConfigurationScreenUiState = ConfigurationScreenUiState(),
    personalConfig: UserSettingUiState = UserSettingUiState(),
    onChangeGlobalConfig: () -> Unit = {},
    onChangePersonalConfig: () -> Unit = {},
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
            .padding(16.dp)
            .verticalScroll(scrollState),
    ) {
        val optionModifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        val labelStyle = MaterialTheme.typography.titleSmall

        Text(
            text = "Configuración",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        HorizontalDivider(modifier = Modifier.padding(16.dp), color = Color.Black)

        VolumeConfiguration(
            configuration = globalConfig,
            onChangeConfiguration = onChangeGlobalConfig,
        )

        HorizontalDivider(modifier = Modifier.padding(16.dp), color = Color.Black)

        SectionTitle("Dificultad") {
            DifficultyConfiguration(
                optionModifier = optionModifier,
                configuration = personalConfig,
                onChange = onChangePersonalConfig,
                labelStyle = MaterialTheme.typography.titleSmall,
            )
        }

        HorizontalDivider(modifier = Modifier.padding(16.dp), color = Color.Black)

        SectionTitle("Accesibilidad"

        ) {
            AccessibilityConfiguration(
                configuration = personalConfig,
                onChange = onChangePersonalConfig,
            )
        }
        ConfigButtons(navigateToMenu)
    }
}

@Composable
fun SectionTitle(
    title: String,
    content: @Composable() (ColumnScope.() -> Unit)
): Unit {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
        )
        content()
    }
}

@Composable
fun DifficultyConfiguration(
    optionModifier: Modifier,
    configuration: UserSettingUiState,
    onChange: () -> Unit = {},
    labelStyle: TextStyle,
) {
    SettingOption(
        label = "Nivel",
        labelStyle = labelStyle,
        selector = {
            Column {
                LevelSelector(
                    level = configuration.level,
                    onChangeConfiguration = onChange,
                )
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.End,
        horizontalDivider = false,
    )
    SettingOption(
        label = "Tiempo",
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
                NumberInput(
                    selectedValue = configuration.time.input.toString(),
                    onValueChange = { value ->
                        configuration.time.onChange(configuration.time.toInput(value, String::toInt, 0))
                        onChange()
                    },
                    hasError = configuration.time.hasError,
                    supportText = configuration.time.errorText
                )
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.Center,
        horizontalDivider = false,
    )
    SettingOption(
        label = "Intentos",
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
                NumberInput(
                    selectedValue = configuration.tryCount.input.toString(),
                    onValueChange = { value ->
                        configuration.tryCount.onChange(configuration.tryCount.toInput(value, String::toInt, 0))
                        onChange()
                    },
                    hasError = configuration.tryCount.hasError,
                    supportText = configuration.tryCount.errorText
                )
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.Center,
        horizontalDivider = false,
    )
    SettingOption(
        label = "Imagenes",
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
                NumberInput(
                    selectedValue = configuration.imageCount.input.toString(),
                    onValueChange = { value ->
                        configuration.imageCount.onChange(configuration.imageCount.toInput(value, String::toInt, 0))
                        onChange()
                    },
                    hasError = configuration.imageCount.hasError,
                    supportText = configuration.imageCount.errorText
                )
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.Center,
        horizontalDivider = false,
    )

}

@Composable
fun VolumeSlider(
    label: String,
    modifier: Modifier = Modifier,
    volume: InputField<Int>,
    onChange: () -> Unit = {},
) {
    val volumePercentage = volume.input
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
                        volume.onChange(if (isMuted) 0 else volume.input)
                    }
            )
            Slider(
                value = volume.input.toFloat(),
                onValueChange = {
                    volume.onChange(it.roundToInt())
                    onChange()
                },
                valueRange = 0f..100f
            )
        }
        Text(text = if (isMuted) "Mute" else "$volumePercentage%")
    }
}

@Composable
fun VolumeConfiguration(
    configuration: ConfigurationScreenUiState,
    onChangeConfiguration: () -> Unit,
) {
    Column {
        Text("Volúmen", style = MaterialTheme.typography.titleMedium)
        VolumeSlider(
            label = "General",
            volume = configuration.generalVolume,
            onChange = { onChangeConfiguration() },
        )
        VolumeSlider(
            label = "Música",
            volume = configuration.musicVolume,
            onChange = { onChangeConfiguration() },
        )
        VolumeSlider(
            label = "Efectos",
            volume = configuration.effectsVolume,
            onChange = { onChangeConfiguration() },
        )
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
    }
}

@Composable
fun AccessibilityConfiguration(
    configuration: UserSettingUiState,
    onChange: () -> Unit = {},
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            "Voz",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 8.dp)
        )
        Spacer(modifier = Modifier.weight(1f))

        VoiceSelector(
            voice = configuration.voice,
            onChangeConfiguration = onChange,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConfigurationScreen() {
    CedicaTheme {
        ConfigurationScreenContent(
            navigateToMenu = { println("Navigating to menu") }
        )
    }
}