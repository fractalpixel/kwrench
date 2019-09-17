package org.kwrench.interpolation.mixers

import org.kwrench.interpolation.Interpolator

/**
 * An interface that indicates that a type knows how to mix itself with other instances of the same type.
 */
interface Mixable<T> {

    /**
     * Returns a linear mix of this value and the other.  Neither this or the other should be changed, instead a new value should be returned.
     * If this class is mutable and a resultOut value is provided, the mixed value should be stored in resultOut and resultOut returned,
     * instead of creating a new instance of this type.
     *
     * @param mixAmount 0 = return value of this, 1 = return value of other,
     *          0..1 = linearly interpolate between this and the other value and return the result in resultOut and as return value.
     * @param other value to mix with.  This value should not be changed by this function if it is mutable.
     * @param resultOut if T is a mutable type and resultOut is not null, the result of the interpolation should be written to this object, and this object returned as result.
     * @return the mixed value (using the resultOut instance, if it is provided and mutable).
     */
    fun mix(mixAmount: Double, other: T, resultOut: T? = null): T

    /**
     * Returns an interpolation of this value and the other.  Neither this or the other should be changed, instead a new value should be returned.
     * If this class is mutable and a resultOut value is provided, the mixed value should be stored in resultOut and resultOut returned,
     * instead of creating a new instance of this type.
     *
     * @param mixAmount 0 = return value of this, 1 = return value of other,
     *          0..1 = linearly interpolate between this and the other value and return the result in resultOut and as return value.
     * @param interpolator an interpolator to use to determine the final mixing ratio.
     * @param other value to mix with.  This value should not be changed by this function if it is mutable.
     * @param resultOut if T is a mutable type and resultOut is not null, the result of the interpolation should be written to this object, and this object returned as result.
     * @return the mixed value (using the resultOut instance, if it is provided and mutable).
     */
    fun mix(mixAmount: Double, interpolator: Interpolator, other: T, resultOut: T? = null): T {
        val pos = interpolator.interpolate(mixAmount)
        return mix(pos, other, resultOut)
    }

}