package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator
import org.kwrench.math.map

/**
 * Uses the [start] interpolator for the part between 0 and [splitPoint],
 * and the [end] interpolator for the part between [splitPoint] and 1.
 * Does not scale the value in any way before passing it to the interpolators.
 * @param compressStart if true, the whole of the start interpolator will be used before the split point
 * @param compressEnd if true, the whole of the  end interpolator will be used after the split point
 */
class CombineInterpolator(val start: Interpolator,
                          val end: Interpolator,
                          val splitPoint: Double = 0.5,
                          val compressStart: Boolean = false,
                          val compressEnd: Boolean = false): Interpolator {
    override fun interpolate(value: Double): Double {
        return if (value < splitPoint) start.interpolate(if (compressStart) map(value, 0.0, splitPoint, 0.0, 1.0) else value)
               else end.interpolate(if (compressEnd) map(value, splitPoint, 1.0, 0.0, 1.0) else value)
    }
}