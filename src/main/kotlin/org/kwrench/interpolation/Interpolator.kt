package org.kwrench.interpolation

import org.kwrench.interpolation.interpolators.*

/**
 * Something that interpolates between two values in some way (fills in values in between)
 */
// IDEA: Add bounce and overshooting interpolators, as well as sigmoid interpolator.  Could also add a gradient interpolator?
interface Interpolator: (Double) -> Double {

    override fun invoke(t: Double): Double = interpolate(t)

    /**
     * Interpolates between 0 and 1, using this interpolation function and an interpolation position t.
     * @param value relative value indicating the tweening position, goes from 0 to 1.
     *          May go beyond 0 or 1 as well, but the result may vary depending on the interpolator used.
     * @return a value between 0 and 1 (may exceed that range as well), at point t.
     */
    fun interpolate(value: Double): Double

    /**
     * Interpolates between targetStart and targetEnd, using this interpolation function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param t value that goes from 0 to 1 (and optionally beyond)
     * @param startValue result value when [t] = 0
     * @param endValue result value when [t] = 1
     * @param clamp if true, [t] is clamped to the 0..1 range,
     *              if false, [t] may be outside that range and the result may be outside the startValue..endValue range (default).
     * @return the value between startValue and endValue (may exceed that range as well), at point t.
     */
    fun interpolate(t: Double,
                    startValue: Double,
                    endValue: Double,
                    clamp: Boolean = false): Double {

        // Clamp position to range if requested
        var pos = t
        if (clamp) {
            if (pos < 0.0) pos = 0.0
            if (pos > 1.0) pos = 1.0
        }

        // Apply interpolation
        pos = interpolate(pos)

        // Expand the position to the output range
        return startValue + pos * (endValue - startValue)
    }

    /**
     * Interpolates between targetStart and targetEnd, using this interpolation function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param sourcePos relative value indicating mix between targetStart and targetEnd, goes from sourceStart to sourceEnd (or beyond if clampSourcePos is false).
     * @param sourceStart value for sourcePos corresponding to targetStart (defaults to 0.0)
     * @param sourceEnd value for sourcePos corresponding to targetEnd (defaults to 1.0)
     * @param targetStart result value when sourcePos = sourceStart
     * @param targetEnd result value when sourcePos = sourceEnd
     * @param clampSourcePos if true, sourcePos is clamped to the sourceStart..sourceEnd range,
     *                       if false, sourcePos may be outside that range and the result may be outside the targetStart..targetEnd range (default).
     * @return the value between targetStart and targetEnd (may exceed that range as well), at point sourcePos.
     */
    fun interpolate(sourcePos: Double,
                    sourceStart: Double,
                    sourceEnd: Double,
                    targetStart: Double,
                    targetEnd: Double,
                    clampSourcePos: Boolean = false): Double {

        // Normalize position to 0..1 range
        var t = if (sourceEnd == sourceStart) 0.5 else (sourcePos - sourceStart) / (sourceEnd - sourceStart)

        // Clamp position to range if requested
        if (clampSourcePos) {
            if (t < 0.0) t = 0.0
            if (t > 1.0) t = 1.0
        }

        // Interpolate
        t = interpolate(t)

        // Expand the position to the output range
        return targetStart + t * (targetEnd - targetStart)
    }

    /**
     * Returns a reverse interpolation of this one (value goes from 1 to 0 instead of 0 to 1)
     */
    fun reverse(): Interpolator = ReversedInterpolator(this)

    /**
     * Returns an inverted interpolation of this one (result goes from 1 to 0 instead of 0 to 1)
     */
    fun invert(): Interpolator = InvertedInterpolator(this)

    /**
     * Return a part of this interpolator as a full interpolation, by default scaling the value of the part to output in the 0..1 range.
     */
    fun part(start: Double, end: Double, scaleValue: Boolean = true) = PartialInterpolator(this, start, end, scaleValue)

    /**
     * Uses this interpolator for the first part and the second interpolator for the second part.
     * Optionally the point where the other interpolator will be used can be defined.
     */
    fun combine(other: Interpolator, splitPoint: Double = 0.5, compressStart: Boolean, compressEnd: Boolean): Interpolator = CombineInterpolator(this, other, splitPoint, compressStart, compressEnd)

    /**
     * Uses this interpolator for at the start and the second interpolator at the end, using a mixer to mix them (by default a linear mix)
     */
    fun mix(other: Interpolator, mixer: Interpolator = LinearInterpolator()): Interpolator = MixInterpolator(this, other, mixer)

    /**
     * Returns a remapped interpolator, using either just the first or last part of this interpolator, or splitting it at the middle and using last part first and first part last (or leaving it unchanged)
     */
    @Deprecated("Can be done better with other functions, e.g. part and combine and invert/reverse")
    fun remap(remapping: InterpolationRemap) = RemappedInterpolator(this, remapping)

}
