package org.kwrench.symbol

import org.junit.Test

import org.junit.Assert.*

class TestSymbol {
    @Test
    @Throws(Exception::class)
    fun testSymbol() {
        val foo1 = Symbol["foo"]
        val foo2 = Symbol[String(charArrayOf('f', 'o', 'o'))]
        val bar = Symbol["bar"]

        assertEquals(foo1, foo2)
        assertNotEquals(foo1, bar)
        assertTrue(foo1 == foo2)
        assertTrue(foo1 === foo2)
        assertTrue(foo1 != bar)
        assertTrue(foo1 !== bar)


        assertEquals("foo", foo1.string)
        assertEquals("foo", foo2.string)


        Symbol["_ahab"]
        Symbol["abc123"]
        Symbol["abc__123"]
        Symbol["x"]
        Symbol["X1"]
        Symbol["_"]

        try {
            Symbol[""]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["  \n "]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol[" asdf"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["a sdf"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["asdf "]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["1asd"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["Ångström"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["Engström"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["asd\$asd"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Symbol["-+asd23"]
            fail()
        } catch (e: IllegalArgumentException) {
        }

    }
}
