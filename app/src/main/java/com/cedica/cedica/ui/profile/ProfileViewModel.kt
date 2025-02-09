package com.cedica.cedica.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileListScreenUiState(
    val users: List<User> = emptyList(),
    val therapists: List<Therapist> = emptyList(),
    val patients: List<Patient> = emptyList(),
    val currentUser: User = GuestUser,
)

class ProfileListScreenViewModel(
    private val session: Session,
    private val users: Flow<List<User>>,
    private val patients: Flow<List<Patient>>,
    private val therapists: Flow<List<Therapist>>,
): ViewModel() {

    val uiState: StateFlow<ProfileListScreenUiState> =
        combine(
            this.users,
            this.therapists,
            this.patients,
            this.session.getUserID(),
        ) { userFlow, therapistFlow, patientFlow, userID ->
            ProfileListScreenUiState(
                users = userFlow,
                therapists = therapistFlow,
                patients = patientFlow,
                currentUser = if (userID == 0) GuestUser else userFlow.first { it.id == userID },
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileListScreenUiState()
        )

    fun login(user: User) {
        viewModelScope.launch {
            this@ProfileListScreenViewModel.session.setUserID(user.id)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}