package com.cedica.cedica.ui.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
                text = stringResource(R.string.register_button_label),
                style = MaterialTheme.typography.labelLarge,

            )
        },
        modifier = modifier,
    )
}

data class FABItem(
    val icon: ImageVector,
    val text: String,
    val onClick: () -> Unit
)

@Composable
fun ExpandableFAB(
    modifier: Modifier = Modifier,
    items: List<FABItem>,
) {

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = modifier,
        elevation = CardDefaults.elevatedCardElevation(4.dp)
    ) {
        Column(Modifier.width(IntrinsicSize.Max).background(MaterialTheme.colorScheme.secondaryContainer)) {
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(1500)) + fadeIn(),
                exit = shrinkVertically(tween(1200)) + fadeOut(
                    animationSpec = tween(1000)
                )
            ) {
                ButtonList(items = items)
            }
            ExpandFAB(
                expanded = expanded,
                modifier = Modifier.fillMaxWidth(),
                onClick = { expanded = !expanded })
        }
    }
}

@Composable
private fun ExpandFAB(expanded: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .clip(MaterialTheme.shapes.large)
        ,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
        ,
        shape = MaterialTheme.shapes.small,
        elevation = CardDefaults.elevatedCardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(dimensionResource(R.dimen.padding_large))
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = Icons.Filled.Add, contentDescription = "refresh"
            )
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(animationSpec = tween(1500)) + fadeIn(),
                exit = shrinkVertically(tween(1200)) + fadeOut(tween(1200))
            ) {
                Row {
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))
                    Text(text = "Acciones")
                }
            }
        }
    }
}

@Composable
private fun ButtonList(
    items: List<FABItem>,
) {
    Column(
        modifier = Modifier
            .padding(all = dimensionResource(R.dimen.padding_small))
    ) {
        items.forEach { item ->
            Row(modifier = Modifier
                .clickable(
                    interactionSource = null,
                    indication = null,
                    onClick = item.onClick
                )
                .padding(all = dimensionResource(R.dimen.padding_medium))
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "refresh",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = item.text,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(
                        start = dimensionResource(R.dimen.padding_small),
                        end = dimensionResource(R.dimen.padding_small),
                    ),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandableFABPreview() {
    CedicaTheme {
        ExpandableFAB(
            items = listOf(
                FABItem(icon = Icons.Filled.Add, text = "Invitado") {},
                FABItem(icon = Icons.Filled.Add, text = "Registrar") {},
                FABItem(icon = Icons.Filled.Add, text = "Add") {},
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AddProfileButtonPreview() {
    CedicaTheme {
        AddProfileButton(onClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun ExpandFABPreview() {
    CedicaTheme {
        ExpandFAB(expanded = true, onClick = {})
    }
}