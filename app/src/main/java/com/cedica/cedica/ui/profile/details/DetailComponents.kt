package com.cedica.cedica.ui.profile.details

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cedica.cedica.R
import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.data.user.Patient
import com.cedica.cedica.data.user.User
import com.cedica.cedica.data.user.UserPatient
import com.cedica.cedica.data.user.UserTherapist
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.time.ZoneOffset
import java.util.*
import java.util.Locale

private fun toAge(birthDate: Date): Pair<Int, Int> {
    // Convertir Date (que está en UTC) a LocalDate en UTC
    val nacimiento = birthDate.toInstant().atZone(ZoneOffset.UTC).toLocalDate()
    val hoy = Instant.now().atZone(ZoneOffset.UTC).toLocalDate() // Fecha actual en UTC
    val periodo = Period.between(nacimiento, hoy)
    return Pair(periodo.years, periodo.months) // Retorna (años, meses)
}

@Composable
fun UserPatientData(
    user: UserPatient,
) {
    CardContainer() {
        UserData(user = user.user)
        PatientData(user = user.patient)
    }
}

@Composable
fun UserTherapistData(
    user: UserTherapist,
) {
    CardContainer() {
        UserData(user = user.user)
    }
}

@Composable
private fun CardContainer(content: @Composable ColumnScope.() -> Unit = {}) {
    Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
        content()
    }
}

@Composable
private fun PatientData(user: Patient) {
    val formattedDate = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    ).format(user.birthDate)

    DataText(title = "Nacimiento:\t", text = formattedDate.toString())
    DataText(
        title = "Edad:\t",
        text = "${toAge(user.birthDate).first} años ${toAge(user.birthDate).second} meses"
    )
    DataText(title = "Género:\t", text = user.gender.toString())
    DataText(title = "Observaciones:\t", text = user.observations)
}

@Composable
private fun UserData(user: User) {
    DataText(title = "Username: ", text = user.username)
    DataText(title = "Nombre: ", text = user.firstName)
    DataText(title = "Apellido: ", text = user.lastName)
}

@Composable
private fun DataText(title: String, text: String) {
    val dataText = buildAnnotatedString {
        // Aplicamos el estilo adecuado al título
        withStyle(style = MaterialTheme.typography.titleMedium.toSpanStyle()) {
            append(title)
        }
        // Agregamos el texto con un valor predeterminado si está vacío
        append(text.ifEmpty { "No se registró información" })
    }

    Text(
        text = dataText,
        style = MaterialTheme.typography.bodyLarge, // El estilo general para todo el texto
        modifier = Modifier.verticalScroll(rememberScrollState())
    )

    Spacer(modifier = Modifier.height(8.dp))
}

@Preview(showBackground = true)
@Composable
fun UserProfileScreenPreview() {
    val localDate = LocalDate.of(2003, 4, 11)
    val date = Date.from(localDate.atStartOfDay(ZoneOffset.UTC).toInstant())

    Column (Modifier.fillMaxWidth()) {
        UserPatientData(
            user = UserPatient(
                user = User(
                    id = 1,
                    role = Role.USER,
                    firstName = "Lautaro",
                    lastName = "Paredes",
                    username = "lautaro_p"
                ),
                patient = Patient(
                    userID = 1,
                    gender = Gender.MALE,
                    observations = "Sin observaciones".repeat(10),
                    birthDate = date
                )
            )
        )
    }
}