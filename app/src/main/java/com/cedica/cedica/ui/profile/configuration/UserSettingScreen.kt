package com.cedica.cedica.ui.profile.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeOff
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.core.utils.input_field.NumberField
import com.cedica.cedica.data.configuration.DifficultyLevel
import com.cedica.cedica.data.configuration.VoiceType
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.composables.SelectableDropdownMenu
import com.cedica.cedica.ui.utils.composables.StatefulSelectableDropdownMenu

/**
 * Pantalla de configuración de usuario.
 *
 * Representa las opciones de configuración personalizada para cada usuario.
 */
@Composable
fun UserSettingScreen(
    userID: Long,
    viewModel: UserSettingViewModel = viewModel(
            factory = AppViewModelProvider.FactoryWithArgs.userSetting(userID = userID)
    ),
) {
    val uiState by viewModel.uiState.collectAsState()

    UserSettingScreenContent(
        uiState = uiState,
        onSave = viewModel::onSave
    )

}

@Composable
private fun UserSettingScreenContent(
    uiState: UserSettingUiState,
    onSave: () -> Unit,
) {
    val optionModifier = Modifier.padding(
        bottom = dimensionResource(R.dimen.padding_medium)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        item {
            DifficultySettingSection(
                optionModifier = optionModifier,
                onChangeConfiguration = onSave,
                uiState = uiState,
            )
        }
        item {
            SoundSettingSection(
                optionModifier = optionModifier,
                onChangeConfiguration = onSave,
                uiState = uiState,
            )
        }
    }
}

/**
 * Sección de configuración de sonido
 *
 * @param optionModifier: Modificador aplicado a cada opción de la sección
 */
@Composable
private fun SoundSettingSection(
    onChangeConfiguration: () -> Unit = {},
    uiState: UserSettingUiState,
    optionModifier: Modifier
) {

    val options = listOf(
        Option(
            label = stringResource(R.string.setting_voice_title),
            secondaryText = null,
            selector = { VoiceSelector(
                voice = uiState.voice,
                onChangeConfiguration = onChangeConfiguration,
            ) },
            arrangementSelector = Arrangement.End
        )
    )

    SettingSection(
        title = "Sonido",
    ) {
        options.forEach { option ->
            SettingOption(
                label = option.label,
                secondaryText = option.secondaryText,
                selector = option.selector,
                arrangementSelector = option.arrangementSelector,
                modifier = optionModifier,
            )
        }
    }
}

/**
 * Sección de configuración de dificultad
 *
 * @param optionModifier Modificador aplicado a cada opción de la sección
 */
@Composable
private fun DifficultySettingSection(
    optionModifier: Modifier,
    onChangeConfiguration: () -> Unit = {},
    uiState: UserSettingUiState,
) {
    val options = listOf(
        Option(
            label = stringResource(R.string.setting_level_title),
            secondaryText = null,
            selector = {
                LevelSelector(uiState, onChangeConfiguration)
            },
            arrangementSelector = Arrangement.End
        ),
        Option(
            label = stringResource(R.string.setting_time_title),
            secondaryText = stringResource(R.string.setting_time_secondary_text),
            selector = {
                TimeInput(uiState, onChangeConfiguration)
            },
            arrangementSelector = Arrangement.End
        ),
        Option(
            label = stringResource(R.string.setting_images_title),
            secondaryText = stringResource(R.string.setting_images_secondary_text),
            selector = {
                ImageCountInput(uiState, onChangeConfiguration)
            },
            arrangementSelector = Arrangement.End
        ),
        Option(
            label = stringResource(R.string.setting_try_title),
            secondaryText = stringResource(R.string.setting_try_secondary_text),
            selector = {
                TryCountInput(uiState, onChangeConfiguration)
            },
            arrangementSelector = Arrangement.End
        )
    )
    SettingSection(
        title = stringResource(R.string.setting_dificulty_title),
    ) {
        options.forEach { option ->
            SettingOption(
                label = option.label,
                secondaryText = option.secondaryText,
                selector = option.selector,
                arrangementSelector = option.arrangementSelector,
                modifier = optionModifier,
            )
        }
    }
}

@Composable
fun TryCountInput(
    uiState: UserSettingUiState,
    onChangeConfiguration: () -> Unit
) {
    NumberInput(
        selectedValue = uiState.tryCount.input.toString(),
        onValueChange = { value ->
            uiState.tryCount.onChange(uiState.tryCount.toInput(value, String::toInt, 0))
            uiState.level.onChange(DifficultyLevel.CUSTOM)
            onChangeConfiguration()
        },
        hasError = uiState.tryCount.hasError,
        supportText = uiState.tryCount.errorText,
    )
}

