package org.kwrench.generator.num

import org.kwrench.generator.Generator
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import kotlin.reflect.KClass

/**
 * Boolean constant generator.
 */
class Bool(var value: Boolean): Generator<Boolean> {
    override val type: KClass<Boolean> get() = Boolean::class

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): Boolean {
        return value
    }
}