package com.cedica.cedica.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.user.GUEST_USER
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProfileListScreenUiState(
    val users: List<User> = emptyList(),
    val therapists: List<Therapist> = emptyList(),
    val patients: List<Patient> = emptyList(),
    val currentUser: User = GUEST_USER,
)

class ProfileListScreenViewModel(
    currentUserID: Flow<Int>,
    users: Flow<List<User>>,
    patients: Flow<List<Patient>>,
    therapists: Flow<List<Therapist>>,
): ViewModel() {

    val uiState: StateFlow<ProfileListScreenUiState> =
        combine(
            users,
            therapists,
            patients,
            currentUserID,
        ) { userFlow, therapistFlow, patientFlow, userID ->
            ProfileListScreenUiState(
                users = userFlow,
                therapists = therapistFlow,
                patients = patientFlow,
                currentUser = if (userID == 0) GUEST_USER else userFlow.first { it.id == userID },
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileListScreenUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}