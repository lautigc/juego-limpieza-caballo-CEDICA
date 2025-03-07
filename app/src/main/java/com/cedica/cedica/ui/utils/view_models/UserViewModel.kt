package com.cedica.cedica.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.LoadingUser
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class UserUiState(
    val user: User = LoadingUser,
)

class UserViewModel(
    private val session: Session,
    private val userRepository: UserRepository,
    ): ViewModel() {

    val uiState = session.getUserID().map {
        if (it == GuestUser.id) UserUiState(GuestUser) else UserUiState(userRepository.getByID(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = UserUiState()
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}