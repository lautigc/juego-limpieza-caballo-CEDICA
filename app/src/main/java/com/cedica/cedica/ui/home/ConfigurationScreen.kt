package com.cedica.cedica.ui.home

import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.profile.configuration.ImageCountInput
import com.cedica.cedica.ui.profile.configuration.LevelSelector
import com.cedica.cedica.ui.profile.configuration.SettingOption
import com.cedica.cedica.ui.profile.configuration.TimeInput
import com.cedica.cedica.ui.profile.configuration.TryCountInput
import com.cedica.cedica.ui.profile.configuration.UserSettingUiState
import com.cedica.cedica.ui.profile.configuration.UserSettingViewModel
import com.cedica.cedica.ui.profile.configuration.VoiceSelector
import com.cedica.cedica.ui.theme.CedicaTheme
import kotlin.math.roundToInt

@Composable
fun ConfigurationScreen(
    viewModel: ConfigurationScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToMenu: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val viewModelPersonalConfig: UserSettingViewModel = viewModel(factory = AppViewModelProvider
        .FactoryWithArgs
        .userSetting(
            userID = viewModel.getUserID(),
        )
    )

    val uiStatePersonalConfig by viewModelPersonalConfig.uiState.collectAsState()

    ConfigurationScreenContent(
        navigateToMenu = navigateToMenu,
        globalConfig = uiState,
        personalConfig = uiStatePersonalConfig,
        onChangeGlobalConfig = viewModel::saveConfiguration,
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
                labelStyle = labelStyle,
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
    content: @Composable (ColumnScope.() -> Unit)
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
        label = stringResource(R.string.setting_level_title),
        labelStyle = labelStyle,
        selector = {
            Column {
                LevelSelector(
                    uiState =  configuration,
                    onChangeConfiguration = onChange,
                )
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.End,
        horizontalDivider = false,
    )
    SettingOption(
        label = stringResource(R.string.setting_time_title),
        secondaryText = stringResource(R.string.setting_time_secondary_text),
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
                TimeInput(uiState = configuration, onChangeConfiguration = onChange)
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.End,
        horizontalDivider = false,
    )
    SettingOption(
        label = stringResource(R.string.setting_try_title),
        secondaryText = stringResource(R.string.setting_try_secondary_text),
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
                TryCountInput(uiState = configuration, onChangeConfiguration = onChange)
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.End,
        horizontalDivider = false,
    )
    SettingOption(
        label = stringResource(R.string.setting_images_title),
        secondaryText = stringResource(R.string.setting_images_secondary_text),
        labelStyle = labelStyle,
        selector = {
            Column(Modifier.padding(start = 16.dp)) {
            ImageCountInput(uiState = configuration, onChangeConfiguration = onChange)
            }
        },
        modifier = optionModifier,
        arrangementSelector = Arrangement.End,
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
        Text(text = stringResource(R.string.setting_voice_title), style = MaterialTheme.typography.titleMedium)
        VolumeSlider(
            label = "General",
            volume = configuration.generalVolume,
            onChange = { onChangeConfiguration() },
        )
//        VolumeSlider(
//            label = "Música",
//            volume = configuration.musicVolume,
//            onChange = { onChangeConfiguration() },
//        )
//        VolumeSlider(
//            label = "Efectos",
//            volume = configuration.effectsVolume,
//            onChange = { onChangeConfiguration() },
//        )
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