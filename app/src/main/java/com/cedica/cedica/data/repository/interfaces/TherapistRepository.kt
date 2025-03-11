package com.cedica.cedica.data.repository.interfaces

import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.UserTherapist
import kotlinx.coroutines.flow.Flow

interface TherapistRepository: BaseRepository<Therapist> {

    fun getAllUserTherapist(): Flow<List<UserTherapist>>
}