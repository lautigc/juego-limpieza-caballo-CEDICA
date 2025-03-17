package com.cedica.cedica.ui.utils.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.ui.theme.CedicaTheme

/**
 * Dropdown option selector.
 *
 * Displays a text field with a dropdown icon that shows the options when clicked.
 *
 * @param options list of selectable options
 * @param expanded indicates if the menu is expanded
 * @param selectedOption indicates the selected text
 * @param onExpandedChange function executed when the expansion state changes
 * @param onCollapse function executed when the menu collapses after being expanded
 * @param onSelectedText function executed when a new option is selected
 * @param modifier menu modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SelectableDropdownMenu(
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

/**
 * This dropdown menu manages the state of the menu and the selected option.
 *
 * @param T type of the options
 * @param options list of selectable options
 * @param selectedOption selected option
 * @param onSelectedText function executed when a new option is selected
 * @param modifier menu modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> StatefulSelectableDropdownMenu(
    options: List<T> = emptyList(),
    selectedOption: T,
    onSelectedText: (selected: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    SelectableDropdownMenu(
        options = options,
        expanded = expanded,
        selectedOption = selectedOption,
        onExpandedChange = { expanded = !expanded },
        onCollapse = { expanded = false },
        onSelectedText = onSelectedText,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PreviewStatefulSelectableDropdownMenu() {
    val options = listOf("Option 1", "Option 2", "Option 3")
    var selectedOption by rememberSaveable { mutableStateOf(options.first()) }

    CedicaTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            StatefulSelectableDropdownMenu(
                options = options,
                selectedOption = selectedOption,
                onSelectedText = { selectedOption = it }
            )
        }
    }
}