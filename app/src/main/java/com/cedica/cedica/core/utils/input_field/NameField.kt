package com.cedica.cedica.core.utils.input_field


private fun String.isOnlyLetters(): Boolean {
    return this.all { it.isLetter() }
}

/**
 * The NameField class is a class that can be used to manage the state of a name input field.
 * @constructor
 * @param initialValue initial value of the input field
 */
class NameField(initialValue: String): InputField<String>(initialValue) {
    override fun inputIsValid(): Boolean {
        if (input.isEmpty()) {
            this.errorText = "El Campo no puede estar vacío"
            return false
        }
        if (!input.isOnlyLetters()) {
            this.errorText = "El Campo solo puede contener letras"
            return false
        }
        return true
    }
}