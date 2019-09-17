package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator


/**
 * IN_OUT = normal interpolator range,                                                      __/¨¨
 * IN = use first half of normal interpolator range and scale it to full range,             ____/
 * OUT = use second half of normal interpolator range and scale it to full range,           /¨¨¨¨
 * OUT_IN = first use second half of interpolator and then first half of the interpolator.  ,---'
 */
@Deprecated("This is not so useful, especially as it assumes the midpoint value is 0.5")
enum class InterpolationRemap {

    /**
     * Use the first half of a normal interpolator range and scale it to full range.  ____/
     */
    IN,

    /**
     * Use the second half of a normal interpolator range and scale it to full range.   /¨¨¨¨¨
     */
    OUT,

    /**
     * Normal interpolator range. __/¨¨
     */
    IN_OUT,

    /**
     * First use the second half of an interpolator and then the first half of the interpolator.  ,---'
     */
    OUT_IN;

    fun interpolate(t: Double, interpolator: Interpolator): Double {
        return when (this) {
            IN -> interpolator.interpolate(t * 0.5) * 2.0
            OUT -> interpolator.interpolate(t * 0.5 + 0.5) * 2.0 - 1.0
            IN_OUT -> interpolator.interpolate(t)
            OUT_IN -> (if (t < 0.5) interpolator.interpolate(t * 0.5 + 0.5) * 2.0 - 1.0
                       else interpolator.interpolate(t * 0.5) * 2.0)
        }
    }
}
