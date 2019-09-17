package org.kwrench.interpolation.interpolators

import org.kwrench.interpolation.Interpolator

/**
 * Remaps this interpolator, using either just the first or last part of it, or splitting it at the middle and using last part first and first part last (or leaving it unchanged)
 */
@Deprecated("InterpolationRemap is deprecated.")
class RemappedInterpolator(val original: Interpolator,
                           val remapping: InterpolationRemap
): Interpolator {
    override fun interpolate(value: Double): Double {
        return remapping.interpolate(value, original)
    }
}