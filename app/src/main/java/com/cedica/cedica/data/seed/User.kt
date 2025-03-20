package com.cedica.cedica.data.seed

import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.Therapist
import com.cedica.cedica.data.user.User
import java.util.Calendar
import java.util.Date

val users_seed = listOf(
    User(id = 1, role = Role.USER, firstName = "Lautaro", lastName = "Paredes", username = "lautaro_p"),
    User(id = 2, role = Role.USER, firstName = "Juan Román", lastName = "Riquelme", username = "roman10"),
    User(id = 3, role = Role.USER, firstName = "Martín", lastName = "Palermo", username = "martin9"),
    User(id = 4, role = Role.USER, firstName = "Carlos", lastName = "Bianchi", username = "carlos_b"),
    User(id = 5, role = Role.USER, firstName = "Leandro", lastName = "Campos", username = "leandro_c"),
    User(id = 6, role = Role.ADMIN, firstName = "Elisabeth", lastName = "Robinson", username = "elisabeth_r"),
    User(id = 7, role = Role.ADMIN, firstName = "Eric", lastName = "Adams", username = "eric_a"),
    User(id = 8, role = Role.ADMIN, firstName = "Erica", lastName = "Clark", username = "erica_c"),
    User(id = 9, role = Role.ADMIN, firstName = "Jacob", lastName = "Smith", username = "jacob_s"),
    User(id = 10, role = Role.ADMIN, firstName = "Juan", lastName = "Rodriguez", username = "juan_r")
)

fun getDate(year: Int, month: Int, day: Int): Date {
    val calendar = Calendar.getInstance()
    calendar.set(year, month - 1, day)
    return calendar.time
}

val patients_seed = listOf(
    Patient(userID = 1, gender = Gender.MALE, observations = "Sin observaciones", birthDate = getDate(1985, 5, 20)),
    Patient(userID = 2, gender = Gender.MALE, observations = "Paciente con historial de lesiones", birthDate = getDate(1990, 8, 15)),
    Patient(userID = 3, gender = Gender.MALE, observations = "Requiere seguimiento mensual", birthDate = getDate(1978, 11, 3)),
    Patient(userID = 4, gender = Gender.MALE, observations = "Bajo tratamiento psicológico", birthDate = getDate(1982, 2, 28)),
    Patient(userID = 5, gender = Gender.MALE, observations = "Consulta por ansiedad", birthDate = getDate(1995, 7, 10))
)

val therapists_seed = listOf(
    Therapist(userID = 6),
    Therapist(userID = 7),
    Therapist(userID = 8),
    Therapist(userID = 9),
    Therapist(userID = 10)
)