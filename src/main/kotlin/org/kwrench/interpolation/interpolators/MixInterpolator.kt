package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator

/**
 * Mixes two interpolators, using one for the start and another for the end, with a third interpolator
 * specifying which interpolator is used (by default a linear interpolator).
 */
class MixInterpolator(val start: Interpolator,
                      val end: Interpolator,
                      val mixer: Interpolator = LinearInterpolator(),
                      val clampMix: Boolean = false): Interpolator {
    override fun interpolate(value: Double): Double {
        return mixer.interpolate(
            value,
            start.interpolate(value),
            end.interpolate(value),
            clampMix)
    }
}