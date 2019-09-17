package org.kwrench.metrics

/**
 * A datapoint for a metic.
 */
data class MetricSample<T: Any>(
    var time: Double,
    var value: T)