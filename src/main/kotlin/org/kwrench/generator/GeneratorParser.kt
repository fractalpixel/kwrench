package org.kwrench.generator

import com.github.h0tk3y.betterParse.grammar

/**
 * Parses generator description from a text string.
 */
class GeneratorParser: Grammar {

    /**
     * Parses a single generator.
     */
    fun parseGenerator(input: String): Generator<*> {
        return ConstantGenerator("asdgf", String::class)
    }

}