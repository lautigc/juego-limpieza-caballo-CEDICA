package com.cedica.cedica.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ProfileListScreenUiState(
    val users: List<User>,
    val therapists: List<Therapist>,
    val patients: List<Patient>,
)

class ProfileListScreenViewModel: ViewModel() {

    val uiState: StateFlow<ProfileListScreenUiState> =
        combine(
            DB.userDao().getAllUsers(),
            DB.therapistDao().getAllTherapists(),
            DB.patientDao().getAllPatients(),
        ) { userFlow, therapistFlow, patientFlow ->
            ProfileListScreenUiState(
                users = userFlow,
                therapists = therapistFlow,
                patients = patientFlow,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileListScreenUiState(
                users = emptyList(),
                therapists = emptyList(),
                patients = emptyList(),
            )
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}