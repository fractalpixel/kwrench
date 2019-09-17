package org.kwrench.varying

/**
 * Combines three varying in some way.
 */
class VaryingFun3(val a: Varying,
                  val b: Varying,
                  val c: Varying,
                  val operation: (a: Double, b: Double, c: Double) -> Double): Varying {
    override val value: Double get() = operation(a.value, b.value, c.value)
}