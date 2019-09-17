package org.kwrench.metrics

import org.kwrench.collections.ringbuffer.ArrayRingBuffer
import org.kwrench.collections.ringbuffer.DoubleRingBuffer
import java.math.BigDecimal
import java.math.BigInteger
import java.util.LinkedHashMap
import kotlin.reflect.KClass

/**
 * Stores a specific number of datapoints for a metric.
 * Uses a fairly efficient ringbuffer storage that avoids creating new objects.
 */
class LimitedMetric<T: Any>(
    override val name: String,
    override val type: KClass<T>,
    storageSize: Int = 1000): Metric<T> {

    /**
     * True if the type specified for this metric is one of the basic numerical types
     */
    val numericalType =
        type == Byte::class ||
        type == Short::class ||
        type == Int::class ||
        type == Long::class ||
        type == Float::class ||
        type == Double::class ||
        type == BigInteger::class ||
        type == BigDecimal::class

    private val times = DoubleRingBuffer(storageSize)
    private val values = ArrayRingBuffer<T>(storageSize)

    override val isEmpty: Boolean get() = times.isEmpty()
    override val size: Int get() = times.size

    override val numberMappings: MutableMap<T, Int> = LinkedHashMap()

    override val latestTime: Double? get() = if (isEmpty) null else times.first
    override val earliestTime: Double? get() = if (isEmpty) null else times.last

    override fun addSample(time: Double, value: T) {
        times.addFirst(time)
        values.addFirst(value)
    }

    override fun getTime(stepsBack: Int): Double = times[stepsBack]
    override fun getValue(stepsBack: Int): T = values[stepsBack]

    override fun getNumericValue(stepsBack: Int): Number {
        val value = getValue(stepsBack)
        return if (numericalType) {
            value as Number
        }
        else {
            numberMappings.getOrPut(value, {
                numberMappings.size + 1
            })
        }

    }

}