package org.kwrench.metrics


/**
 * Fits in general utilities for metrics, in this case FPS reporting that needs local state.
 */
abstract class MetricsBase: Metrics {

    private var lastTime = System.currentTimeMillis()
    private var lastFrameTime: Long = 0


    override fun reportFrameTime(frameTypeName: String,
                                 fps: Boolean,
                                 frameTimeMilliseconds: Boolean,
                                 frameTimeVariation: Boolean) {
        val time = System.currentTimeMillis()
        val frameTimeMs = (time - lastTime)
        lastTime = time
        lastFrameTime = frameTimeMs

        // TODO: Count number of frames since the last second change instead?  Or duration of the last 100 frames or so?  Now it gives absurd numbers if one frame is very soon after the previous one.
        if (fps) report("${frameTypeName}s per second", 1000.0 / frameTimeMs)
        if (frameTimeMilliseconds) report("$frameTypeName time (ms)", frameTimeMs)
        if (frameTimeVariation) report("$frameTypeName time variation (ms)", frameTimeMs - lastFrameTime )
    }
}