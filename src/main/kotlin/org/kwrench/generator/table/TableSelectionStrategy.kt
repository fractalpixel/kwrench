package org.kwrench.generator.table

/**
 * Describes how entries are selected form a table generator, if they have no custom weight specified.
 * This basically generates weights for entries based on their position in the table.
 */
// TODO: Add normal distributed with most probable initially (or least probable inverted curve)
interface TableSelectionStrategy {

    /**
     * Determine the weight for [position] (0-indexed), when there is [totalCount] amount of entries.
     * May use one supplied [parameter] in an implementation specific way if it is specified.
     */
    fun calculateWeight(position: Int, totalCount: Int, parameter: Double? = null): Double

}