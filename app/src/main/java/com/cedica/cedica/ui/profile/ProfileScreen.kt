package com.cedica.cedica.ui.profile

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme

@Composable
fun ProfileListScreen(
    viewModel: ProfileListScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateGuestLogin: () -> Unit = {},
    onNavigateUserSetting: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsState()

    Screen(
        users = profileUiState.users,
        currentUser = profileUiState.currentUser,
        modifier = modifier,
        onLogin = { user: User -> viewModel.login(user) },
        onGuestLogin = {
            viewModel.guestLogin()
            onNavigateGuestLogin()
        },
        onUserSetting = onNavigateUserSetting
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
private fun Screen(
    users: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit = {},
    onGuestLogin: () -> Unit = {},
    onUserSetting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        floatingActionButton = {
            ExpandableFAB(onGuestLogin = onGuestLogin)
        }
    ) { _ ->
        UserList(
            users = users,
            currentUser = currentUser,
            onLogin = onLogin,
            onUserSetting = onUserSetting,
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CedicaTheme {
        Screen(
            users = users_seed,
            currentUser = users_seed.first(),
            onUserSetting = { },
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyProfileScreenPreview() {
    CedicaTheme {
        Screen(
            users = emptyList(),
            currentUser = users_seed.first(),
            onUserSetting = { },
            modifier = Modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    CedicaTheme(darkTheme = true) {
        Screen(
            users = users_seed,
            currentUser = users_seed.first(),
            onUserSetting = { },
            modifier = Modifier
        )
    }
}

