package com.cedica.cedica.ui.profile

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.composables.AlertNotification
import com.cedica.cedica.ui.utils.composables.SimpleAlertDialog

@Composable
fun ProfileListScreen(
    viewModel: ProfileListScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateGuestLogin: () -> Unit = {},
    onNavigateUserSetting: () -> Unit = {},
    onNavigateUserLogin: () -> Unit = {},
    onNavigateCreateTherapist: () -> Unit = {},
    onNavigateCreatePatient: () -> Unit = {},
    alertNotification: String? = null,
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsState()

    Screen(
        patients = profileUiState.patients.map { it.user },
        therapists = profileUiState.therapists.map { it.user },
        onLogin = { user: User ->
            viewModel.login(user)
            onNavigateUserLogin()
        },
        onGuestLogin = {
            viewModel.guestLogin()
            onNavigateGuestLogin()
        },
        onDeleteUser = { user: User ->
            viewModel.deleteUser(user)
        },
        onCreateTherapist = onNavigateCreateTherapist,
        onCreatePatient = onNavigateCreatePatient,
        onUserSetting = onNavigateUserSetting,
        alertNotification = AlertNotification(alertNotification),
        modifier = modifier,
        currentUser = profileUiState.currentUser
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Screen(
    patients: List<User> = emptyList(),
    therapists: List<User> = emptyList(),
    onLogin: (User) -> Unit = {},
    onDeleteUser: (User) -> Unit,
    onGuestLogin: () -> Unit = {},
    onCreateTherapist: () -> Unit = {},
    onCreatePatient: () -> Unit = {},
    onUserSetting: () -> Unit,
    currentUser: User = GuestUser,
    alertNotification: AlertNotification = AlertNotification(),
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            ExpandableFAB(
                onGuestLogin = onGuestLogin,
                onCreateTherapist = onCreateTherapist,
                onCreatePatient = onCreatePatient,
            )
        }
    ) { _ ->
        UserScreen(
            patients = patients,
            therapists = therapists,
            currentUser = currentUser,
            onLogin = onLogin,
            onDelete = onDeleteUser,
            onUserSetting = onUserSetting
        )

        alertNotification.alert.value?.let {
            SimpleAlertDialog(
                displayDismissButton = false,
                dialogTitle = "Notificación",
                dialogText = it,
                onConfirmation = { alertNotification.hiddenAlert() },
                icon = Icons.Default.Info
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CedicaTheme {
        Screen(
            patients = users_seed,
            onUserSetting = { },
            modifier = Modifier,
            currentUser = users_seed.first(),
            onDeleteUser = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyProfileScreenPreview() {
    CedicaTheme {
        Screen(
            patients = emptyList(),
            onUserSetting = { },
            modifier = Modifier,
            currentUser = users_seed.first(),
            onDeleteUser = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    CedicaTheme(darkTheme = true) {
        Screen(
            patients = users_seed,
            therapists = users_seed.slice(0..users_seed.size / 2),
            onUserSetting = { },
            modifier = Modifier,
            currentUser = users_seed.first(),
            onDeleteUser = {}
        )
    }
}

