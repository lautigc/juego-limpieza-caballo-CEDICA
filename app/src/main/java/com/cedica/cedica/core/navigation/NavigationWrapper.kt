package com.cedica.cedica.core.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cedica.cedica.ui.home.AboutScreen
import com.cedica.cedica.ui.home.ConfigurationScreen
import com.cedica.cedica.ui.game.GameScreen
import com.cedica.cedica.ui.home.MainMenuScreen
import com.cedica.cedica.ui.home.StatisticsScreen
import com.cedica.cedica.ui.profile.screen.ProfileListScreen
import com.cedica.cedica.ui.profile.configuration.UserSettingScreen
import com.cedica.cedica.ui.profile.create.CreatePatientForm
import com.cedica.cedica.ui.profile.create.CreateTherapistForm
import com.cedica.cedica.ui.profile.create.EditPatientForm
import com.cedica.cedica.ui.profile.create.EditTherapistForm

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
                onNavigateEditTherapist = { userID -> navController.navigate(EditTherapistScreen(userID)) },
                onNavigateEditPatient = { userID -> navController.navigate(EditPatientScreen(userID)) }
            )
        }

        composable<UserSetting> {
            UserSettingScreen()
        }

        composable<Stats> {
            StatisticsScreen()
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

        composable<EditTherapistScreen> { backStackEntry ->
            val args: EditTherapistScreen = backStackEntry.toRoute()
            EditTherapistForm(
                userID = args.userID,
                onEditTherapistNavigateTo = {
                    navController
                        .navigate(UserListScreen(alertNotification = "Usuario editado correctamente"))
                }
            )
        }

        composable<EditPatientScreen> { backStackEntry ->
            val args: EditTherapistScreen = backStackEntry.toRoute()
            EditPatientForm(
                userID = args.userID,
                onEditPatientNavigateTo = {
                    navController
                        .navigate(UserListScreen(alertNotification = "Usuario editado correctamente"))
                }
            )
        }

    }
}