package com.cedica.cedica.ui.profile.create

import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.data.repository.interfaces.PatientRepository
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.ui.utils.composables.AlertNotification
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class CreatePatientFormViewModel(
    userRepository: UserRepository,
    private val patientRepository: PatientRepository
): CreateUserFormViewModel(userRepository) {
    val gender = InputField(Gender.FEMALE)

    val observations = InputField("")

    val birthDate = InputField(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))

    val alert = AlertNotification()

    override suspend fun userCreationProcess(): Long {
        val id = super.userCreationProcess()
        return this.patientRepository.insert(
            Patient(
                userID = id,
                gender = gender.input,
                observations = observations.input,
                birthDate = birthDate.input,
            )
        )
    }



}