package com.cedica.cedica.ui.home

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cedica.cedica.R
import com.cedica.cedica.core.guestData.isGuestUser
import com.cedica.cedica.core.navigation.About
import com.cedica.cedica.core.navigation.Game
import com.cedica.cedica.core.navigation.Menu
import com.cedica.cedica.core.navigation.Stats
import com.cedica.cedica.core.navigation.UserListScreen
import com.cedica.cedica.data.permissions.HasPermission
import com.cedica.cedica.data.permissions.Permission
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.view_models.UserUiState
import com.cedica.cedica.ui.utils.view_models.UserViewModel

data class MenuItem(val text: String, val destination: Any, val permission: Permission? = null)

val menuItems = listOf(
    MenuItem("Jugar", Game, Permission.GAME_PLAY),
    MenuItem("Progreso", Stats, Permission.STATS_READ),
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
            IconButton(onClick = { navController.navigate(UserListScreen(alertNotification = null)) }) {
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
        HasPermission(Permission.CONFIG_CREATE) {
            IconButton(onClick = { navController.navigate(com.cedica.cedica.core.navigation.Configuration) }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = "Configuración",
                    modifier = Modifier.size(60.dp)
                )
            }
        }
    }
}

@Composable
fun HorizontalLayout(navController: NavController, onGuestLogin: () -> Unit = {}, uiState: UserUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        MenuItems(
            navController = navController,
            buttonModifier = Modifier.weight(1f),
            onGuestLogin = onGuestLogin,
            uiState = uiState
        )
    }
}

@Composable
private fun MenuItems(
    navController: NavController,
    uiState: UserUiState,
    buttonModifier: Modifier,
    onGuestLogin: () -> Unit = {},
    spaceBetweenButtons: Dp? = null,
) {
    menuItems.forEach { item ->
        val button: @Composable () -> Unit = {
            MenuButton(item.text, modifier = buttonModifier) {
                navController.navigate(item.destination)
            }
        }
        item.permission?.let { p ->
            HasPermission(p) {
                button.invoke()
                Log.d("roleperm:", "uiState:${uiState.user.id}")
            }
        } ?: button.invoke()
        spaceBetweenButtons?.let { Spacer(modifier = Modifier.size(it)) }
    }

    if (!isGuestUser(uiState.user.id)) {
        MenuButton("Ingresar como invitado", modifier = buttonModifier) {
            onGuestLogin()
            navController.navigate(Menu)
        }
    }
    spaceBetweenButtons?.let { Spacer(modifier = Modifier.size(it)) }
}

@Composable
fun VerticalLayout(navController: NavController, onGuestLogin: () -> Unit = {}, uiState: UserUiState) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ) {
        MenuItems(
            navController = navController,
            buttonModifier = Modifier.fillMaxWidth(0.6f).scale(1.4f),
            spaceBetweenButtons = 32.dp,
            onGuestLogin = onGuestLogin,
            uiState = uiState,
        )
    }
}

@Composable
fun MainMenuScreen(
    navController: NavController,
    viewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    MainMenu(navController, uiState = uiState, onGuestLogin = viewModel::guestLogin)
}

@Composable
private fun MainMenu(
    navController: NavController,
    onGuestLogin: () -> Unit = {},
    uiState: UserUiState = UserUiState(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFADD8E6))
    ) {
        TopBar(
            navController,
            modifier = Modifier.align(Alignment.TopStart),
            firstNameUser = uiState.user.firstName
        )

        val isHorizontal =
            LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE
        if (isHorizontal) {
            HorizontalLayout(navController, onGuestLogin = onGuestLogin, uiState)
        } else {
            VerticalLayout(navController, onGuestLogin = onGuestLogin, uiState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainMenuVertical() {
    CedicaTheme {
        MainMenu(navController = rememberNavController())
    }
}

@Preview(showBackground = true, widthDp = 720, heightDp = 360)
@Composable
fun PreviewMainMenuHorizontal() {
    CedicaTheme {
        MainMenu(navController = rememberNavController())
    }
}
