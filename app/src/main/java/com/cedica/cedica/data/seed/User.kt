package com.cedica.cedica.data.seed

import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import java.util.Date

val users_seed = listOf(
    User(id = 1, firstName = "Lautaro", lastName = "Paredes", role = Role.USER),
    User(id = 2, firstName = "Juan Román", lastName = "Riquelme", role = Role.USER),
    User(id = 3, firstName = "Martín", lastName = "Palermo", role = Role.USER),
    User(id = 4, firstName = "Carlos", lastName = "Bianchi", role = Role.USER),
    User(id = 5, firstName = "Leandro", lastName = "Campos", role = Role.USER),
    User(id = 6, firstName = "Adrian", lastName = "Robinson", role = Role.USER),
    User(id = 7, firstName = "Alex", lastName = "Jackson", role = Role.USER),
    User(id = 8, firstName = "Amy", lastName = "Scott", role = Role.USER),
    User(id = 9, firstName = "Andrew", lastName = "Wright", role = Role.USER),
    User(id = 10, firstName = "Anna", lastName = "Mitchell", role = Role.USER),
    User(id = 11, firstName = "Anna", lastName = "Parker", role = Role.USER),
    User(id = 12, firstName = "Brandon", lastName = "Johnson", role = Role.USER),
    User(id = 13, firstName = "Chuck", lastName = "Taylor", role = Role.USER),
    User(id = 14, firstName = "Elisabeth", lastName = "Robinson", role = Role.USER),
    User(id = 15, firstName = "Eric", lastName = "Adams", role = Role.USER),
    User(id = 16, firstName = "Erica", lastName = "Clark", role = Role.USER),
    User(id = 17, firstName = "Jacob", lastName = "Smith", role = Role.USER),
    User(id = 18, firstName = "Juan", lastName = "Rodriguez", role = Role.USER),
    User(id = 19, firstName = "Kim", lastName = "Anderson", role = Role.USER),
    User(id = 20, firstName = "Lindsay", lastName = "Moore", role = Role.USER),
    User(id = 21, firstName = "Lisa", lastName = "Moore", role = Role.USER),
    User(id = 22, firstName = "Maria", lastName = "Lopez", role = Role.USER),
    User(id = 23, firstName = "Peter", lastName = "Nelson", role = Role.USER),
    User(id = 24, firstName = "Rachel", lastName = "Thomas", role = Role.USER),
    User(id = 25, firstName = "Stuart", lastName = "Thomson", role = Role.USER),
    User(id = 26, firstName = "Victoria", lastName = "Lewis", role = Role.USER),
    User(id = 27, firstName = "Vincent", lastName = "Lee", role = Role.USER)
)

val patients_seed = listOf(
    Patient(userID = 1, gender = Gender.MALE, observations = "Sin observaciones", birthDate = Date()),
    Patient(userID = 2, gender = Gender.MALE, observations = "Paciente con historial de lesiones", birthDate = Date()),
    Patient(userID = 3, gender = Gender.MALE, observations = "Requiere seguimiento mensual", birthDate = Date()),
    Patient(userID = 4, gender = Gender.MALE, observations = "Bajo tratamiento psicológico", birthDate = Date()),
    Patient(userID = 5, gender = Gender.MALE, observations = "Consulta por ansiedad", birthDate = Date()),
    Patient(userID = 6, gender = Gender.MALE, observations = "Paciente con trastorno del sueño", birthDate = Date()),
    Patient(userID = 7, gender = Gender.MALE, observations = "Historial de depresión", birthDate = Date()),
    Patient(userID = 8, gender = Gender.FEMALE, observations = "Evaluación inicial realizada", birthDate = Date()),
    Patient(userID = 9, gender = Gender.MALE, observations = "Problemas de estrés laboral", birthDate = Date()),
    Patient(userID = 10, gender = Gender.FEMALE, observations = "Seguimiento de terapia cognitiva", birthDate = Date()),
    Patient(userID = 11, gender = Gender.FEMALE, observations = "Diagnóstico de TDAH", birthDate = Date()),
    Patient(userID = 12, gender = Gender.MALE, observations = "Tratamiento por ansiedad social", birthDate = Date()),
    Patient(userID = 13, gender = Gender.MALE, observations = "Consulta por fobia social", birthDate = Date())
)

val therapists_seed = listOf(
    Therapist(userID = 14),
    Therapist(userID = 15),
    Therapist(userID = 16),
    Therapist(userID = 17),
    Therapist(userID = 18),
    Therapist(userID = 19),
    Therapist(userID = 20),
    Therapist(userID = 21),
    Therapist(userID = 22),
    Therapist(userID = 23),
    Therapist(userID = 24),
    Therapist(userID = 25),
    Therapist(userID = 26),
    Therapist(userID = 27)
)