package org.kwrench.generator

import org.kwrench.collections.WeightedMap
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import java.lang.IllegalStateException
import kotlin.reflect.KClass

/**
 * A [Generator] that selects one of the contained generators based on their weight and returns its result.
 * At least one entry must be added to the entries before calling [generate].
 */
class TableGenerator<T: Any>(override val type: KClass<T>, var entries: WeightedMap<Generator<T>> = WeightedMap()): GeneratorBase<T>() {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        if (entries.isEmpty) throw IllegalStateException("No entries have been added to the TableGenerator, can not randomize an entry.")

        return entries.randomEntry(random).generate(random, parameters)
    }
}