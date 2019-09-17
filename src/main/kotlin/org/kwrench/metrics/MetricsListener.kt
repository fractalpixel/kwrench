package org.kwrench.metrics

interface MetricsListener {
    fun metricAdded(metrics: Metrics, metric: Metric<*>)
    fun sampleAdded(metrics: Metrics, metric: Metric<*>)
}