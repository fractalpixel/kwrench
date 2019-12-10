package org.kwrench.generator

import org.kwrench.classes.toNumberOfType
import java.lang.IllegalStateException
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

abstract class GeneratorBase<T: Any>: Generator<T> {

    /**
     * Utility function to convert a value to the output value type.
     */
    protected fun convertToTargetType(value: Any, valueDesc: String): T {
        if (type.isInstance(value)) {
            // Handle case where value is correct type
            return value as T
        } else if (type == String::class) {
            // Handle case where output type is string
            return value.toString() as T
        } else if (type.isSubclassOf(Number::class)) {
            // Handle case where output value is a number
            if (value is Number) {
                // Input is a number as well, attempt to convert
                return value.toNumberOfType(type as KClass<out Number>) as T
            } else if (value is String) {
                // Input is a string, attempt to parse
                return value.toString().toNumberOfType(type as KClass<out Number>) as T
            }
        } else if (type == Boolean::class) {
            // Handle case where output value is a boolean
            if (value is String) {
                // Input is a string, attempt to parse
                if (value.trim().toLowerCase() == "true") return true as T
                else if (value.trim().toLowerCase() == "false") return false as T
            }
        }

        // Could not convert
        throw IllegalStateException("Expected $valueDesc to be value of type $type, but it was a ${value.javaClass} with value '$value'")
    }

}