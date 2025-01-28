package com.cedica.cedica

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape

@Composable
fun GameScreen(navigateToMenu: () -> Unit) {
    val images = listOf(
        R.drawable.cepillo_blando,
        R.drawable.cepillo_duro,
        R.drawable.escarba_vasos,
        R.drawable.rasqueta_blanda,
        R.drawable.rasqueta_dura
    )

    var selectedImage by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFE4B5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Volver al Menú",
                color = Color.Blue,
                modifier = Modifier
                    .clickable { navigateToMenu() }
                    .padding(bottom = 16.dp)
            )

            // Mostrar la imagen seleccionada
            selectedImage?.let { imageRes ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color.Transparent)
                        .border(border = BorderStroke(2.dp, Color.Black), shape = CutCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }

        ImageSelectionList(
            images = images,
            onImageSelected = { selectedImage = it },
            modifier = Modifier
                .align(Alignment.BottomCenter) // Alineación válida dentro del Box
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun ImageSelectionList(
    images: List<Int>,
    onImageSelected: (Int) -> Unit,
    modifier: Modifier = Modifier // Permitir modificar el estilo desde afuera
) {
    var selectedImage by remember { mutableStateOf<Int?>(null) }

    LazyRow(
        modifier = modifier.fillMaxWidth(), // Usa el Modifier proporcionado
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(images) { imageRes ->
            SelectableImage(
                imageRes = imageRes,
                isSelected = selectedImage == imageRes,
                onClick = {
                    selectedImage = imageRes
                    onImageSelected(imageRes)
                }
            )
        }
    }
}

@Composable
fun SelectableImage(
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(150.dp)
            .padding(8.dp)
            .background(
                color = if (isSelected) Color(0xFFFFE4B5) else Color.Transparent,
                shape = CircleShape
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier.size(125.dp)
        )
    }
}

