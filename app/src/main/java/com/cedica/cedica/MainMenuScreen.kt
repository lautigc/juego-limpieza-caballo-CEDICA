package com.cedica.cedica

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cedica.cedica.core.navigation.About
import com.cedica.cedica.core.navigation.NavigationWrapper
import com.cedica.cedica.ui.theme.CedicaTheme

data class MenuItem(val text: String, val destination: Any)

val menuItems = listOf(
    MenuItem("Jugar", About),
    MenuItem("Progreso", About),
    MenuItem("Acerca de", About)
)

@Composable
fun MenuButton(text: String, modifier: Modifier = Modifier, navigateTo: () -> Unit) {
    FilledTonalButton(
        onClick = navigateTo,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = Color(0xFFFFE4B5),
            contentColor = Color.Black
        ),
        modifier = modifier.padding(8.dp)
    ) {
        Text(text)
    }
}

@Composable
fun TopBar(navController: NavController, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Logo a la izquierda
        Image(
            painter = painterResource(id = R.drawable.logo_clean_textless),
            contentDescription = "Logo",
            modifier = Modifier.size(50.dp),
        )

        // Espaciador flexible
        Spacer(modifier = Modifier.weight(1f))

        // Botón de perfil
        IconButton(onClick = { navController.navigate(com.cedica.cedica.core.navigation.Configuration) }) {
            Icon(
                imageVector = Icons.Rounded.AccountCircle,
                contentDescription = "Perfil",
                modifier = Modifier.size(60.dp)
            )
        }

        // Botón de configuración
        IconButton(onClick = { navController.navigate(com.cedica.cedica.core.navigation.Configuration) }) {
            Icon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "Configuración",
                modifier = Modifier.size(60.dp)
            )
        }
    }
}

@Composable
fun HorizontalLayout(navController: NavController) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        MenuButtons(navigateToGame, Modifier.weight(1f))
        menuItems.forEach { item ->
            MenuButton(item.text, Modifier.weight(1f)) {
                navController.navigate(item.destination)
            }
        }
    }
}

@Composable
fun VerticalLayout(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MenuButtons(navigateToGame, Modifier.fillMaxWidth(0.6f).scale(1.4f))
        menuItems.forEach { item ->
            MenuButton(text = item.text, Modifier.fillMaxWidth(0.6f).scale(1.4f)) {
               navController.navigate(item.destination)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MainMenuScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    ) {
        TopBar(navController, modifier = Modifier.align(Alignment.TopStart))

        val isHorizontal = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isHorizontal) {
            HorizontalLayout(navigateToGame)
        } else {
            VerticalLayout(navigateToGame)
            HorizontalLayout(navController)
        } else {
            VerticalLayout(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainMenuVertical() {
    CedicaTheme {
        NavigationWrapper()
    }
}

@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewMainMenuHorizontal() {
    CedicaTheme {
        NavigationWrapper()
    }
}
