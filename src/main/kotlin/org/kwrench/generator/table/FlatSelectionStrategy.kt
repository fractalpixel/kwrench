package org.kwrench.generator.table

/**
 * Flat weight profile, all entries have the same weight.  1 by default, or the value of the parameter if it is specified.
 */
object FlatSelectionStrategy: TableSelectionStrategy {
    override fun calculateWeight(position: Int, totalCount: Int, parameter: Double?): Double {
        return parameter ?: 1.0
    }
}