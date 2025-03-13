package com.cedica.cedica.ui.profile.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cedica.cedica.core.utils.input_field.ValidationInputField
import com.cedica.cedica.core.utils.input_field.NameField
import com.cedica.cedica.data.permissions.Role
import com.cedica.cedica.data.repository.interfaces.UserRepository
import com.cedica.cedica.data.user.User
import com.cedica.cedica.ui.utils.composables.AlertNotification
import kotlinx.coroutines.launch

open class CreateUserFormViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    var firstName: ValidationInputField<String> = NameField("")

    var lastName: ValidationInputField<String> = NameField("")

    val dataError = AlertNotification()

    protected open suspend fun validateForm(): Boolean {
        return firstName.inputIsValid() && lastName.inputIsValid()
    }

    protected open suspend fun validateData(): Boolean {
        if (this.userRepository.existsByFullName(this.firstName.input, this.lastName.input)) {
            this.dataError.displayAlert("El usuario ${this.firstName.input} ${this.lastName.input} ya existe")
            return false
        }
        return true
    }

    fun createUser(redirectTo: () -> Unit = {}) {
        this.viewModelScope.launch {
            if (
                this@CreateUserFormViewModel.validateForm() &&
                this@CreateUserFormViewModel.validateData()
            ) {
                userCreationProcess()
                redirectTo()
            }
        }
    }

    protected open suspend fun userCreationProcess(): Long {
        return this.userRepository.insert(
            User(
                firstName = this.firstName.input,
                lastName = this.lastName.input,
                role = Role.USER,
            )
        )
    }
}