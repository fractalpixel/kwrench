package org.kwrench.metrics

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

/**
 * Used to access data for a metric.
 */
interface Metric<T: Any> {

    /**
     * Name or identifier for this metric.
     */
    val name: String

    /**
     * Type of values in this metric.
     */
    val type: KClass<T>

    /**
     * True if there are no values for this metric yet.
     */
    val isEmpty: Boolean

    /**
     * Number of samples that are available.
     */
    val size: Int

    /**
     * Latest recorded time value, or null if empty.
     */
    val latestTime: Double?

    /**
     * Earliest recorded time value, or null if empty.
     */
    val earliestTime: Double?

    /**
     * Add a new sample for this metric.
     */
    fun addSample(time: Double, value: T)

    /**
     * The time for the sample that is the specified number of steps ago, 0 returning the time of the latest sample.
     * Throws an exception if stepsBack is >= size or < 0.
     */
    fun getTime(stepsBack: Int): Double

    /**
     * The value for the sample that is the specified number of steps ago, 0 returning the value of the latest sample.
     * Throws an exception if stepsBack is >= size or < 0.
     */
    fun getValue(stepsBack: Int): T

    /**
     * The value for the sample that is the specified number of steps ago, 0 returning the value of the latest sample.
     * Throws an exception if stepsBack is >= size or < 0.
     *
     * Strings, booleans and other non-number values are converted to numbers by assigning each unique encountered value a number, starting from 1.
     * See [numberMappings].
     */
    fun getNumericValue(stepsBack: Int): Number

    /**
     * Stores mappings from non-numerical values to numbers, used when requesting a numerical value for the metric with [getNumericValue].
     * The mapping can be edited by the caller in advance, e.g. to associate certain values with certain numbers.
     * Otherwise values are associated with integers, starting with 1 for the first encountered value.
     */
    val numberMappings: MutableMap<T, Int>

    /**
     * Returns the sample the specified number of steps ago, 0 returning the latest sample.
     * [sampleOut] is the sample object to write the sample to, if not provided a new sample object is created.
     * Returns null if there is no sample at the specific step.
     */
    fun getSample(stepsBack: Int, sampleOut: MetricSample<T> = MetricSample(0.0, null as T)): MetricSample<T>? {
        // Check parameters
        if (stepsBack >= size) return null
        if (stepsBack < 0) throw IllegalArgumentException("Can not get samples from the future, stepsBack was $stepsBack")

        // Get time and value at sample
        sampleOut.time = getTime(stepsBack)
        sampleOut.value = getValue(stepsBack)

        return sampleOut
    }

    /**
     * The sample at the specified time, or null if no sample available before the time.
     *
     * NOTE: Currently inefficient implementation (time O(n) where n is number of samples).
     */
    fun getSampleIndexAtTime(timeSeconds: Double): Int? {
        // NOTE: This is slow if there are a lot of datapoints, a binary search would be more optimal
        for (i in 0 until size) {
            if (timeSeconds >= getTime(i)) return i
        }

        // We wanted a value from before the first available one
        return null
    }

    /**
     * Returns the closest value at or after the specified time.
     * Time is normally specified in seconds since starting the application.
     * Returns null if the time is before any values were recorded.
     *
     * NOTE: Currently inefficient implementation (time O(n) where n is number of samples).
     */
    operator fun get(timeSeconds: Double): T? {
        val sampleIndex = getSampleIndexAtTime(timeSeconds)
        return if (sampleIndex != null) getValue(sampleIndex)
        else null
    }
}