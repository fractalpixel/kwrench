package org.kwrench.interpolation.mixers

import org.kwrench.interpolation.ValueMixer

/**
 * Mixes any two numerical values.
 * Returns a double result.
 */
class NumericValueMixer(): ValueMixer<Number> {

    override fun mix(t: Double, a: Number, b: Number, out: Number?): Number {
        return mix(t, a.toDouble(), b.toDouble())
    }

}