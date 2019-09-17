package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator


/**
 * Simple cosine based interpolation.
 */
class CosineInterpolator : Interpolator {

    override fun interpolate(value: Double): Double {
        return 0.5 - 0.5 * Math.cos(value * Math.PI)
    }

    companion object {
        val IN_OUT = CosineInterpolator()
    }

}