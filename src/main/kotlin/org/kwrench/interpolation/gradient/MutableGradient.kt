package org.kwrench.interpolation.gradient

import org.kwrench.interpolation.Interpolator
import org.kwrench.interpolation.ValueMixer
import org.kwrench.interpolation.interpolators.LinearInterpolator
import java.util.*


/**
 * Default implementation of a gradient.
 * @param valueMixer used for interpolating between the values in the gradient.
 * @param defaultValue value to return if the gradient has no values.
 * @param defaultInterpolator interpolator to use when no interpolator is specified when adding a point.  Defaults to linear.
 * @param initialContent the entries to add initially.  Defaults to none.
 */
open class MutableGradient<T>(override var valueMixer: ValueMixer<T>,
                              var defaultValue: T,
                              var defaultInterpolator: Interpolator = LinearInterpolator.IN_OUT,
                              initialContent: Map<Double, T> = emptyMap()) : Gradient<T> {

    override val points = ArrayList<MutableGradientPoint<T>>()

    private val listeners = ArrayList<(MutableGradient<T>) -> Unit>()

    override val size: Int
        get() = points.size

    init {
        // Initialize content
        for (entry in initialContent) {
            addPoint(entry.key, entry.value)
        }
    }

    override fun addPoint(pos: Double, value: T, interpolator: Interpolator?) {
        points.add(MutableGradientPoint(pos, value, interpolator ?: defaultInterpolator))

        // Sort points if the new point is not after the previous ones
        if (points.size > 1 && pos < points.last().pos) {
            sortPoints()
        }

        onChanged()
    }

    override fun getClosestPoint(pos: Double): GradientPoint<T>? = getClosestMutablePoint(pos)

    override fun getClosestIndex(pos: Double): Int = findClosestIndex(pos)

    override fun getPointAtIndex(index: Int): GradientPoint<T>? {
        if (index < 0 || index >= points.size)
            return null
        else
            return points.get(index)
    }

    override fun removeClosestPoint(pos: Double): Boolean {
        return removePointAtIndex(findClosestIndex(pos))
    }

    override fun removePointAtIndex(index: Int): Boolean {
        if (index < 0 || index >= points.size) return false
        points.removeAt(index)
        onChanged()
        return true
    }

    override fun clear() {
        points.clear()
        onChanged()
    }

    override fun updatePoint(pointIndex: Int, position: Double, value: T, interpolator: Interpolator) {
        val mutablePoint = getClosestMutablePoint(pointIndex.toDouble())
        if (mutablePoint != null) {
            val positionChanged = mutablePoint.setPos(position)
            mutablePoint.value = value
            mutablePoint.interpolator = interpolator

            // The order of the points may have changed as position was updated, sort them
            if (positionChanged) sortPoints()

            onChanged()
        }
    }

    override fun setPointPosition(pointIndex: Int, position: Double) {
        val mutablePoint = getClosestMutablePoint(pointIndex.toDouble())
        if (mutablePoint != null) {
            val positionChanged = mutablePoint.setPos(position)

            // The order of the points may have changed as position was updated, sort them
            if (positionChanged) sortPoints()

            onChanged()
        }
    }

    override fun setPointValue(pointIndex: Int, newValue: T) {
        val mutablePoint = getClosestMutablePoint(pointIndex.toDouble())
        mutablePoint?.value = newValue

        onChanged()
    }

    override fun setPointInterpolation(pointIndex: Int, newInterpolation: Interpolator) {
        val mutablePoint = getClosestMutablePoint(pointIndex.toDouble())
        mutablePoint?.interpolator = newInterpolation

        onChanged()
    }

    override fun get(pos: Double): T {
        return get(pos, valueMixer)
    }

    override fun get(pos: Double, valueMixer: ValueMixer<T>): T {
        // If we are empty return null, if we only have one point return the value at it
        if (points.size <= 0) return defaultValue
        else if (points.size === 1) return points.first().value

        // Find closest point to the requested position
        val index = findClosestIndex(pos)
        val closestPos = points[index].pos
        val closestValue = points[index].value

        // Requested position is an exact match for the closest point, return value at it
        if (pos == closestPos) return closestValue

        val prevPos: Double
        val nextPos: Double
        val prevValue: T
        val nextValue: T
        val interpolator: Interpolator
        if (pos < closestPos) {
            // Requested position is before first point, return value at first point
            if (index == 0) return closestValue

            prevPos = points[index - 1].pos
            prevValue = points[index - 1].value
            nextPos = closestPos
            nextValue = closestValue
            interpolator = points[index].interpolator
        } else {
            // Requested position is after last point, return value at last point
            if (index >= points.size - 1) return closestValue

            prevPos = closestPos
            prevValue = closestValue
            nextPos = points[index + 1].pos
            nextValue = points[index + 1].value
            interpolator = points[index + 1].interpolator
        }

        // Use interpolation function to interpolate between the two closest values
        val t = interpolator.interpolate(pos, prevPos, nextPos, 0.0, 1.0)

        // Interpolate between the two closest values using the calculated interpolation value
        val result = valueMixer.mix(t, prevValue, nextValue)

        return result
    }

    /**
     * Called when the gradient is changed.
     * Notifies listeners.
     */
    protected open fun onChanged() {
        // Notify listeners.
        for (listener in listeners) {
            listener(this)
        }
    }

    /**
     * Add a listener that is called when the gradient is changed.
     */
    fun addListener(listener: (MutableGradient<T>) -> Unit) {
        listeners.add(listener)
    }

    /**
     * Remove the specified listener.
     */
    fun removeListener(listener: (MutableGradient<T>) -> Unit) {
        listeners.remove(listener)
    }

    private fun sortPoints() {
        points.sortBy { it.pos }
    }

    private fun getClosestMutablePoint(pos: Double): MutableGradientPoint<T>? {
        val index = findClosestIndex(pos)
        if (index < 0)
            return null
        else
            return points[index]
    }

    /**
     * Uses binary search to find the closest point to the requested position.
     * @return -1 if not found (== no points in gradient), otherwise index of closest point.
     */
    private fun findClosestIndex(pos: Double): Int {
        if (points.size <= 0)
            return -1
        else if (points.size === 1) return 0

        var start = 0
        var end = points.size - 1

        // Continue until we have the two closest entries to the position
        while (start + 1 < end) {
            // Calculate midpoint for roughly equal partition
            val mid = (start + end) / 2
            val midPos = points[mid].pos

            if (pos > midPos) {
                start = mid + 1
            } else if (pos < midPos) {
                end = mid - 1
            } else {
                // Found exact match
                return mid
            }
        }

        // Get closest
        val startPos = points[start].pos
        val endPos = points[end].pos
        if (distance(pos, startPos) < distance(pos, endPos))
            return start
        else
            return end
    }


    private fun distance(a: Double, b: Double): Double {
        return Math.abs(b - a)
    }

}
