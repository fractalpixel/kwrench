package org.kwrench.generator

import org.kwrench.collections.RandomEntries
import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol

/**
 * Creates random objects of the specified type.
 */
// TODO: Replace map with a context interface that allows querying values?
interface Generator<T: Any>: RandomEntries<T> {

    /**
     * Generates several random instances.  Each uses a separate random number generator (so the state of random
     * after this function returns will be the same regardless of the number of entries requested).
     *
     * @param parameters parameters passed to the generators.
     * @param count number of entries to generate.  Must be 0 or positive.
     */
    fun generate(random: Rand, parameters: Map<Symbol, Any>? = null, count: Int): List<T> {
        require(count >= 0) {"Count parameter should be zero or larger, but was $count"}

        val seed = random.nextLong()
        val uniqueRandom = random.nextRand()

        val entries = ArrayList<T>(count)
        for (i in 1 .. count) {
            uniqueRandom.setSeed(seed, i.toLong())
            entries.add(generate(uniqueRandom, parameters))
        }
        return entries
    }

    /**
     * Generates a random instance.
     */
    fun generate(random: Rand, parameters: Map<Symbol, Any>? = null): T

    override fun nextRandomEntry(rand: Rand): T = generate(rand)
}