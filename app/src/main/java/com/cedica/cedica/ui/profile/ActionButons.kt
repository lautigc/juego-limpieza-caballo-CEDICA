package com.cedica.cedica.ui.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.cedica.cedica.R
import com.cedica.cedica.ui.theme.CedicaTheme


@Composable
fun AddProfileButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = { onClick() },
        icon =
        {
            Icon(Icons.Filled.Add, contentDescription = null)
        },
        text =
        {
            Text(
                text = stringResource(R.string.register_button_title),
                style = MaterialTheme.typography.labelLarge,

            )
        },
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun AddProfileButtonPreview() {
    CedicaTheme {
        AddProfileButton(onClick = {})
    }
}