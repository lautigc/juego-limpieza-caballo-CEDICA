package com.cedica.cedica.ui.utils.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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