package org.kwrench.color.colorspace

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.mix

/**
 * sRGB color space.
 */
object RGBColorSpace: ColorSpace {

    override fun toRGB(source: Double3, target: MutableDouble3) {
        // This is the rgb color space already
        target.set(source)
    }

    override fun fromRGB(source: Double3, target: MutableDouble3) {
        // This is the rgb color space already
        target.set(source)
    }

    override fun mix(t: Double, a: Double3, b: Double3, out: MutableDouble3) {
        // Linear interpolation
        out.x = mix(t, a.x, b.x)
        out.y = mix(t, a.y, b.y)
        out.z = mix(t, a.z, b.z)
    }
}