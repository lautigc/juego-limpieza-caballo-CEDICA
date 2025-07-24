package com.cedica.cedica.core.utils.input_field

class NumberField<T>(
    private val rangeStart: T,
    private val rangeEnd: T,
    initialValue: T
): ValidationInputField<T>(initialValue) where T : Number, T : Comparable<T>{

    override fun inputIsValid(): Boolean {
        if (this.input < this.rangeStart || this.input > this.rangeEnd) {
            this.errorText = "El valor debe estar entre [$rangeStart, $rangeEnd]"
            return false
        }
        return true
    }

    fun toInput(value: String, converter: (String) -> T, emptyOrNullValue: T): T {
        return if (value.isEmpty() || value.isBlank()) {
            emptyOrNullValue
        } else {
            converter(value)
        }
    }
}