@Composable
fun ImageCountInput(
    uiState: UserSettingUiState,
    onChangeConfiguration: () -> Unit
) {
    NumberInput(
        selectedValue = uiState.imageCount.input.toString(),
        onValueChange = { value ->
            uiState.imageCount.onChange(uiState.imageCount.toInput(value, String::toInt, 0))
            uiState.level.onChange(DifficultyLevel.CUSTOM)
            onChangeConfiguration()
        },
        horizontalAligment = Alignment.End,
        hasError = uiState.imageCount.hasError,
        supportText = uiState.imageCount.errorText,
    )
}

@Composable
fun TimeInput(
    uiState: UserSettingUiState,
    onChangeConfiguration: () -> Unit
) {
    NumberInput(
        selectedValue = uiState.time.input.toString(),
        onValueChange = { value ->
            uiState.time.onChange(uiState.time.toInput(value, String::toInt, 0))
            uiState.level.onChange(DifficultyLevel.CUSTOM)
            onChangeConfiguration()
        },
        horizontalAligment = Alignment.End,
        hasError = uiState.time.hasError,
        supportText = uiState.time.errorText
    )
}

@Composable
fun LevelSelector(
    uiState: UserSettingUiState,
    onChangeConfiguration: () -> Unit
) {
    val optionsDropdown = DifficultyLevel.entries.toList()

    StatefulSelectableDropdownMenu(
        options = optionsDropdown,
        selectedOption = uiState.level.input,
        onSelectedText = {
            uiState.level.onChange(DifficultyLevel.toDifficultyLevel(it))
            uiState.time.onChange(uiState.level.input.getSecondsTime(uiState.time.input))
            uiState.imageCount.onChange(uiState.level.input.getNumberOfImages(uiState.imageCount.input))
            uiState.tryCount.onChange(uiState.level.input.getNumberOfAttempts(uiState.tryCount.input))
            onChangeConfiguration()
        },
    )
}

/**
 * Selector de tipo de voz
 *
 * @param modifier Modificador del elemento
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSelector(
    voice: InputField<VoiceType>,
    onChangeConfiguration: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(
        Triple("Deshabilitado", Icons.AutoMirrored.Outlined.VolumeOff, VoiceType.NONE),
        Triple(stringResource(R.string.man_voice_option_title), Icons.Outlined.Man, VoiceType.MALE),
        Triple(stringResource(R.string.woman_voice_option_title), Icons.Outlined.Woman, VoiceType.FEMALE),
    )

    selectedIndex = options.indexOfFirst { it.third == voice.input }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, triple ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex = index
                    voice.onChange(options[selectedIndex].third)
                    onChangeConfiguration()
                },
                selected = index == selectedIndex,
                label = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = triple.second,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp) // Ajusta el tamaño del icono si es necesario
                        )
                    }
                },
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun UserSettingScreenPreview() {
    CedicaTheme {
        UserSettingScreenContent(
        uiState = UserSettingUiState(
            level = InputField(DifficultyLevel.CUSTOM),
            time = NumberField(rangeStart = 0, rangeEnd = Int.MAX_VALUE, initialValue = 0),
            imageCount = NumberField(rangeStart = 0, rangeEnd = 5, initialValue = 0),
            tryCount = NumberField(rangeStart = 0, rangeEnd = 10, initialValue = 0),
            voice = InputField(VoiceType.FEMALE)
        ),
        onSave = {},
        )
    }
}

/**
 * Input de valores numéricos
 *
 * @param selectedValue valor seleccionado
 * @param onValueChange función que se ejecuta al cambiar el valor
 * @param hasError indicador si hay un error en el valor
 * @param modifier modificador del elemento
 */
@Composable
fun NumberInput(
    selectedValue: String,
    onValueChange: (String) -> Unit,
    supportText: String = "",
    hasError: Boolean = false,
    horizontalAligment: Alignment.Horizontal = Alignment.End,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = horizontalAligment,
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(80.dp),
            isError = hasError,
            //supportingText = { if (hasError) Text(text = supportText) },
            textStyle = MaterialTheme.typography.bodySmall.copy(textAlign = TextAlign.Center),
            singleLine = true,
        )
        if (hasError) {
            Text(
                text = supportText,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Preview() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }

    CedicaTheme {
        Column(Modifier.fillMaxSize()) {
            SelectableDropdownMenu(
                options = options,
                expanded = expanded,
                selectedOption = selectedOption,
                onExpandedChange = {
                    expanded = !expanded
                },
                onCollapse = {
                    expanded = false
                },
                onSelectedText = {
                    selectedOption = it
                },
            )
        }
    }
}