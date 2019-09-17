package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator


/**
 * Linear interpolation.
 */
class LinearInterpolator : Interpolator {

    override fun interpolate(value: Double): Double {
        return value
    }

    companion object {
        val IN_OUT = LinearInterpolator()
    }
}
