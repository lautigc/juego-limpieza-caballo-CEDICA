package com.cedica.cedica.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class UserUiState(
    val user: User,
)

class UserViewModel(
    private val userID: Flow<Int>,
    private val db : DB
): ViewModel() {

    val uiState = userID.map {
        if (it == GuestUser.id) UserUiState(GuestUser) else UserUiState(db.userDao().getByID(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = UserUiState(GuestUser)
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}