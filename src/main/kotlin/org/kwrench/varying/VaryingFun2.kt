package org.kwrench.varying

/**
 * Combines two varying in some way.
 */
class VaryingFun2(val a: Varying,
                  val b: Varying,
                  val operation: (a: Double, b: Double) -> Double): Varying {
    override val value: Double get() = operation(a.value, b.value)
}