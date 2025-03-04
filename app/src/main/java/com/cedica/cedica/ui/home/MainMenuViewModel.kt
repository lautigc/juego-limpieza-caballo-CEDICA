package com.cedica.cedica.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.repository.api.UserRepository
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class MainMenuUiState(
    val user: User,
)

class MainMenuViewModel(
    private val userID: Flow<Long>,
    private val userRepository: UserRepository,

    ): ViewModel() {

    val uiState = userID.map {
        if (it == GuestUser.id) MainMenuUiState(GuestUser) else MainMenuUiState(userRepository.getByID(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = MainMenuUiState(GuestUser)
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}