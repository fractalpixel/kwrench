package org.kwrench.interpolation.gradient

import org.kwrench.interpolation.Interpolator


/**
 * A point in a Gradient.
 */
interface GradientPoint<T> {

    /**
     * Position of this point in the gradient.
     */
    val pos: Double

    /**
     * Value at this point.
     */
    val value: T

    /**
     * Interpolation method to use from the previous point to this point.
     */
    val interpolator: Interpolator

}
