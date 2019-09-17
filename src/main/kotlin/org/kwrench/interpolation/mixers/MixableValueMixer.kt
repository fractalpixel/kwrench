package org.kwrench.interpolation.mixers

import org.kwrench.interpolation.ValueMixer

/**
 * A ValueMixer that works with a class that implements Mixable.
 */
class MixableValueMixer<T: Mixable<Any>>: ValueMixer<T> {

    override fun mix(t: Double, a: T, b: T, out: T?): T = a.mix(t, b, out) as T

}