package org.kwrench.generator

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.Test
import org.kwrench.random.Rand
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorTest {

    @Test
    fun testParse() {
        val parser = GeneratorParser()
        val gens: Generators = parser.parseToEnd (
            "nameStart=[fo/bo/do/go];\n" +
                    "nameEnd=[gen/den/bon/der];\n" +
                    "name=<nameStart>[<nameStart>/]<nameEnd>;\n" +
                    "hello=Hello [World/Future/Random/Cthulhu]!;\n" +
                    "helloName=Name: <name>;")
        val gen: Generator<String> = gens.getGenerator<String>("hello")!!
        for (i in 1..10) println(gens.get<String>("hello"))
        for (i in 1..10) println(gens.get<String>("helloName"))
        assertEquals(String::class, gen.type)
        val result = gens.get<String>("hello")
        assertTrue(
            result == "Hello World!" ||
            result == "Hello Future!" ||
            result == "Hello Random!" ||
            result == "Hello Cthulhu!",
            "Result '$result' should be one of the expected values")
    }

}