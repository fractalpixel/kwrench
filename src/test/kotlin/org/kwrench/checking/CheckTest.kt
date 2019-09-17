package org.kwrench.checking

import org.junit.Assert
import org.junit.Test
import org.kwrench.checking.Check.contained
import org.kwrench.checking.Check.empty
import org.kwrench.checking.Check.equal
import org.kwrench.checking.Check.greater
import org.kwrench.checking.Check.greaterOrEqual
import org.kwrench.checking.Check.identifier
import org.kwrench.checking.Check.inRange
import org.kwrench.checking.Check.inRangeInclusive
import org.kwrench.checking.Check.inRangeZeroToOne
import org.kwrench.checking.Check.instanceOf
import org.kwrench.checking.Check.less
import org.kwrench.checking.Check.lessOrEqual
import org.kwrench.checking.Check.negative
import org.kwrench.checking.Check.negativeOrZero
import org.kwrench.checking.Check.nonEmptyString
import org.kwrench.checking.Check.normalNumber
import org.kwrench.checking.Check.notContained
import org.kwrench.checking.Check.notEmpty
import org.kwrench.checking.Check.notInstanceOf
import org.kwrench.checking.Check.notZero
import org.kwrench.checking.Check.positive
import org.kwrench.checking.Check.positiveOrZero
import org.kwrench.checking.Check.strictIdentifier

import java.util.Arrays
import java.util.HashMap

class CheckTest {

