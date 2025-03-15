package com.cedica.cedica.ui.profile.create

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Man
import androidx.compose.material.icons.outlined.Woman
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cedica.cedica.R
import com.cedica.cedica.core.utils.input_field.InputField
import com.cedica.cedica.core.utils.input_field.ValidationInputField
import com.cedica.cedica.core.utils.input_field.NameField
import com.cedica.cedica.data.user.Gender
import com.cedica.cedica.ui.AppViewModelProvider
import com.cedica.cedica.ui.theme.CedicaTheme
import com.cedica.cedica.ui.utils.composables.AlertNotification
import com.cedica.cedica.ui.utils.composables.SimpleAlertDialog
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun CreateTherapistForm(
    viewModel: CreateTherapistFormViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToCreateUser: () -> Unit = {},
) {
    CreateTherapistFormContent(
        firstName = viewModel.firstName,
        lastName = viewModel.lastName,
        dataError = viewModel.dataError,
        onClick = {
            viewModel.insertUser(redirectTo = onNavigateToCreateUser) }
    )
}

@Composable
private fun FormContainer(
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_extra_large))
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
    ) {
        content()
    }
}

@Composable
private fun CreateTherapistFormContent(
    firstName: ValidationInputField<String>,
    lastName: ValidationInputField<String>,
    dataError: AlertNotification,
    onClick: () -> Unit = {},
) {
    FormContainer {
        val spacerModifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large) * 1.5f)

        CreateUserFormContent(
            firstName = firstName,
            lastName = lastName,
            spacerModifier = spacerModifier,
            headerStyle = MaterialTheme.typography.displayMedium,
        )

        Spacer(modifier = spacerModifier)

        Button(
            onClick = onClick,
        ) {
            Text("Registras usuario")
        }

        dataError.alert.value?.let {
            SimpleAlertDialog(
                displayDismissButton = false,
                onConfirmation = { dataError.hiddenAlert() },
                dialogTitle = "Error",
                dialogText = it,
                icon = Icons.Outlined.ErrorOutline
            )
        }
    }
}

@Composable
fun CreatePatientForm(
    viewModel: CreatePatientFormViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToCreateUser: () -> Unit = {},
) {
    CreatePatientFormContent(
        firstName = viewModel.firstName,
        lastName = viewModel.lastName,
        observations = viewModel.observations,
        gender = viewModel.gender,
        date = viewModel.birthDate,
        alert = viewModel.alert,
        onCreate = { viewModel.insertUser(redirectTo = onNavigateToCreateUser) }
    )
}

@Composable
fun EditTherapistForm(
    userID: Long,
    viewModel: EditUserFormViewModel = viewModel(
        factory = AppViewModelProvider.FactoryWithArgs.editUserForm(userID = userID)
    ),
    onEditTherapistNavigateTo: () -> Unit = {},
) {
    CreateTherapistFormContent(
        firstName = viewModel.firstName,
        lastName = viewModel.lastName,
        dataError = viewModel.dataError,
        onClick = { viewModel.insertUser(redirectTo = onEditTherapistNavigateTo) }
    )
}

@Composable
fun EditPatientForm(
    userID: Long,
    viewModel: EditPatientFormViewModel = viewModel(
        factory = AppViewModelProvider.FactoryWithArgs.editPatientForm(userID = userID)
    ),
    onEditPatientNavigateTo: () -> Unit = {},
) {
    CreatePatientFormContent(
        firstName = viewModel.firstName,
        lastName = viewModel.lastName,
        observations = viewModel.observations,
        gender = viewModel.gender,
        date = viewModel.birthDate,
        alert = viewModel.alert,
        onCreate = { viewModel.insertUser(redirectTo = onEditPatientNavigateTo) }
    )
}

