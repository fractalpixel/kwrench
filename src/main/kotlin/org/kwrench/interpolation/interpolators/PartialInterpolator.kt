package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator
import org.kwrench.math.map

/**
 * Extracts part of the original interpolator and uses it.
 * @param start start of the value range of the original interpolator to use.
 * @param end end of the value range of the original interpolator to use.
 * @param scaleValue if true, the value at the start and end positions of the original interpolator are sampled,
 * and the output value is scaled so that it is in the 0..1 range.
 */
class PartialInterpolator(val original: Interpolator,
                          val start: Double = 0.0,
                          val end: Double = 1.0,
                          val scaleValue: Boolean = true): Interpolator {
    override fun interpolate(value: Double): Double {
        return if (scaleValue) {
            map(original.interpolate(map(value, 0.0, 1.0, start, end)),
                original.interpolate(start),
                original.interpolate(end),
                0.0,
                1.0)
        } else {
            original.interpolate(map(value, 0.0, 1.0, start, end))
        }
    }
}