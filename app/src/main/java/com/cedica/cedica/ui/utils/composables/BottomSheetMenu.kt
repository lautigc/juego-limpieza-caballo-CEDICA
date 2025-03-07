package com.cedica.cedica.ui.utils.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import kotlinx.coroutines.launch

/**
 * The BottomSheetMenu composable is a custom implementation of a BottomSheet that displays a menu
 * This composable manages the state of the BottomSheet and the content that will be displayed
 *
 * @param expandElement The composable that will be displayed to expand the BottomSheet
 * @param contentPaddingValues The padding values for the content of the BottomSheet
 * @param modifier The modifier for the BottomSheet
 * @param content The content of the BottomSheet. It receives:
 *  - showBottomSheet: The internal MutableState that is used to manage the visibility of the BottomSheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetMenu(
    expandElement: @Composable (onExpandedMenu: () -> Unit) -> Unit = {},
    contentPaddingValues: PaddingValues = PaddingValues(0.dp),
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.(showBottomSheet: MutableState<Boolean>) -> Unit = {},
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()
    var showBottomSheet = rememberSaveable { mutableStateOf(false) }
    val expandButton = {
        scope.launch {
            sheetState.expand()
        }.invokeOnCompletion {
            showBottomSheet.value = true
        }
    }

    Box(
        modifier.clickable(
            onClick = { expandButton() },
        )
    ) {
        expandElement({ expandButton() })
    }

    if (showBottomSheet.value) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showBottomSheet.value = false
                    }
                }
            },
            sheetState = sheetState,
            modifier = modifier,
        ) {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(contentPaddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                content(showBottomSheet)
            }
        }
    }
}

/**
 * Composable that displays a item in the BottomSheetMenu
 *
 * @param label The text that will be displayed in the item
 * @param leadingIcon The icon that will be displayed at the start of the item
 * @param horizontalArrangement The horizontal arrangement of the item
 * @param modifier The modifier for the item
 * @param onClick The lambda that will be called when the item is clicked
 */
@Composable
fun BottomSheetMenuItem(
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        leadingIcon?.invoke()
        Spacer(Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text = label,
            style = MaterialTheme.typography.titleLarge,
        )
    }
    Spacer(Modifier.height(dimensionResource(R.dimen.padding_medium)))
}