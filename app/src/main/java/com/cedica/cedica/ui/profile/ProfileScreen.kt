package com.cedica.cedica.ui.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.cedica.cedica.R
import com.cedica.cedica.data.seed.users_seed
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.theme.CedicaTheme

@Composable
fun ProfileScreen(users: List<User>, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        UserList(users = users)
        AddProfileButton(onClick = {},
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_large))

        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    CedicaTheme {
        ProfileScreen(modifier = Modifier, users = users_seed)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    CedicaTheme(darkTheme = true) {
        ProfileScreen(modifier = Modifier, users = users_seed)
    }
}

