package com.cedica.cedica.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.PatientRepository
import com.cedica.cedica.data.repository.interfaces.TherapistRepository
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.GuestUser
import com.cedica.cedica.data.user.LoadingUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserPatient
import com.cedica.cedica.data.user.UserTherapist
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileListScreenUiState(
    val users: List<User> = emptyList(),
    val therapists: List<UserTherapist> = emptyList(),
    val patients: List<UserPatient> = emptyList(),
    val currentUser: User = LoadingUser,
)

class ProfileListScreenViewModel(
    private val session: Session,
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository,
    private val therapistRepository: TherapistRepository,
): ViewModel() {

    val uiState: StateFlow<ProfileListScreenUiState> =
        combine(
            this.userRepository.getAll(),
            this.therapistRepository.getAllUserTherapist(),
            this.patientRepository.getAllUserPatient(),
            this.session.getUserID(),
        ) { userFlow, therapistFlow, patientFlow, userID ->
            ProfileListScreenUiState(
                users = userFlow,
                therapists = therapistFlow,
                patients = patientFlow,
                currentUser = if (userID == GuestUser.id) GuestUser else userFlow.first { it.id == userID },
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

    fun guestLogin() {
        viewModelScope.launch {
            this@ProfileListScreenViewModel.session.setUserID(GuestUser.id)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}