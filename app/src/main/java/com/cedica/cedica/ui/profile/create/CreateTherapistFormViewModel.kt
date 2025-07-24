package com.cedica.cedica.ui.profile.create

import com.cedica.cedica.data.repository.interfaces.TherapistRepository
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.Therapist


class CreateTherapistFormViewModel(
    private val therapistRepository: TherapistRepository,
    userRepository: UserRepository
): CreateUserFormViewModel(userRepository) {

    override suspend fun insertProccess(): Long {
        val id = super.insertProccess()
        return this.therapistRepository.insert(
            Therapist(id)
        )
    }
}