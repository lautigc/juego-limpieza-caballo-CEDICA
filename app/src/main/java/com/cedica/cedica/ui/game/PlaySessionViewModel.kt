package com.cedica.cedica.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.data.user.PlaySessionDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PlaySessionViewModel(private val playSessionDao: PlaySessionDao) : ViewModel() {

    val allPlaySessions: Flow<List<PlaySession>> = playSessionDao.getAllPlaySessions()

    fun insert(playSession: PlaySession) {
        viewModelScope.launch {
            playSessionDao.insert(playSession)
        }
    }

    fun update(playSession: PlaySession) {
        viewModelScope.launch {
            playSessionDao.update(playSession)
        }
    }

    fun delete(playSession: PlaySession) {
        viewModelScope.launch {
            playSessionDao.delete(playSession)
        }
    }

    suspend fun getByID(id: Int): PlaySession? {
        return playSessionDao.getByID(id)
    }
}
