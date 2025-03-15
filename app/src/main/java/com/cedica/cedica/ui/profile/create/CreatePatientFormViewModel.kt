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

    val gender = InputField(Gender.MALE)

    val observations = InputField("")

    val birthDate = InputField(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))

    val alert = AlertNotification()

    override suspend fun insertProccess(): Long {
        val id = super.insertProccess()
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

class EditPatientFormViewModel(
    userRepository: UserRepository,
    userID: Long,
    private val patientRepository: PatientRepository
): EditUserFormViewModel(userRepository, userID) {
    val gender = InputField(Gender.MALE)

    val observations = InputField("")

    val birthDate = InputField(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))

    val alert = AlertNotification()

    override suspend fun loadUser(): Long {
        val userID = super.loadUser()
        val patient = this.patientRepository.getByID(userID)
        this.gender.onChange(patient.gender)
        this.observations.onChange(patient.observations)
        this.birthDate.onChange(patient.birthDate)
        return patient.userID
    }

    override suspend fun insertProccess(): Long {
        val id = super.insertProccess()
        this.patientRepository.update(
            Patient(
                userID = id,
                gender = gender.input,
                observations = observations.input,
                birthDate = birthDate.input,
            )
        )
        return id
    }
}