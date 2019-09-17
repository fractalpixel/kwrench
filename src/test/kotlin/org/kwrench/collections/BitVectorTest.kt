package org.kwrench.collections

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test

/**
 *
 */
class BitVectorTest {

    @Test
    fun bitVectorTest() {
        val v = BitVector()
        v[10] = true
        v[71] = true
        v[0] = true
        v[1] = true
        v[187] = true

        assertTrue(v[0])
        assertTrue(v[1])
        assertTrue(v[10])
        assertTrue(v[71])
        assertTrue(v[187])
        assertFalse(v[2])
        assertFalse(v[2000])
    }
}