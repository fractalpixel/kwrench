package org.kwrench.interpolation.gradient

import org.kwrench.checking.Check
import org.kwrench.interpolation.Interpolator
import org.kwrench.interpolation.interpolators.LinearInterpolator

/**
 * Mutable implementation of GradientPoint.
 */
class MutableGradientPoint<T>(override var pos: Double,
                              override var value: T,
                              override var interpolator: Interpolator = LinearInterpolator.IN_OUT) : GradientPoint<T> {

    init {
        Check.normalNumber(pos, "pos")
    }

    /**
     * @return true if the position changed.
     */
    fun setPos(pos: Double): Boolean {
        Check.normalNumber(pos, "pos")

        if (pos != this.pos) {
            this.pos = pos
            return true
        } else {
            return false
        }
    }
}
