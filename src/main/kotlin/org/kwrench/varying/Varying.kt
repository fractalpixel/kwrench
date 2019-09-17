package org.kwrench.varying

/**
 * Something that can be operated on and passed around, but
 * has its value calculated on the fly on every request,
 * enabling continuously changing values at the cost of calculating
 * it every time.
 *
 * Basically a bit like a numerical abstract syntax tree (AST) node.
 */
interface Varying {

    /**
     * Calculates and returns the current value of this Varying.
     */
    val value: Double

    /**
     * Returns a new Varying that returns the sum of the values of this and the other varying.
     */
    operator fun plus(other: Varying) = VaryingFun2(this, other) { a, b -> a + b}

    /**
     * Returns a new Varying that returns the sum of the difference of this and the other varying.
     */
    operator fun minus(other: Varying) = VaryingFun2(this, other) { a, b -> a - b}

    /**
     * Returns a new Varying that returns the product of the values of this and the other varying.
     */
    operator fun times(other: Varying) = VaryingFun2(this, other) { a, b -> a * b}

    /**
     * Returns a new Varying that returns the division of the values of this and the other varying.
     */
    operator fun div(other: Varying) = VaryingFun2(this, other) { a, b -> a / b}


    /**
     * Returns a new Varying that returns the sum of the value of this varying and the specified number.
     */
    operator fun plus(other: Double) = plus(VaryingValue(other))

    /**
     * Returns a new Varying that returns the difference of the value of this varying and the specified number.
     */
    operator fun minus(other: Double) = minus(VaryingValue(other))

    /**
     * Returns a new Varying that returns the product of the value of this varying and the specified number.
     */
    operator fun times(other: Double) = times(VaryingValue(other))

    /**
     * Returns a new Varying that returns the division of the value of this varying and the specified number.
     */
    operator fun div(other: Double) = div(VaryingValue(other))


    /**
     * Returns a new Varying that applies the given function to the value of this varying.
     */
    fun map(operation: (Double) -> Double): Varying = VaryingFun1(this, operation)

    /**
     * Returns a new Varying that applies the given function to the value of this and the other varying.
     */
    fun combine(other: Varying, operation: (Double, Double) -> Double): Varying = VaryingFun2(this, other, operation)

    /**
     * Returns a new Varying that applies the given function to the value of this and the two other varyings.
     */
    fun combine(other1: Varying, other2: Varying, operation: (Double, Double, Double) -> Double): Varying = VaryingFun3(this, other1, other2, operation)

}


/**
 * Returns a new Varying that returns the sum of this value and the varying on the right side.
 */
operator fun Double.plus(other: Varying) = VaryingFun2(VaryingValue(this), other) { a, b -> a + b}

/**
 * Returns a new Varying that returns the difference of this value and the varying on the right side.
 */
operator fun Double.minus(other: Varying) = VaryingFun2(VaryingValue(this), other) { a, b -> a - b}

/**
 * Returns a new Varying that returns the product of this value and the varying on the right side.
 */
operator fun Double.times(other: Varying) = VaryingFun2(VaryingValue(this), other) { a, b -> a * b}

/**
 * Returns a new Varying that returns the division of this value and the varying on the right side.
 */
operator fun Double.div(other: Varying) = VaryingFun2(VaryingValue(this), other) { a, b -> a / b}


/**
 * Returns a new Varying that returns this value.
 */
fun Double.toVarying() = VaryingValue(this)

