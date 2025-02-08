package com.cedica.cedica.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class MainMenuUiState(
    val user: User,
)

class MainMenuViewModel(userID: Int?, db : DB): ViewModel() {

    private var _uiState = MutableStateFlow(MainMenuUiState(
        User(0, Role.GUEST, "Invitado", "")
    ))

    init {
        viewModelScope.launch {
            userID?.let {
                _uiState.value = MainMenuUiState(db.userDao().getByID(it))
            }
        }
    }

    val uiState: StateFlow<MainMenuUiState> = _uiState.asStateFlow()
}