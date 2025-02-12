package com.cedica.cedica.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cedica.cedica.AboutScreen
import com.cedica.cedica.ConfigurationScreen
import com.cedica.cedica.GameScreen
import com.cedica.cedica.ui.home.MainMenuScreen
import com.cedica.cedica.ui.profile.ProfileListScreen
import com.cedica.cedica.ui.profile.UserSettingScreen

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
                onNavigateUserLogin = { navController.navigate(Menu) }
            )
        }

        composable<UserSetting> {
            UserSettingScreen()
        }
    }
}