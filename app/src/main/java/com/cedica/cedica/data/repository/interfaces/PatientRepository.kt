package com.cedica.cedica.data.repository.interfaces

import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.PlaySession
import com.cedica.cedica.data.user.UserPatient
import kotlinx.coroutines.flow.Flow

interface PatientRepository: BaseRepository<Patient> {

    fun getAllUserPatient(): Flow<List<UserPatient>>

    fun getAllPlaySessions(userID: Long): Flow<List<PlaySession>>
}