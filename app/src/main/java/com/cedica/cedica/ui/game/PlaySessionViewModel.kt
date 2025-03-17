package com.cedica.cedica.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.repository.interfaces.PlaySessionRepository
import com.cedica.cedica.data.user.PlaySession
import kotlinx.coroutines.launch

class PlaySessionViewModel(private val playSessionRepository: PlaySessionRepository) : ViewModel() {

    fun insert(playSession: PlaySession) {
        viewModelScope.launch {
            playSessionRepository.insert(playSession)
        }
    }

}
