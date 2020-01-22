package org.kwrench.generator

import com.github.h0tk3y.betterParse.grammar.parseToEnd
import org.junit.Test
import org.kwrench.random.Rand
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeneratorTest {

    /* Example

    foo=[asdhais;asdaskd;2:asd;sdas #sadas#sdf]
    new(tag1, tag2, tag3, structure1, structure2)
    find(prop=value or prop in [values] or prop like "*value*")
    select(entries, criteria)..

    structure generator (json -like)
    gen foo= #bar + {name= #name; desc += some desc; num1 += 1; num2 = #num1 * 2 + #num2 * 3}

     */

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