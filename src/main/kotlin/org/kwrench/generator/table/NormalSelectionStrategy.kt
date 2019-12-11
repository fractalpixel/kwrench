package org.kwrench.generator.table

import org.kwrench.math.MathUtils.gaussianFunction
import org.kwrench.math.map
import java.lang.Math.abs

/**
 * Normal distributed weight profile, with first entry as most probable and last as least probable.
 * The parameter controls in what standard deviation the last element will be, by default 3 (this includes the
 * traditional S shape of the normal distribution, making the last elements very unlikely but not impossible).
 * The values are scaled so that the average value is 1.
 */
object NormalSelectionStrategy: TableSelectionStrategy {
    override fun calculateWeight(position: Int, totalCount: Int, parameter: Double?): Double {
        val extent = parameter ?: 3.0
        val x = map(position.toDouble(), 0.0, totalCount.toDouble() - 1.0, 0.0, extent)
        return gaussianFunction(x, 1.0, 0.0) * totalCount.toDouble() // Multiply with total count to have average value of 1.
    }
}