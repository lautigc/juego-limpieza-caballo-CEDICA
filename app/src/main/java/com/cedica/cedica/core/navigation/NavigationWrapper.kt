package com.cedica.cedica.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.home.AboutScreen
import com.cedica.cedica.ui.home.ConfigurationScreen
import com.cedica.cedica.ui.game.GameScreen
import com.cedica.cedica.ui.home.MainMenuScreen
import com.cedica.cedica.ui.home.StatisticsScreen
import com.cedica.cedica.ui.home.sampleGameSessions
import com.cedica.cedica.ui.profile.ProfileListScreen
import com.cedica.cedica.ui.profile.UserSettingScreen
import com.cedica.cedica.ui.profile.create.CreatePatientForm
import com.cedica.cedica.ui.profile.create.CreateTherapistForm
import com.cedica.cedica.ui.utils.composables.AlertNotification

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Menu) {
        composable<Menu> {
            MainMenuScreen(
                navController = navController
            )
        }

        composable<Configuration> {
            ConfigurationScreen {
                navController.navigate(Menu)
            }
        }

        composable<Game> {
            GameScreen {
                navController.navigate(Menu)
            }
        }
        composable<About> {
            AboutScreen {
                navController.navigate(Menu)
            }
        }

        composable<UserListScreen> { backStackEntry ->
            val route: UserListScreen = backStackEntry.toRoute()
            ProfileListScreen(
                notification = route.alertNotification,
                onNavigateGuestLogin = { navController.navigate(Menu) },
                onNavigateUserSetting = { navController.navigate(UserSetting) },
                onNavigateUserLogin = { navController.navigate(Menu) },
                onNavigateCreateTherapist = { navController.navigate(CreateTherapistScreen) },
                onNavigateCreatePatient = { navController.navigate(CreatePatientScreen) },
            )
        }

        composable<UserSetting> {
            UserSettingScreen()
        }

        composable<Stats> {
            // TODO: quitar parametros cuando se tomen los datos
            StatisticsScreen(
                gameSessions = sampleGameSessions
            )
        }

        composable<CreateTherapistScreen> {
            CreateTherapistForm(
                onNavigateToCreateUser = { navController.navigate(route = UserListScreen(
                    alertNotification = "Usuario creado correctamente"
                )) }
            )
        }

        composable<CreatePatientScreen> {
            CreatePatientForm(
                onNavigateToCreateUser = { navController.navigate(route = UserListScreen(
                    alertNotification = "Usuario creado correctamente"
                )) }
            )
        }
    }
}