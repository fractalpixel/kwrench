package org.kwrench.classes

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

/**
 *
 */
class TestClassUtils {


    @Test fun testToNumberOfType() {
        assertEquals(23.0, 23.toNumberOfType(Double::class), 0.00001)
        assertEquals(23.0, 23.toNumberOfType(Double::class.java), 0.00001)
        assertEquals(23, 23.0.toNumberOfType(Int::class))
        assertEquals(23, 23.0.toNumberOfType(Int::class.java))
        assertEquals(23, "23".toNumberOfType(Int::class))
        assertEquals(23.2, "23.2".toNumberOfType(Double::class), 0.00001)
        assertEquals(-23.2f, "-23.2".toNumberOfType(Float::class))
        assertEquals(-23.2f, "-23.2".toNumberOfType(Float::class.java))
        assertEquals(-23.toByte(), -23.0f.toNumberOfType(Byte::class))
        assertEquals(-23.toByte(), -23.0f.toNumberOfType(Byte::class.java))
        assertEquals(BigDecimal("33.0"), 33.0f.toNumberOfType(BigDecimal::class))
        assertEquals(BigDecimal("33.1"), 33.1f.toNumberOfType(BigDecimal::class.java))
    }

    @Test fun testNumberMath() {
        assertEquals(23.0, 10.add(13.toByte(), Double::class), 0.00001)
        assertEquals(20.0, 2.0.multiply(10.toShort(), Double::class.java), 0.00001)
        assertEquals(0.1f, 2.0.divide(20.toShort(), Float::class.java))
        assertEquals(BigDecimal("33.0"), 30.0f.add(3.toNumberOfType(BigDecimal::class), BigDecimal::class))
    }

}