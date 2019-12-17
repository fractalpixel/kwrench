package org.kwrench.generator

import org.junit.Test
import org.kwrench.random.Rand
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorTest {

    @Test
    fun testParse() {
        val parser = GeneratorParser()
        val gen = parser.parseGenerator("Hello [World|Future|Random|Cthulhu]!") as Generator<String>
        val result = gen.generate(Rand.default)
        for (i in 1..5) println(gen.generate(Rand.default))
        assertEquals(String::class, gen.type)
        assertTrue(
            result == "Hello World!" ||
            result == "Hello Future!" ||
            result == "Hello Random!" ||
            result == "Hello Cthulhu!",
            "Result should be one of the expected values")
    }

}