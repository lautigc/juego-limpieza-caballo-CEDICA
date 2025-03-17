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

abstract class UserFormViewModel(
    private val userRepository: UserRepository
): ViewModel() {
    open var firstName: ValidationInputField<String> = NameField("")

    open var lastName: ValidationInputField<String> = NameField("")

    open val dataError = AlertNotification()

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

    fun insertUser(redirectTo: () -> Unit = {}) {
        this.viewModelScope.launch {
            if (
                this@UserFormViewModel.validateForm() &&
                this@UserFormViewModel.validateData()
            ) {
                insertProccess()
                redirectTo()
            }
        }
    }

    abstract suspend fun insertProccess(): Long
}

open class CreateUserFormViewModel(
    private val userRepository: UserRepository
): UserFormViewModel(userRepository) {

    override suspend fun insertProccess(): Long {
        return this.userRepository.insert(
            User(
                firstName = this.firstName.input,
                lastName = this.lastName.input,
                role = Role.USER,
            )
        )
    }
}

open class EditUserFormViewModel(
    private val userRepository: UserRepository,
    private val userID: Long,
): UserFormViewModel(userRepository) {

    private lateinit var currentUser: User

    init {
        this.viewModelScope.launch {
            this@EditUserFormViewModel.loadUser()
        }
    }

    override suspend fun validateData(): Boolean {
        if (this.firstName.input != this.currentUser.firstName || this.lastName.input != this.currentUser.lastName) {
            return super.validateData()
        }
        return true
    }

    protected open suspend fun loadUser(): Long {
        currentUser = userRepository.getByID(userID)
        this.firstName.onChange(currentUser.firstName)
        this.lastName.onChange(currentUser.lastName)
        return currentUser.id
    }

    override suspend fun insertProccess(): Long {
        this.userRepository.update(
            User(
                id = userID,
                firstName = this.firstName.input,
                lastName = this.lastName.input,
                role = Role.USER,
            )
        )
        return this.userID
    }
}