package com.cedica.cedica.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cedica.cedica.ui.home.AboutScreen
import com.cedica.cedica.ui.home.ConfigurationScreen
import com.cedica.cedica.ui.game.GameScreen
import com.cedica.cedica.ui.home.MainMenuScreen
import com.cedica.cedica.ui.home.StatisticsScreen
import com.cedica.cedica.ui.home.sampleGameSessions
import com.cedica.cedica.ui.profile.ProfileListScreen
import com.cedica.cedica.ui.profile.UserSettingScreen
import com.cedica.cedica.ui.profile.create.CreateTherapistForm

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

        composable<UserListScreen> {
            ProfileListScreen(
                onNavigateGuestLogin = { navController.navigate(Menu) },
                onNavigateUserSetting = { navController.navigate(UserSetting) },
                onNavigateUserLogin = { navController.navigate(Menu) },
                onNavigateCreateUser = { navController.navigate(CreateTherapistScreen) }
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
            CreateTherapistForm()
        }
    }
}