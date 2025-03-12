package com.cedica.cedica.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.PatientRepository
import com.cedica.cedica.data.user.PlaySession
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList

data class PatientUiState(val playSessions: List<PlaySession> = emptyList())

class PatientViewModel(private val session: Session, private val patientRepository: PatientRepository) : ViewModel() {
    val playSessions: StateFlow<PatientUiState> =
        session.getUserID()
            .map { id -> patientRepository.getAllPlaySessions(id) }
            .map { session -> PatientUiState(session.first()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PatientUiState()
            )

}