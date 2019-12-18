package org.kwrench.generator.num

import org.kwrench.generator.Generator
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol

/**
 * Numerical constant generator.
 */
class Num(var value: Double): Generator<Double> {
    override val type get() = Double::class

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): Double {
        return value
    }
}