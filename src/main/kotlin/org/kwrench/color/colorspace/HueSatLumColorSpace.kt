package org.kwrench.color.colorspace

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.clamp0To1
import org.kwrench.math.wrap0To1

/**
 * Base class for the class or color spaces that have axis approximating
 * hue, saturation and luminance.
 */
abstract class HueSatLumColorSpace: ColorSpaceBase() {

    override fun mix(t: Double, a: Double3, b: Double3, out: MutableDouble3) {
        mixPolarHueSat(t, a, b, out)
    }

    override fun clampValues(values: MutableDouble3) {
        values.x = values.x.wrap0To1()
        values.y = values.y.clamp0To1()
        values.z = values.z.clamp0To1()
    }
}