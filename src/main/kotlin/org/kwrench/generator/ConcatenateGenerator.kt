package org.kwrench.generator

import org.kwrench.random.Rand
import org.kwrench.symbol.Symbol
import java.lang.StringBuilder
import kotlin.reflect.KClass

/**
 * Takes any number of generators and converts their output to strings and then concatenates the outputs.
 */
class StringConcatenateGenerator(var generators: List<Generator<*>>): Generator<String> {

    override val type: KClass<String> get() = String::class

    override fun generate(random: Rand, parameters: Map<Symbol, Any>?): String {
        val sb = StringBuilder()
        for (generator in generators) {
            val result = generator.generate(random, parameters)
            sb.append(result.toString())
        }
        return sb.toString()
    }
}