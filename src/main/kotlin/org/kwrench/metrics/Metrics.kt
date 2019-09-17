package org.kwrench.metrics

import kotlin.reflect.KClass

// TODO: Each metric function could support specifying the name of the axis / group to use, metrics with the same axis can be shown on the same chart.
/**
 * Interface intended for something that can report various metrics during runtime,
 * e.g. number of clients, processing load, and so on.  The idea is that this information
 * can be displayed interactively in some GIU, or reported in other ways.
 * Metrics are usually numerical, or in some cases booleans or statuses.
 */
interface Metrics {

    /**
     * Describes this collection of metrics.
     */
    val title: String

    /**
     * Report the value of an integer metric at this time.
     * Stored as a Long metric for interoperability convenience.
     */
    fun report(metric: String, value: Int) = report(metric, value.toLong(), Long::class)

    /**
     * Report the value of a long metric at this time.
     */
    fun report(metric: String, value: Long) = report(metric, value, Long::class)

    /**
     * Report the value of a float metric at this time.
     * Stored as a Double metric for interoperability convenience.
     */
    fun report(metric: String, value: Float) = report(metric, value.toDouble(), Double::class)

    /**
     * Report the value of a double metric at this time.
     */
    fun report(metric: String, value: Double) = report(metric, value, Double::class)

    /**
     * Report the value of a boolean metric at this time.
     */
    fun report(metric: String, value: Boolean) = report(metric, value, Boolean::class)

    /**
     * Report the value of a text metric at this time (typically for statuses, where only a few different strings are used).
     */
    fun report(metric: String, value: String) = report(metric, value, String::class)

    /**
     * Report the value of a metric of arbitrary type at this time.
     * Avoid passing in any values that take a lot of space or change over time.
     * Used by the other report methods to provide one function for descendants to implement.
     */
    fun <T: Any>report(metric: String, value: T, type: KClass<T>)

    /**
     * Utility method for reporting memory stats, by default reports total and used memory in MiB.
     */
    fun reportMemoryUsage(totalMemory: Boolean = true,
                          usedMemory: Boolean = true,
                          freeMemory: Boolean = false,
                          maxMemory: Boolean = false) {
        val runtime = Runtime.getRuntime()
        if (totalMemory) report("Total Memory (MiB)", runtime.totalMemory() / MiB)
        if (usedMemory) report("Used Memory (MiB)", (runtime.totalMemory() - runtime.freeMemory()) / MiB)
        if (freeMemory) report("Free Memory (MiB)", runtime.freeMemory() / MiB)
        if (maxMemory) report("Max Memory (MiB)", (runtime.totalMemory() - runtime.freeMemory()) / MiB)
    }

    /**
     * Utility method for reporting frames per second and related properties.
     * Call this once every frame to allow it to calculate the frame times.
     * Use [frameTypeName] if you want to use a different prefix for the report, e.g. separate logic and rendering reporting.
     */
    fun reportFrameTime(frameTypeName: String = "Frame",
                        fps: Boolean = true,
                        frameTimeMilliseconds: Boolean = false,
                        frameTimeVariation: Boolean = true)

    /**
     * Reported metrics and their history
     */
    val metrics: Map<String, Metric<*>>

    /**
     * Time that has passed since this class was created, and reporting begun.
     */
    val elapsedTimeSeconds: Double

    /**
     * Reset the start time to now, so time 0 is now.
     */
    fun resetStartTime()

    /**
     * Add listener that is notified when metrics or samples are added.
     */
    fun addListener(listener: MetricsListener)

    /**
     * Remove specified listener.
     */
    fun removeListener(listener: MetricsListener)

    companion object {
        private val MiB = 1024 * 1024
    }
}