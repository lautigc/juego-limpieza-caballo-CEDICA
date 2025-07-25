package com.cedica.cedica.data.repository

import com.cedica.cedica.core.guestData.GuestData
import com.cedica.cedica.data.DB
import com.cedica.cedica.data.repository.local.LocalPatientRepository
import com.cedica.cedica.data.repository.local.LocalPlaySessionRepository
import com.cedica.cedica.data.repository.local.LocalTherapistRepository
import com.cedica.cedica.data.repository.local.LocalUserRepository


object RepositoryProvider {
    val userRepository = LocalUserRepository(DB.userDao(), GuestData)
    val patientRepository = LocalPatientRepository(DB.patientDao())
    val therapistRepository = LocalTherapistRepository(DB.therapistDao())
    val playSessionRepository = LocalPlaySessionRepository(DB.playSessionDao())
}