package com.cedica.cedica.ui.utils.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * AlertNotification is a class that can be used to display alerts to the user. The use expected is
 * in a ViewModel, where the ViewModel can display an alert to the user and the screen can observe the
 * alert and display it to the user.
 *
 * For Example:
 *
 * @Composable
 * fun screen(viewModel: MyViewModel) {
 *      val alert = viewModel.alert
 *      alert.value?.let {
 *          SimpleAlertDialog(
 *              ...
 *              onConfirmation = {alert.hiddenAlert()}
 *              ....
 *          )
 *       }
 * {
 */
class AlertNotification() {
    private val _alert = mutableStateOf<String?>(null)
    val alert: State<String?> get() = _alert

    fun displayAlert(alert: String) {
        this._alert.value = alert
    }

    fun hiddenAlert() {
        this._alert.value = null
    }
}

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
    displayDismissButton: Boolean = true,
    onDismissRequest: () -> Unit = {},
    onConfirmation: () -> Unit = {},
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
        dismissButton = if (displayDismissButton) {
            {
                TextButton(onClick = { onDismissRequest() }) {
                    Text("Cancelar")
                }
            }
        } else null
    )
}