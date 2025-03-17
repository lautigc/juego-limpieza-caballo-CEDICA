package com.cedica.cedica.ui.utils.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.core.guestData.GuestUser
import com.cedica.cedica.data.user.LoadingUser
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.Flow
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
        UserUiState(userRepository.getByID(it))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = UserUiState()
    )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun getUserSessionID(): Flow<Long> {
        return session.getUserID()
    }
}
