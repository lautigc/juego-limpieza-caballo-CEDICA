package com.cedica.cedica.ui.profile.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.cedica.cedica.core.guestData.GuestUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.profile.details.UserPatientData
import com.cedica.cedica.ui.profile.details.UserTherapistData
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.composables.SimpleAlertDialog

@Composable
fun ProfileListScreen(
    notification: String? = null,
    viewModel: ProfileListScreenViewModel = viewModel(
        factory = AppViewModelProvider.profileListScreenViewModelFactory(
            notification = notification
        )
    ),
    onNavigateGuestLogin: () -> Unit = {},
    onNavigateUserLogin: () -> Unit = {},
    onNavigateCreateTherapist: () -> Unit = {},
    onNavigateCreatePatient: () -> Unit = {},
    onNavigateEditTherapist: (userID: Long) -> Unit = {},
    onNavigateEditPatient: (userID: Long) -> Unit = {},
    onNavigateUserSetting: (userID: Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsState()
    val alertNotification = viewModel.alertNotification

    val onDetailTherapist: @Composable (id: Long) -> Unit = { id ->
        val user = profileUiState.therapists.find { it.user.id == id }
        user?.let {
            UserTherapistData(user = it)
        }
    }
    val onDetailPatient: @Composable (id: Long) -> Unit = { id ->
        val user = profileUiState.patients.find { it.user.id == id }
        user?.let {
            UserPatientData(user = it)
        }
    }

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
        onUserSetting = onNavigateUserSetting,
        onCreateTherapist = onNavigateCreateTherapist,
        onEditTherapist = onNavigateEditTherapist,
        onDetailTherapist = onDetailTherapist,
        onCreatePatient = onNavigateCreatePatient,
        onEditPatient = onNavigateEditPatient,
        onDetailPatient = onDetailPatient,
        modifier = modifier,
        currentUser = profileUiState.currentUser
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Screen(
    patients: List<User> = emptyList(),
    therapists: List<User> = emptyList(),
    onLogin: (User) -> Unit = {},
    onDeleteUser: (User) -> Unit,
    onUserSetting: (Long) -> Unit,
    onGuestLogin: () -> Unit = {},
    onCreateTherapist: () -> Unit = {},
    onEditTherapist: (Long) -> Unit = {},
    onDetailTherapist: @Composable (id: Long) -> Unit = {},
    onCreatePatient: () -> Unit = {},
    onEditPatient: (Long) -> Unit = {},
    onDetailPatient: @Composable (id: Long) -> Unit = {},
    currentUser: User = GuestUser,
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
        TabRowComponent(
            tabs = listOf("Alumnos", "Maestros"),
            contentScreens = listOf(
                {
                    UserList(
                        users = patients,
                        currentUser = currentUser,
                        onLogin = onLogin,
                        onDelete = onDeleteUser,
                        onEdit = onEditPatient,
                        onUserSetting = onUserSetting,
                        onDetail = onDetailPatient,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                {
                    UserList(
                        users = therapists,
                        currentUser = currentUser,
                        onLogin = onLogin,
                        onDelete = onDeleteUser,
                        onUserSetting = onUserSetting,
                        onDetail = onDetailTherapist,
                        onEdit = onEditTherapist,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            )
        )
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

