package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator


/**
 * Uses the x^exponent function to fade in and out.
 * @param exponent exponent to use for the interpolation curve.  2.0 by default.
 */
class PowInterpolator(var exponent: Double = 2.0): Interpolator {

    override fun interpolate(value: Double): Double {
        var t = value
        if (t < 0.5) {
            // Move t to 0..1 range.
            t *= 2.0

            // Return power
            return Math.pow(t, exponent) * 0.5
        } else {
            // Move t to 0..1 range
            t -= 0.5
            t *= 2.0

            // Flip t to 1..0 range
            t = 1.0 - t

            // Return flipped over power
            return 1.0 - Math.pow(t, exponent) * 0.5
        }
    }

    companion object {
        val IN_OUT = PowInterpolator(2.0)
    }

}
