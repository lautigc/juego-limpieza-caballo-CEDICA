package com.cedica.cedica.ui.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cedica.cedica.R
import com.cedica.cedica.core.navigation.About
import com.cedica.cedica.core.navigation.Game
import com.cedica.cedica.core.navigation.UserListScreen
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme

data class MenuItem(val text: String, val destination: Any)

val menuItems = listOf(
    MenuItem("Jugar", Game),
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
fun TopBar(navController: NavController, modifier: Modifier = Modifier, firstNameUser: String) {
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
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            IconButton(onClick = { navController.navigate(UserListScreen) }) {
                Icon(
                    imageVector = Icons.Rounded.AccountCircle,
                    contentDescription = "Perfil",
                    modifier = Modifier.size(80.dp)
                )
            }
            Text(
                text = firstNameUser,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(60.dp),
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
        menuItems.forEach { item ->
            MenuButton(text = item.text, Modifier.fillMaxWidth(0.6f).scale(1.4f)) {
                navController.navigate(item.destination)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MainMenuScreen(
    navController: NavController,
    viewModel: MainMenuViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MainMenu(navController, firstNameUser = uiState.user.firstName)
}

@Composable
private fun MainMenu(
    navController: NavController,
    firstNameUser: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    ) {
        TopBar(
            navController,
            modifier = Modifier.align(Alignment.TopStart),
            firstNameUser = firstNameUser
        )

        val isHorizontal =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isHorizontal) {
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
        MainMenu(navController = rememberNavController(), firstNameUser = "Invitado")
    }
}

@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewMainMenuHorizontal() {
    CedicaTheme {
        MainMenu(navController = rememberNavController(), firstNameUser = "Invitado")
    }
}
