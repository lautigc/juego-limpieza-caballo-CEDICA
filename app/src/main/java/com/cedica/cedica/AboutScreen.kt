package com.cedica.cedica

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// TODO: Arreglar la pantalla para la versión horizontal

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AboutScreen(navigateToMenu: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Acerca de") },
                navigationIcon = {
                    Icon(Icons.Filled.Info, "Acerca de")
                })
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_clean_textless),
                contentDescription = "Logo",
                modifier = Modifier.size(220.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = "Aplicación desarrollada por el CEDICA.", textAlign = TextAlign.Center)
            Text(text = "Versión 1.0", textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.size(32.dp))
            Text(
                text = "Alumnos: Franco Kumichel, Gustavo Lopez, Lautaro Castro.",
                textAlign = TextAlign.Center
            )
            Button(
                onClick = { navigateToMenu() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.padding(top = 64.dp)
            ) {
                Text("Volver")
            }
        }
    }
}

@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen {  }
}