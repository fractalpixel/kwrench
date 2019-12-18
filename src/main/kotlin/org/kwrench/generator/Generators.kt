package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.strings.toSymbol
import org.kwrench.symbol.Symbol
import java.lang.IllegalArgumentException

/**
 * A context with zero or more generators.  Generators can be accessed by name.
 * Generators can also use each other.
 */
class Generators(initialGenerators: Map<String, Generator<*>>) {

    val generators = HashMap(initialGenerators)

    /**
     * Generate value with the specified named generator.
     * Optionally pass in [parameters] and a [random] number generator.
     */
    operator fun <T: Any>get(name: String,
                             parameters: Map<Symbol, Any> = emptyMap(),
                             random: Rand = Rand.default): T {
        val gen = getGenerator<T>(name) ?: throw IllegalArgumentException("No generator named $name found.")
        val combinedMap = HashMap(parameters)
        for (g in generators) {
            combinedMap[g.key.toSymbol()] = g.value
        }
        return gen.generate(random, combinedMap)
    }

    /**
     * Get the generator with the specified name, or null if not found.
     */
    fun <T: Any>getGenerator(name: String): Generator<T>? = generators[name] as Generator<T>?

    /**
     * Set the generator with the specified name.
     */
    fun setGenerator(name: String, generator: Generator<*>) {
        generators[name] = generator
    }

}