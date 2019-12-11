package org.kwrench.generator.table

import org.kwrench.checking.Check
import org.kwrench.math.map
import java.lang.Math.abs
import kotlin.math.sign

/**
 * Sloping linear weight profile, earlier entries are more probable than later (unless parameter is < 0).
 * First entry has a probability that is total number of entries times larger than the last entry.
 * If the parameter is specified, it determines the relative gradient of the slope.  At 1 it behaves like above, at 0
 * all probabilities are 1.  A parameter > 1 increases the slope further, making the early entries even more probable.
 * Negative parameter values are not allowed.
 * The average of the probabilities stays as 1.
 */
object SlopingSelectionStrategy: TableSelectionStrategy {
    override fun calculateWeight(position: Int, totalCount: Int, parameter: Double?): Double {
        val factor = parameter ?: 1.0
        Check.positiveOrZero(factor, "parameter")
        val start = 1.0 + totalCount * factor
        val sum = (totalCount + totalCount*factor*totalCount*factor / 2.0)
        val adjust = sum / totalCount
        return map(position.toDouble(), 0.0, totalCount - 1.0, start, 1.0) / adjust
    }
}