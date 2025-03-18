package com.cedica.cedica.data.seed

import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import java.util.Date

val users_seed = listOf(
    User(id = 1, role = Role.USER, firstName = "Lautaro", lastName = "Paredes", username = "lautaro_p"),
    User(id = 2, role = Role.USER, firstName = "Juan Román", lastName = "Riquelme", username = "roman10"),
    User(id = 3, role = Role.USER, firstName = "Martín", lastName = "Palermo", username = "martin9"),
    User(id = 4, role = Role.USER, firstName = "Carlos", lastName = "Bianchi", username = "carlos_b"),
    User(id = 5, role = Role.USER, firstName = "Leandro", lastName = "Campos", username = "leandro_c"),
    User(id = 6, role = Role.USER, firstName = "Adrian", lastName = "Robinson", username = "adrian_r"),
    User(id = 7, role = Role.USER, firstName = "Alex", lastName = "Jackson", username = "alex_j"),
    User(id = 8, role = Role.USER, firstName = "Amy", lastName = "Scott", username = "amy_s"),
    User(id = 9, role = Role.USER, firstName = "Andrew", lastName = "Wright", username = "andrew_w"),
    User(id = 10, role = Role.USER, firstName = "Anna", lastName = "Mitchell", username = "anna_m"),
    User(id = 11, role = Role.USER, firstName = "Anna", lastName = "Parker", username = "anna_p"),
    User(id = 12, role = Role.USER, firstName = "Brandon", lastName = "Johnson", username = "brandon_j"),
    User(id = 13, role = Role.USER, firstName = "Chuck", lastName = "Taylor", username = "chuck_t"),
    User(id = 14, role = Role.USER, firstName = "Elisabeth", lastName = "Robinson", username = "elisabeth_r"),
    User(id = 15, role = Role.USER, firstName = "Eric", lastName = "Adams", username = "eric_a"),
    User(id = 16, role = Role.USER, firstName = "Erica", lastName = "Clark", username = "erica_c"),
    User(id = 17, role = Role.USER, firstName = "Jacob", lastName = "Smith", username = "jacob_s"),
    User(id = 18, role = Role.USER, firstName = "Juan", lastName = "Rodriguez", username = "juan_r"),
    User(id = 19, role = Role.USER, firstName = "Kim", lastName = "Anderson", username = "kim_a"),
    User(id = 20, role = Role.USER, firstName = "Lindsay", lastName = "Moore", username = "lindsay_m"),
    User(id = 21, role = Role.USER, firstName = "Lisa", lastName = "Moore", username = "lisa_m"),
    User(id = 22, role = Role.USER, firstName = "Maria", lastName = "Lopez", username = "maria_l"),
    User(id = 23, role = Role.USER, firstName = "Peter", lastName = "Nelson", username = "peter_n"),
    User(id = 24, role = Role.USER, firstName = "Rachel", lastName = "Thomas", username = "rachel_t"),
    User(id = 25, role = Role.USER, firstName = "Stuart", lastName = "Thomson", username = "stuart_t"),
    User(id = 26, role = Role.USER, firstName = "Victoria", lastName = "Lewis", username = "victoria_l"),
    User(id = 27, role = Role.USER, firstName = "Vincent", lastName = "Lee", username = "vincent_l"),
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