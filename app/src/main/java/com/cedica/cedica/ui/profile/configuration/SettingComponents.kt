package com.cedica.cedica.ui.profile.configuration

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R

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
    HorizontalDivider(
        thickness = 1.dp,
        modifier = Modifier.padding(
            top = dimensionResource(R.dimen.padding_medium),
            bottom = dimensionResource(R.dimen.padding_medium),
        ),

        )
}

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