package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import kotlin.reflect.KClass

/**
 * Retrieves a parameter with the specified id and returns it as the generated value.
 * If the parameter is not found the provided default value is returned.
 *
 * If T is of the incorrect type and can't be converted, an error is produced.
 * If T is String, then toString is applied to any parameter.
 * If T is a number type, then it is attempted to be converted, and throws an error on failure.
 */
class ParameterGenerator<T: Any>(var parameterId: Symbol,
                                 var defaultValue: T,
                                 override val type: KClass<T>): GeneratorBase<T>() {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        val value = parameters?.get(parameterId) ?: defaultValue
        return convertToTargetType(value, "parameter $parameterId")
    }

}