@Composable
fun CreatePatientFormContent(
    firstName: ValidationInputField<String>,
    lastName: ValidationInputField<String>,
    observations: InputField<String>,
    gender: InputField<Gender>,
    date: InputField<Date>,
    alert: AlertNotification,
    headerStyle: TextStyle = MaterialTheme.typography.displayMedium,
    onCreate: () -> Unit = {},
) {
    FormContainer {
        val spacerModifier = Modifier.height(dimensionResource(R.dimen.padding_extra_large) * 1.5f)

        CreateUserFormContent(
            firstName = firstName,
            lastName = lastName,
            headerStyle = headerStyle,
            spacerModifier = spacerModifier
        )

        Spacer(modifier = spacerModifier)

        GenderInputField(gender, headerStyle)

        Spacer(modifier = spacerModifier)

        FormInputField(
            title = "Observaciones",
            titleStyle = headerStyle,
            text = observations.input,
            onValueChange = observations::onChange,
            textFieldStyle = MaterialTheme.typography.bodyLarge,
        )

        Spacer(modifier = spacerModifier)

        DateInputField(date)

        alert.alert.value?.let {
            SimpleAlertDialog(
                displayDismissButton = false,
                onConfirmation = { alert.hiddenAlert() },
                dialogTitle = "Error",
                dialogText = it,
                icon = Icons.Outlined.ErrorOutline
            )
        }

        Spacer(modifier = spacerModifier)

        Button(
            onClick = onCreate,
        ) {
            Text("Registras usuario")
        }
    }
}

/**
 * Convert a Long to a Date
 */
fun Long.toDate(): Date {
    return Date(this)
}

/**
 * Convert a String to a Date, using the format "dd/MM/yyyy". The format of DateInput is UTC, then
 * conversion takes into account the timezone.
 */
fun String.toDate(): Date {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.parse(this) ?: throw IllegalArgumentException("Invalid date format: $this")
}

/**
 * Convert a Date to a formatted String, using the format "dd/MM/yyyy". The format of DateInput is UTC, then
 * conversion takes into account the timezone.
 */
