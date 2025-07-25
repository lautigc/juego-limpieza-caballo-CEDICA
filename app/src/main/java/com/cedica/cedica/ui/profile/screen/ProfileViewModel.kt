package com.cedica.cedica.ui.profile.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.PatientRepository
import com.cedica.cedica.data.repository.interfaces.TherapistRepository
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.core.guestData.GuestUser
import com.cedica.cedica.data.user.LoadingUser
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserPatient
import com.cedica.cedica.data.user.UserTherapist
import com.cedica.cedica.ui.utils.composables.AlertNotification
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
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
    notification: String? = null,
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
                currentUser = userRepository.getByID(userID),
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ProfileListScreenUiState()
        )

    val alertNotification = AlertNotification(notification)

    fun login(user: User, redirectTo: () -> Unit) {
        viewModelScope.launch {
            this@ProfileListScreenViewModel.session.setUserID(user.id)
            redirectTo()
        }
    }

    fun guestLogin() {
        viewModelScope.launch {
            this@ProfileListScreenViewModel.session.setUserID(GuestUser.id)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            if (this@ProfileListScreenViewModel.session.getUserID().first() == user.id) {
                this@ProfileListScreenViewModel.session.setUserID(GuestUser.id)
            }
            this@ProfileListScreenViewModel.userRepository.delete(user)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

}