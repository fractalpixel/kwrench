package org.kwrench.varying

/**
 * A Varying that applies an operation to another varying and returns the result.
 */
class VaryingFun1(val v: Varying,
                  val operation: (Double) -> Double): Varying {

    override val value: Double get() = operation(v.value)
}