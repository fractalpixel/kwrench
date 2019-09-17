package org.kwrench.varying

/**
 * A Varying that calculates the result using the given function.
 */
class VaryingFun0(val f: () -> Double): Varying {

    override val value: Double get() = f()
}