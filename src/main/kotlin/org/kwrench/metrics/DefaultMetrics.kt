package org.kwrench.metrics

import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

/**
 * A default metrics storage.
 * By default stores the last 1000 samples only, change by passing in a value for [defaultSamplesStored],
 * or change the storage completely by passing in a different [metricsFactory].
 */
open class DefaultMetrics(override val title: String = "Metrics",
                          val defaultSamplesStored: Int = 1000,
                          val metricsFactory: (name: String, type: KClass<*>) -> Metric<*> =
                              {name, type -> LimitedMetric(name, type, defaultSamplesStored)}): MetricsBase() {

    private var startTimeMillis: Long = System.currentTimeMillis()
    override val elapsedTimeSeconds: Double get() = (System.currentTimeMillis() - startTimeMillis) / 1000.0

    override fun resetStartTime() {
        startTimeMillis = System.currentTimeMillis()
    }

    override val metrics = LinkedHashMap<String, Metric<*>>()

    val listeners = ArrayList<MetricsListener>()

    override fun <T: Any>report(metric: String, value: T, type: KClass<T>) {
        // Get sample data for metric, or create new sample set if new metric.
        val hadMetric = metrics.containsKey(metric)
        val metricSamples = metrics.getOrPut(metric, { metricsFactory(metric, type) }) as Metric<T>

        // Check type
        if (metricSamples.type != type) {
            throw IllegalArgumentException("Trying to report a sample of type ${type.simpleName} for a metric with name '$metric', but that metric has already received values of type ${metricSamples.type.simpleName}")
        }

        metricSamples.addSample(elapsedTimeSeconds, value)

        // Notify listeners about eventual metrics addition
        if (!hadMetric) {
            for (listener in listeners) {
                listener.metricAdded(this, metricSamples)
            }
        }

        // Notify listeners about sample addition
        for (listener in listeners) {
            listener.sampleAdded(this, metricSamples)
        }
    }

    override fun addListener(listener: MetricsListener) {
        listeners.add(listener)
    }

    override fun removeListener(listener: MetricsListener) {
        listeners.remove(listener)
    }
}