fun Date.toFormattedString(): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(this)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    date: InputField<Date>,
    headerStyle: TextStyle = MaterialTheme.typography.displayMedium,
) {
    var display by rememberSaveable { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Input)

    Text(
        text = "Fecha de nacimiento",
        style = headerStyle,
    )

    TextField(
        value = date.input.toFormattedString(),
        onValueChange = { },
        textStyle = MaterialTheme.typography.bodyLarge,
        enabled = false,
        modifier = Modifier
            .width(300.dp) // Ancho fijo
            .wrapContentHeight() // Se ajusta verticalmente al contenido
            .clip(RoundedCornerShape(20.dp)) // Esquinas redondeadas
            .clickable {
                display = true
            }
    )

    display.takeIf { it }?.let {
        DatePickerDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    date.onChange(datePickerState.selectedDateMillis?.toDate() ?: Date(LocalDate.now().toEpochDay()))
                    display = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = null,
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun CreateUserFormContent(
    firstName: ValidationInputField<String>,
    lastName: ValidationInputField<String>,
    headerStyle: TextStyle = MaterialTheme.typography.displayMedium,
    spacerModifier: Modifier,
) {
    Column {
        FormInputField(
            title = "Nombre",
            titleStyle = headerStyle,
            text = firstName.input,
            hasError = firstName.hasError,
            supportText = firstName.errorText,
            onValueChange = firstName::onChange,
            textFieldStyle = MaterialTheme.typography.headlineLarge,
        )

        Spacer(modifier = spacerModifier)

        FormInputField(
            title = "Apellido",
            titleStyle = headerStyle,
            text = lastName.input,
            hasError = lastName.hasError,
            supportText = lastName.errorText,
            onValueChange = lastName::onChange,
            textFieldStyle = MaterialTheme.typography.headlineLarge,
        )
    }
}


@Composable
private fun GenderInputField(
    gender: InputField<Gender>,
    headerStyle: TextStyle = MaterialTheme.typography.displayMedium,
) {
    Column {
        Text(
            text = "Género",
            style = headerStyle,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                bottom = dimensionResource(R.dimen.padding_medium)
            )
        )
        GenderSelector(gender)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderSelector(
    gender: InputField<Gender>,
    modifier: Modifier = Modifier
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val options = listOf(
        Triple(stringResource(R.string.man_voice_option_title), Icons.Outlined.Man, Gender.MALE),
        Triple(stringResource(R.string.woman_voice_option_title), Icons.Outlined.Woman, Gender.FEMALE),
    )

    selectedIndex = options.indexOfFirst { it.third == gender.input }

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, pair ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = {
                    selectedIndex = index
                    gender.onChange(options[index].third)
            },
                selected = index == selectedIndex,
                label =
                {
                    Row {
                        Icon(imageVector = pair.second, contentDescription = null)
                        Text(text = pair.first, style = MaterialTheme.typography.labelLarge)
                    }
                }
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FormInputField(
    title: String = "",
    titleStyle: TextStyle = MaterialTheme.typography.displayMedium,
    text: String = "",
    hasError: Boolean = false,
    supportText: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    textFieldStyle: TextStyle = MaterialTheme.typography.headlineLarge,
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = titleStyle,
            modifier = Modifier.padding(
                bottom = dimensionResource(R.dimen.padding_medium)
            )
        )

        TextField(
            value = text,
            onValueChange = { input -> onValueChange(input) },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
            ),
            isError = hasError,
            supportingText = { if (hasError) Text(text = supportText) },
            textStyle = textFieldStyle,
            shape = RoundedCornerShape(20.dp), // Aplica esquinas redondeadas correctamente
            modifier = Modifier
                .width(300.dp) // Ancho fijo
                .wrapContentHeight() // Se ajusta verticalmente al contenido
                .clip(RoundedCornerShape(20.dp)), // Esquinas redondeadas
        )
    }
}

@Composable
fun SelectGender() {
    var selectedImage by remember { mutableStateOf<Int?>(null) } // Guarda la imagen seleccionada

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally), // Agrega espacio entre los elemento,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val images = listOf(
            Pair(R.drawable.man_gender_option, "Masculino"),
            Pair(R.drawable.woman_gender_option, "Femenino"),
        )
        images.forEachIndexed { index, pair ->

            // Animaciones para el efecto de selección
            val scale by animateFloatAsState(if (selectedImage == index) 1.2f else 1f, label = "scale") // Zoom al seleccionar
            val elevation by animateFloatAsState(if (selectedImage == index) 12f else 2f, label = "elevation") // Sombra elevada

            Row(
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        shadowElevation = elevation // Aplica sombra
                    )
                    .clip(RoundedCornerShape(12.dp)) // Bordes redondeados
                    .background(if (selectedImage == index) Color.LightGray else Color.Transparent) // Fondo resaltado
                    .clickable { selectedImage = index } // Cambia el estado al hacer clic
                    .border(
                        2.dp,
                        if (selectedImage == index) Color.Blue else Color.Transparent,
                        RoundedCornerShape(12.dp)
                    ), // Borde azul si está seleccionada
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = pair.first),
                        contentDescription = "Imagen $index",
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(470.dp)
                            .width(160.dp)
                    )
                    Text(
                        text = pair.second,
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                }

            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun SelectGenderPreview() {
    CedicaTheme {
        SelectGender()
    }
}

@Composable
@Preview(showBackground = true)
fun CreateUserFormPreview() {
    CedicaTheme {
        CreateUserFormContent(
            firstName = NameField("..."),
            lastName = NameField("..."),
            spacerModifier = Modifier.height(8.dp),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CreateTherapistFormPreview() {
    CedicaTheme {
        CreateTherapistFormContent(
            firstName = NameField("..."),
            lastName = NameField("..."),
            dataError = AlertNotification(),
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CreateTherapistFormDataErrorPreview() {
    CedicaTheme {
        val alert = AlertNotification()
        alert.displayAlert("Error en el formulario de creación de paciente")
        CreateTherapistFormContent(
            firstName = NameField("..."),
            lastName = NameField("..."),
            dataError = alert,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CreatePatientFormPreview() {
    CedicaTheme {
        CreatePatientFormContent(
            firstName = NameField("..."),
            lastName = NameField("..."),
            observations = NameField("..."),
            gender = InputField(Gender.FEMALE),
            date = InputField(Date()),
            alert = AlertNotification(),
        )
    }
}

