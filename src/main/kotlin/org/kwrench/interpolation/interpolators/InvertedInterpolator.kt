package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator

/**
 * Inverts the interpolator given as parameter using the formula value = 1.0 - [a].interpolate(value)
 */
class InvertedInterpolator(val a: Interpolator): Interpolator {
    override fun interpolate(value: Double): Double = 1.0 - a.interpolate(value)
}