package com.cedica.cedica.data.repository.api

import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.UserPatient
import kotlinx.coroutines.flow.Flow

interface PatientRepository: BaseRepository<Patient> {

    fun getAllUserPatient(): Flow<List<UserPatient>>
}