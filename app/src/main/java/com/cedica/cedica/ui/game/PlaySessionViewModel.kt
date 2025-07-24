package com.cedica.cedica.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.guestData.isGuestUser
import com.cedica.cedica.core.session.Session
import com.cedica.cedica.data.repository.interfaces.PlaySessionRepository
import com.cedica.cedica.data.user.PlaySession
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class PlaySessionViewModel(
    private val playSessionRepository: PlaySessionRepository,
    private val session: Session
) : ViewModel() {

    fun insert(playSession: PlaySession) {
        viewModelScope.launch {
            if (!isGuestUser(session.getUserID().first()))
                playSessionRepository.insert(playSession)
        }
    }

}
