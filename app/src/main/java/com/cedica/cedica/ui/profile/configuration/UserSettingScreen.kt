package com.cedica.cedica.ui.profile.configuration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    UserSettingScreenContent(
        level = viewModel.level,
        time = viewModel.time,
        imageCount = viewModel.imageCount,
        tryCount = viewModel.tryCount,
        voice = viewModel.voice,
        onSave = viewModel::onSave,
    )

}

@Composable
private fun UserSettingScreenContent(
    level: InputField<DifficultyLevel>,
    time: NumberField<Int>,
    imageCount: NumberField<Int>,
    tryCount: NumberField<Int>,
    onSave: () -> Unit,
    voice: InputField<VoiceType>
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
                level = level,
                time = time,
                imageCount = imageCount,
                tryCount = tryCount
            )
        }
        item {
            SoundSettingSection(
                optionModifier = optionModifier,
                onChangeConfiguration = onSave,
                voice = voice
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
    voice: InputField<VoiceType>,
    optionModifier: Modifier
) {

    val options = listOf(
        Option(
            label = "Tipo de voz",
            secondaryText = null,
            selector = { VoiceSelector(
                voice = voice,
                onChangeConfiguration = onChangeConfiguration,
            ) },
            arrangementSelector = Arrangement.Center
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
    level: InputField<DifficultyLevel>,
    time: NumberField<Int>,
    imageCount: NumberField<Int>,
    tryCount: NumberField<Int>,
) {
    val optionsDropdown = DifficultyLevel.entries.toList()
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        Option(
            label = "Nivel",
            secondaryText = null,
            selector = {
                SelectedDropdownMenu(
                    options = optionsDropdown,
                    expanded = expanded,
                    selectedOption = level.input,
                    onExpandedChange = {
                        expanded = !expanded
                    },
                    onCollapse = {
                        expanded = false
                    },
                    onSelectedText = {
                        level.onChange(DifficultyLevel.toDifficultyLevel(it))
                        onChangeConfiguration()
                    },
                )
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Tiempo",
            secondaryText = "Para completar el juego",
            selector = {
                NumberInput(
                    selectedValue = time.input.toString(),
                    onValueChange = { value ->
                        time.onChange(time.toInput(value, String::toInt, 0))
                        onChangeConfiguration()
                    },
                    hasError = time.hasError,
                    supportText = time.errorText
                )
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Cantidad de Imágenes",
            secondaryText = "Como ayuda en el juego",
            selector = {
                NumberInput(
                    selectedValue = imageCount.input.toString(),
                    onValueChange = { value ->
                        imageCount.onChange(imageCount.toInput(value, String::toInt, 0))
                        onChangeConfiguration()
                    },
                    hasError = imageCount.hasError,
                    supportText = imageCount.errorText,
                )
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Intentos",
            secondaryText = "Errores permitidos antes de restar puntos",
            selector = {
                NumberInput(
                    selectedValue = tryCount.input.toString(),
                    onValueChange = { value ->
                        tryCount.onChange(tryCount.toInput(value, String::toInt, 0))
                        onChangeConfiguration()
                    },
                    hasError = tryCount.hasError,
                    supportText = tryCount.errorText,
                )
            },
            arrangementSelector = Arrangement.Center
        )
    )
    SettingSection(
        title = "Dificultad",
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
 * Selector de tipo de voz
 *
 * @param modifier Modificador del elemento
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSelector(
    voice: InputField<VoiceType>,
    onChangeConfiguration: () -> Unit = {},
    modifier: Modifier = Modifier) {

    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(
        Triple(stringResource(R.string.man_voice_option_title), Icons.Outlined.Man, VoiceType.MALE),
        Triple(stringResource(R.string.woman_voice_option_title), Icons.Outlined.Woman, VoiceType.FEMALE),
    )

    selectedIndex = options.indexOfFirst { it.third == voice.input }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, pair ->
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
                label =
                {
                    Column {
                        Icon(imageVector = pair.second, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserSettingScreenPreview() {
    CedicaTheme {
        UserSettingScreenContent(
            level = InputField(DifficultyLevel.EASY),
            time = NumberField(rangeStart = 0, rangeEnd = Int.MAX_VALUE, initialValue = 0),
            imageCount = NumberField(rangeStart = 0, rangeEnd = 5, initialValue = 0),
            tryCount = NumberField(rangeStart = 0, rangeEnd = Int.MAX_VALUE, initialValue = 0),
            voice = InputField(VoiceType.FEMALE),
            onSave = {},
        )
    }
}

/**
 * Input de valores numéricos
 *
 * @param range rango de valores permitidos
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OutlinedTextField(
            value = selectedValue,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(60.dp),
            isError = hasError,
            textStyle = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
            singleLine = true,
        )

        if (hasError) {
            Text(
                text = supportText,
                maxLines = 2,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

/**
 * Selector de opciones desplegable.
 *
 * Muestra un campo de texto con un icono de desplegar que al hacer click muestra las opciones.
 *
 * @param options lista de opciones seleccionables
 * @param expanded indica si el menú está expandido
 * @param selectedOption indica el texto seleccionado
 * @param onExpandedChange función que se ejecuta al cambiar el estado de la expansión
 * @param onCollapse función que se ejecuta al colapsar el menú luego de ser desplegado
 * @param onSelectedText función que se ejecuta al seleccionar una nueva opcion
 * @param modifier modificador del menú
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectedDropdownMenu(
    options: List<T> = emptyList(),
    expanded: Boolean,
    selectedOption: T,
    onExpandedChange: () -> Unit,
    onCollapse: () -> Unit,
    onSelectedText: (selected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange() },
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor().width(125.dp).height(60.dp),
                value = selectedOption.toString(),
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                textStyle = MaterialTheme.typography.labelLarge.copy(textAlign = TextAlign.Center),
                singleLine = true,

            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = onCollapse,
            ) {
                options.forEachIndexed { _, item ->
                    DropdownMenuItem(
                        text = { Text(item.toString()) },
                        onClick = {
                            onSelectedText(item.toString())
                            onCollapse()
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }

            }
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
            SelectedDropdownMenu(
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