package org.kwrench.interpolation

/**
 * Something that can mix two values of the specified type.
 */
interface ValueMixer<T> {

    /**
     * @param t 0 = return value of a, 1 = return value of b,
     *          0..1 = linearly interpolate between a and b and return the result in out and as return value.
     * @param a value for t value of 0.  This value should not be changed by this function if it is mutable.
     * @param b value for t value of 1.  This value should not be changed by this function if it is mutable.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mix(t: Double, a: T, b: T, out: T? = null): T

    /**
     * @param t 0 = return value of a, 1 = return value of b,
     *          0..1 = linearly interpolate between a and b and return the result in out and as return value.
     * @param interpolator an interpolator to use to determine the final mixing ratio.
     * @param a value for t value of 0.  This value should not be changed by this function if it is mutable.
     * @param b value for t value of 1.  This value should not be changed by this function if it is mutable.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mix(t: Double, interpolator: Interpolator, a: T, b: T, out: T? = null): T {
        val pos = interpolator.interpolate(t)
        return mix(pos, a, b, out)
    }

}
