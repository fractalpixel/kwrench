package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol

/**
 * Always generates the same result of the correct type.
 */
class ConstantGenerator<T: Any>(var value: T): Generator<T> {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        return value
    }
}