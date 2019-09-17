package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator


/**
 * Reverses the interpolator given as parameter using the formula value = [a].interpolate(1.0 - value)
 */
class ReversedInterpolator (val a: Interpolator): Interpolator {
    override fun interpolate(value: Double): Double = a.interpolate(1.0 - value)
}