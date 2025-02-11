package com.cedica.cedica.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.ui.theme.CedicaTheme


data class FABItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ExpandableFAB(onGuestLogin: () -> Unit = {}) {
    val itemModifier = Modifier.padding(
        top = dimensionResource(R.dimen.padding_small),
        bottom = dimensionResource(R.dimen.padding_small)
    )

    BottomSheetMenu(
        contentPaddingValues = PaddingValues(
            bottom = dimensionResource(R.dimen.padding_large),
            start = dimensionResource(R.dimen.padding_large),
            end = dimensionResource(R.dimen.padding_large),
        ),
        expandElement = { onExpandedMenu ->
            LargeFloatingActionButton(
                onClick = onExpandedMenu,
                modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_medium)),
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null,)
            }
        },
    ) {
        Text(
            text = "Acciones generales",
            modifier = Modifier.padding(
                bottom = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_large),
            ).fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary
        )

        HorizontalDivider(
            thickness = 1.5.dp,
            modifier = Modifier
                .padding(
                    bottom = dimensionResource(R.dimen.padding_medium),
                )
        )

        BottomSheetMenuItem(
            label = "Registrar usuario",
            leadingIcon = { Icon(imageVector = Icons.Filled.Add, contentDescription = null) },
            onClick = { },
            modifier = Modifier.padding(
                bottom = dimensionResource(R.dimen.padding_medium),
            ),
        )

        BottomSheetMenuItem(
            label = "Ingresar como invitado",
            leadingIcon = { Icon(imageVector = Icons.AutoMirrored.Filled.Login, contentDescription = null) },
            onClick = onGuestLogin,
            modifier = itemModifier,
        )
    }

}


@Preview(showBackground = true)
@Composable
fun ExpandableFABPreview() {
    CedicaTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            ExpandableFAB()
        }
    }
}