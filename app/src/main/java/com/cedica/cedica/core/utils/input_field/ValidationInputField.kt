package com.cedica.cedica.core.utils.input_field

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * The InputField class is a class that can be used to manage the state of an simple input field that
 * no requires any validation.
 * The class provides a common property:
 * - input: the value of the input field
 * The onChange method is a method that can be used to update the value of the input field.
 * @param T
 * @constructor
 * @param initialValueField initial value of the input field
 */
open class InputField<T>(
    initialValueField: T,
) {
    var input by mutableStateOf<T>(initialValueField)
        protected set

    open fun onChange(newValue: T) {
        this.input = newValue
    }
}

/**
 * The InputKeyboardField class is a class that can be used to manage the state of an input field
 * that requires validation.
 * The class provides three common properties:
 * - input: the value of the input field
 * - errorText: the error message of the input field.
 * - hasError: a boolean that indicates if the input field has an error. This property is reactive and
 * the value is updated when the errorText property is updated.
 * The inputIsValid method is an abstract method that should be implemented by the child class, this
 * method should return a boolean that indicates if the input field is valid.
 *
 * @param T type of the input field
 * @constructor
 * @param initialValueField initial value of the input field
 */
abstract class ValidationInputField<T>(
    initialValueField: T,
): InputField<T>(initialValueField) {
    var errorText = ""
        protected set

    val hasError by derivedStateOf<Boolean> { !this.inputIsValid() }

    abstract fun inputIsValid(): Boolean

}