    @Test
    @Throws(Exception::class)
    fun testInvariant() {
        Check.invariant(true, "should work")

        try {
            Check.invariant(false, "should fail")
            Assert.fail()
        } catch (e: java.lang.IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testEqual() {
        Check.equals("abc", "foo", "abc" + "", "bar")
        Check.equals(2, "foo", 1 + 1, "bar")
        Check.equalRef(2, "foo", 1 + 1, "bar")

        val s = "asdf"
        Check.equalRef(s, "foo", s, "bar")

        try {
            Check.equals("abc", "foo", "abc" + "def", "bar")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.equals("abc", "foo", "ABC", "bar")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.equalRef("asdf", "foo", "ghjk", "bar")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testStrings() {
        nonEmptyString("1", "foo")
        nonEmptyString("abc", "foo")
        nonEmptyString(" as \n d  ", "foo")

        try {
            nonEmptyString("", "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString("\n", "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString(" ", "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            nonEmptyString(" \t \r\n  ", "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testIdentifier() {
        identifier("JavaIdentifier123", "id")
        identifier("x", "id")
        identifier("_3", "id")
        identifier("x_3", "id")
        identifier("\$foo\$bar$$$$", "id")
        identifier("Ångström", "id")

        strictIdentifier("x", "id")
        strictIdentifier("_x", "id")
        strictIdentifier("xxx3x3", "id")
        strictIdentifier("___sdf__", "id")

        try {
            identifier(null, "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("?sdf", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("x^", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("Asdf+5", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            identifier("+-*/", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier(" ", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            identifier("3x", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            strictIdentifier("sd\$f", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier("Ångström", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier("", "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            strictIdentifier(null, "id")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testCollections() {
        val l = Arrays.asList("a", "b", "c", "d")
        val l2 = Arrays.asList(null, "asdf")
        val l3 = Arrays.asList<String>()
        val m = HashMap<String, String>()
        val m2 = HashMap<String?, String>()
        m.put("abc", "123")
        m.put("def", "456")
        m2.put(null, "asdf")

        contained("a", l, "l")
        contained("c", l, "l")
        contained(null, l2, "l2")

        contained("abc", m, "m")
        contained(null, m2, "m2")

        notEmpty(l, "l")
        empty(l3, "l3")

        notContained(null, l, "l")
        notContained("g", l, "l")

        notContained("geh", m, "m")
        notContained(null, m, "m")

        try {
            contained("g", l, "l")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained(null, l, "l")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained("geh", m, "m")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            contained(null, m, "m")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained("a", l, "l")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained(null, l2, "l2")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained("abc", m, "m")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notContained(null, m2, "m2")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            empty(l, "l")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notEmpty(l3, "l3")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testInstanceOf() {
        instanceOf("abc", "foo", String::class.java)
        instanceOf("abc", "foo", Any::class.java)
        notInstanceOf("abc", "foo", Int::class.java)

        try {
            instanceOf("abc", "foo", Int::class.java)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notInstanceOf("abc", "foo", Any::class.java)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }


        try {
            notInstanceOf("abc", "foo", String::class.java)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testFail() {
        try {
            Check.fail("Some reason to fail")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            Check.fail("abc", "foo", "should be imaginary string")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testNormalNumber() {
        normalNumber(0.0, "foo")
        normalNumber(-1.0, "foo")
        normalNumber(1f, "foo")
        normalNumber(java.lang.Double.MIN_NORMAL, "foo")
        normalNumber(-java.lang.Double.MIN_NORMAL, "foo")

        try {
            normalNumber(java.lang.Double.POSITIVE_INFINITY, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Double.NEGATIVE_INFINITY, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Float.NEGATIVE_INFINITY, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            normalNumber(java.lang.Double.NaN, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testSignChecks() {
        positive(1, "foo")
        positive(2309.23, "foo")
        positive(0.00001, "foo")
        positive(Integer.MAX_VALUE, "foo")

        positiveOrZero(0, "foo")
        positiveOrZero(0.0, "foo")
        positiveOrZero(0.00001, "foo")
        positiveOrZero(1, "foo")
        positiveOrZero(4234, "foo")
        positiveOrZero(4234.231f, "foo")

        negative(-0.001f, "foo")
        negative(-123123.123, "foo")
        negative(-1, "foo")
        negative(Integer.MIN_VALUE, "foo")

        negativeOrZero(0, "foo")
        negativeOrZero(0.0, "foo")
        negativeOrZero(-0.00001f, "foo")
        negativeOrZero(-1, "foo")
        negativeOrZero(-1123.123, "foo")
        negativeOrZero(Integer.MIN_VALUE, "foo")

        notZero(1, "foo")
        notZero(-1, "foo")
        notZero(Integer.MIN_VALUE, "foo")
        notZero(Integer.MAX_VALUE, "foo")
        notZero(0.001, "foo", 0.0001)
        notZero(-0.001, "foo", 0.0001)
        notZero(1.0, "foo", 0.0001)
        notZero(-1.0f, "foo", 0.0001f)

        try {
            positive(0, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positive(-1, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positive(-0.0001, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positiveOrZero(-0.0001, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            positiveOrZero(-1, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0f, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(0.00001, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negative(1, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negativeOrZero(0.0012, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            negativeOrZero(1, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notZero(0, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            notZero(0.000f, "foo", 0.00001f)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }

    @Test
    @Throws(Exception::class)
    fun testComparisons() {

        // Ints
        greater(3, "foo", 0)

        greater(3, "foo", 0, "limit")
        greater(-4, "foo", -5, "limit")

        greaterOrEqual(3, "foo", 0, "limit")
        greaterOrEqual(3, "foo", 3, "limit")
        greaterOrEqual(-4, "foo", -5, "limit")
        greaterOrEqual(-4, "foo", -4, "limit")

        less(-4, "foo", -3, "limit")
        less(1000, "foo", 1001, "limit")

        lessOrEqual(1000, "foo", 1001, "limit")
        lessOrEqual(1000, "foo", 1000, "limit")
        lessOrEqual(-1, "foo", 0, "limit")

        equal(0, "foo", 0, "target")
        equal(-42, "foo", -42, "target")
        equal(Integer.MAX_VALUE, "foo", Integer.MAX_VALUE, "target")

        // Floats
        greater(3f, "foo", 0f, null)
        greater(3.0f, "foo", 2.99f)
        greater(3f, "foo", 2.99f)

        greaterOrEqual(3f, "foo", 2.99f, "limit")
        greaterOrEqual(3f, "foo", 3.0f, "limit")
        greaterOrEqual(3.0f, "foo", 3f, "limit")
        greaterOrEqual(3.01f, "foo", 3f, "limit")

        lessOrEqual(3f, "foo", 3.1f, "limit")
        lessOrEqual(3.0f, "foo", 3f, "limit")

        less(3.0, "foo", 3.000000001, "limit")
        less(-0.1f, "foo", 0f, "limit")

        equal(3.14f, "foo", 3.14f, "limit", 0.00001f)
        equal(3f, "foo", 4f, "limit", 1f)

        // Test failures
        try {
            Check.greaterOrEqual(3.14f, "param", Math.PI.toFloat(), "Pi")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(3.14f, "param", Math.PI.toFloat(), "Pi", 0.0001f)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3, "param", 2, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3, "param", 3, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            less(3f, "param", 3.0f, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            greater(3f, "param", 3.1f, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            greater(-1, "param", 0, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(1, "param", 0, "limit")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            equal(0f, "param", 0.000001f, "limit", 0.00000001f)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }


    @Test
    @Throws(Exception::class)
    fun testRanges() {
        inRange(0, "foo", -1, 1)
        inRange(0.1, "foo", -1.0, 1.0)
        inRange(0f, "foo", -1.0f, 1f)

        inRange(43.0, "foo", 42.5, 43.01)
        inRange(43, "foo", 42, 44)
        inRange(43, "foo", 43, 44)

        inRangeInclusive(43, "foo", 42, 43)
        inRangeInclusive(43.0, "foo", 42.5, 43.0)

        inRangeZeroToOne(0.0, "foo")
        inRangeZeroToOne(0f, "foo")
        inRangeZeroToOne(0.34, "foo")
        inRangeZeroToOne(1.0, "foo")
        inRangeZeroToOne(1f, "foo")

        try {
            inRange(43.0, "foo", 42.5, 43.0)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRange(43, "foo", 42, 43)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRange(41, "foo", 42, 43)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeInclusive(43f, "foo", 41f, 42f)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeInclusive(40, "foo", 41, 42)
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(1.001, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(-0.001, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

        try {
            inRangeZeroToOne(2f, "foo")
            Assert.fail()
        } catch (e: IllegalArgumentException) {
        }

    }
}
