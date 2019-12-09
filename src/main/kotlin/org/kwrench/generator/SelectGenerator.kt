package org.kwrench.generator

import org.kwrench.collections.WeightedMap
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import java.lang.IllegalStateException

/**
 * A [Generator] that selects one of the contained generators based on their weight and returns its result.
 * At least one entry must be added to the entries before calling [generate].
 */
class SelectGenerator<T: Any>(var entries: WeightedMap<Generator<T>> = WeightedMap()): Generator<T> {

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): T {
        if (entries.isEmpty) throw IllegalStateException("No entries have been added to the SelectGenerator, can not randomize an entry.")

        return entries.randomEntry(random).generate(random, parameters)
    }
}