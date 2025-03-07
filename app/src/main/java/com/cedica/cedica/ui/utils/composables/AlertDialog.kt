package com.cedica.cedica.ui.utils.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Simple Alert Dialog that can be used to display information to the user. This component don't
 * have any state, so it's necessary to manage the state in the parent component.
 *
 * @param onDismissRequest: A lambda that is called when the dialog is dismissed.
 * @param onConfirmation: A lambda that is called when the user confirms the dialog.
 * @param dialogTitle: The title of the dialog.
 * @param dialogText: The text of the dialog.
 * @param icon: The icon to display in the dialog.
 */
@Composable
fun SimpleAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}