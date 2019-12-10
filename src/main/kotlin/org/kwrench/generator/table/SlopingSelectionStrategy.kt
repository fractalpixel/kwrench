package org.kwrench.generator.table

import org.kwrench.math.map
import java.lang.Math.abs

/**
 * Sloping linear weight profile, earlier entries are more probable than later (unless parameter is < 0).
 * First entry has the weight of the total number of entries, and last entry has weight of 1.
 * If the parameter is specified, it determines the relative gradient of the slope.  At 1 it behaves like above, at 0
 * all probabilities are 1, and at -1 the initial probabilities are 1 / entrycount.  A parameter > 1 increases the slope further,
 * making the early entries even more probable.
 */
object SlopingSelectionStrategy: TableSelectionStrategy {
    override fun calculateWeight(position: Int, totalCount: Int, parameter: Double?): Double {
        val factor = parameter ?: 1.0
        val start = if (factor < 0) 1.0 / (1.0 + totalCount * abs(factor)) else 1.0 + totalCount * factor
        return map(position.toDouble(), 0.0, totalCount - 1.0, start, 1.0)
    }
}