package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol

/**
 * Retrieves a parameter with the specified id and returns it as the generated value.
 * If the parameter is not found, returns the provided default value.
 *
 * NOTE: The parameter is assumed to be of the correct type.
 */
class ParameterGenerator<T: Any>(var parameterId: Symbol,
                                 var defaultValue: T): Generator<T> {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        return (parameters?.get(parameterId) ?: defaultValue) as T
    }
}