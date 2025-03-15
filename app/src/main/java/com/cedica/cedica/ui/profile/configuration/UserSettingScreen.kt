package com.cedica.cedica.ui.profile.configuration

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.cedica.cedica.R
import com.cedica.cedica.ui.theme.CedicaTheme

/**
 * Elemento que representa una opción de configuración.
 *
 * @param label Titulo de la opción
 * @param secondaryText Texto complementario
 * @param selector Composable que representa el elemento seleccionable para la opcion
 * @param arrangementSelector Alineación del selector
 */
data class Option(
    val label: String = "",
    val secondaryText: String? = null,
    val selector: @Composable () -> Unit,
    val arrangementSelector: Arrangement.Horizontal = Arrangement.Start
)

/**
 * Elemento que representa una sección de configuración.
 *
 * Una sección agrupa una serie de opciones de configuración relacionadas.
 *
 * @param modifier: Modificador de la sección
 * @param title: Título de la sección
 * @param content: Contenido de la sección
 */
@Composable
fun SettingSection(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Card(modifier) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(
                    bottom = dimensionResource(R.dimen.padding_large)
                ),
                color = MaterialTheme.colorScheme.primary
            )
            content()
        }
    }
    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))
}

/**
 * Elemento que representa una opción de configuración.
 *
 * @param modifier Modificador del elemento
 * @param label Título de la opción
 * @param secondaryText Texto complementario de la opción
 * @param selector elemento de IU para configurar la opcion
 * @param arrangementSelector Alineación del selector
 */
@Composable
fun SettingOption(
    modifier: Modifier = Modifier,
    label: String,
    secondaryText: String?,
    selector: @Composable () -> Unit,
    arrangementSelector: Arrangement.Horizontal = Arrangement.Start,
) {
    Row(modifier = modifier) {
        Column(modifier = Modifier.width(150.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 2,
                modifier = Modifier
                    .horizontalScroll(rememberScrollState()),
            )
            secondaryText?.let {
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    modifier = Modifier.heightIn(max = 80.dp).verticalScroll(rememberScrollState())
                )
            }
        }
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Row(modifier = Modifier.weight(1f), horizontalArrangement = arrangementSelector) {
            selector()
        }
    }
    HorizontalDivider(thickness = 1.dp, modifier = Modifier.padding(
        top = dimensionResource(R.dimen.padding_medium),
        bottom = dimensionResource(R.dimen.padding_medium),
    ),

    )
}

/**
 * Pantalla de configuración de usuario.
 *
 * Representa las opciones de configuración personalizada para cada usuario.
 */
@Composable
fun UserSettingScreen() {
    val optionModifier = Modifier.padding(
        bottom = dimensionResource(R.dimen.padding_medium)
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        item { DifficultySettingSection(optionModifier = optionModifier) }
        item { SoundSettingSection(optionModifier = optionModifier) }
    }
}

/**
 * Sección de configuración de sonido
 *
 * @param optionModifier: Modificador aplicado a cada opción de la sección
 */
@Composable
private fun SoundSettingSection(optionModifier: Modifier) {

    val options = listOf(
        Option(
            label = "Tipo de voz",
            secondaryText = null,
            selector = { VoiceSelector() },
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
private fun DifficultySettingSection(optionModifier: Modifier) {
    var timeValue by rememberSaveable { mutableStateOf("0") }
    val timeRange = 0..Int.MAX_VALUE
    val hasErrorTimeValue by remember {
        derivedStateOf { outOfRange(timeValue, timeRange) }
    }


    var imageCountValue by rememberSaveable { mutableStateOf("0") }
    val imageCountRange = 0..5
    val hasErrorImageCountValue by remember {
        derivedStateOf { outOfRange(imageCountValue, imageCountRange) }
    }

    var tryCountValue by rememberSaveable { mutableStateOf("0") }
    val tryCountRange = 0..Int.MAX_VALUE
    val hasErrorTryCountValue by remember {
        derivedStateOf { outOfRange(tryCountValue, tryCountRange) }
    }

    val optionsDropdown = listOf("Fácil", "Medio", "Dificil")
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(optionsDropdown.first()) }

    val options = listOf(
        Option(
            label = "Nivel",
            secondaryText = null,
            selector = {
                SelectedDropdownMenu(
                    options = optionsDropdown,
                    expanded = expanded,
                    selectedText = selectedOption,
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
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Tiempo",
            secondaryText = "Para completar el juego",
            selector = {
                NumberInput(
                    range = timeRange,
                    selectedValue = timeValue,
                    onValueChange = { timeValue = it },
                    hasError = hasErrorTimeValue,
                )
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Cantidad de Imágenes",
            secondaryText = "Como ayuda en el juego",
            selector = {
                NumberInput(
                    range = imageCountRange,
                    selectedValue = imageCountValue,
                    onValueChange = { imageCountValue = it },
                    hasError = hasErrorImageCountValue
                )
            },
            arrangementSelector = Arrangement.Center
        ),
        Option(
            label = "Intentos",
            secondaryText = "Errores permitidos antes de restar puntos",
            selector = {
                NumberInput(
                    range = tryCountRange,
                    selectedValue = tryCountValue,
                    onValueChange = { tryCountValue = it },
                    hasError = hasErrorTryCountValue
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
fun VoiceSelector(modifier: Modifier = Modifier) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(
        Pair(stringResource(R.string.man_voice_option_title), Icons.Outlined.Man),
        Pair(stringResource(R.string.woman_voice_option_title), Icons.Outlined.Woman),
    )

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, pair ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { selectedIndex = index },
                selected = index == selectedIndex,
                label =
                {
                    Column {
                        Icon(imageVector = pair.second, contentDescription = null)
                       //Text(text = pair.first, style = MaterialTheme.typography.labelLarge)
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
        UserSettingScreen()
    }
}

/**
 * Validador de rango de valores.
 *
 * Se utiliza en NumberInput
 *
 * @param value valor a validar
 * @param range rango de valores permitido
 * @return true si el valor no está en el rango, false en caso contrario
 */
fun outOfRange(value: String, range: IntRange): Boolean {
   return  value.toIntOrNull()?.let {
        it !in range
    } ?: true
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
    range: IntRange,
    selectedValue: String,
    onValueChange: (String) -> Unit,
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

        val textError = if (range.last == Int.MAX_VALUE) {
            "El rango permitido es a partir de ${range.first}"
        } else {
            "El rango permitido es entre ${range.first} y ${range.last}"
        }
        if (hasError) {
            Text(
                text = textError,
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
 * @param selectedText indica el texto seleccionado
 * @param onExpandedChange función que se ejecuta al cambiar el estado de la expansión
 * @param onCollapse función que se ejecuta al colapsar el menú luego de ser desplegado
 * @param onSelectedText función que se ejecuta al seleccionar una nueva opcion
 * @param modifier modificador del menú
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectedDropdownMenu(
    options: List<String> = emptyList(),
    expanded: Boolean,
    selectedText: String,
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
                value = selectedText,
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
                        text = { Text(item) },
                        onClick = {
                            onSelectedText(item)
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
                selectedText = selectedOption,
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