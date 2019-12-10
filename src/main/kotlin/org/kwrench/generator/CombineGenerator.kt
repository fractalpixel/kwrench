package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import kotlin.reflect.KClass


/**
 * Uses the function to combine the result of two generators to produce one result.
 */
open class CombineGenerator<T: Any, A: Any, B: Any>(override val type: KClass<T>,
                                                    var a: Generator<A>,
                                                    var b: Generator<B>,
                                                    var function: (A, B) -> T): Generator<T> {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        val resultA = a.generate(random, parameters)
        val resultB = b.generate(random, parameters)
        return function(resultA, resultB)
    }
}