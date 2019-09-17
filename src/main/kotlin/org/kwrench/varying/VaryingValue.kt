package org.kwrench.varying

/**
 * An editable varying value.
 * Other Varying that use this one are not notified if the value
 * changes, but the next time their value is requested they will
 * use the new value to calculate themselves.
 */
class VaryingValue(override var value: Double = 0.0): Varying