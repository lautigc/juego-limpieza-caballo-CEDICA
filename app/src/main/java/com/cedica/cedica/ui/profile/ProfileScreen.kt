package com.cedica.cedica.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.R
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme

@Composable
fun ProfileListScreen(
    viewModel: ProfileListScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val profileUiState by viewModel.uiState.collectAsState()

    Screen(
        users = profileUiState.users,
        currentUser = profileUiState.currentUser,
        modifier = modifier,
        onLogin = { user: User -> viewModel.login(user) }
    )
}

@Composable
private fun Screen(
    users: List<User>,
    currentUser: User,
    onLogin: (User) -> Unit = {},
    modifier: Modifier,
) {
    Box(modifier = modifier) {
        UserList(users = users, currentUser, onLogin = onLogin, modifier = Modifier.fillMaxWidth())
        ExpandableFAB(
            items = listOf(
                FABItem(
                    icon = Icons.Filled.Add,
                    text = "Registrar",
                    onClick = { onClick() }
                ),
                FABItem(
                    icon = Icons.AutoMirrored.Filled.Login,
                    text = "Invitado",
                    onClick = { onClick() }
                )
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

fun onClick() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CedicaTheme {
        Screen(users = users_seed, currentUser = users_seed.first(), modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyProfileScreenPreview() {
    CedicaTheme {
        Screen(users = emptyList(), currentUser = users_seed.first(), modifier = Modifier)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    CedicaTheme(darkTheme = true) {
        Screen(users = users_seed, currentUser = users_seed.first(), modifier = Modifier)
    }
}

