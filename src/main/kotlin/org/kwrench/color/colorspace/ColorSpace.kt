package org.kwrench.color.colorspace

import org.kwrench.color.GenColor
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.interpolation.ValueMixer
import org.kwrench.math.clamp0To1
import org.kwrench.math.mix

/**
 * Provides conversion to and from the color space, as well as color mixing /
 * interpolation within the color space.
 */
interface ColorSpace: ValueMixer<GenColor> {

    /**
     * Converts the specified color from this color space to sRGB color space.
     * Note that source and target may be the same.
     */
    fun toRGB(source: Double3,
              target: MutableDouble3)

    /**
     * Converts the specified color from sRGB color space to this color space.
     * Note that source and target may be the same.
     */
    fun fromRGB(source: Double3,
                target: MutableDouble3)

    /**
     * Converts the specified color coordinates from this color space to the target color space.
     */
    fun toColorSpace(source: Double3,
                     target: MutableDouble3,
                     targetColorSpace: ColorSpace) {
        if (targetColorSpace == this) {
            target.set(source)
        }
        else {
            this.toRGB(source, target)
            targetColorSpace.fromRGB(target, target)
        }
    }

    /**
     * Converts the specified color coordinates from the specified color space to this color space.
     */
    fun fromColorSpace(source: Double3,
                       target: MutableDouble3,
                       sourceColorSpace: ColorSpace) = sourceColorSpace.toColorSpace(source, target, this)

    /**
     * Interpolation between the coordinates in this colorspace.
     * Note that a, b, and/or out may be the same.
     */
    fun mix(t: Double,
            a: Double3,
            b: Double3,
            out: MutableDouble3)

    /**
     * Mixes two values in this color space using [t].
     * Converts a and/or b to this color space first if necessary.
     */
    override fun mix(t: Double, a: GenColor, b: GenColor, out: GenColor?): GenColor {
        val o = out ?: GenColor(colorSpace = this)

        val aValue = if (a.colorSpace == this) a.values
                else {
            val v = MutableDouble3()
            a.colorSpace.toRGB(a.values, v)
            fromRGB(v, v)
            v
        }
        val bValue = if (b.colorSpace == this) b.values
        else {
            val v = MutableDouble3()
            b.colorSpace.toRGB(b.values, v)
            fromRGB(v, v)
            v
        }

        mix(t,
            aValue,
            bValue,
            o.values)
        o.opacity = mix(t, a.opacity, b.opacity)
        o.set(colorSpace = this)
        return o
    }

    fun color(v1: Double, v2: Double, v3: Double, opacity: Double = 1.0): GenColor {
        return GenColor(v1, v2, v3, opacity, this)
    }

    fun color(values: Double3, opacity: Double = 1.0): GenColor {
        return GenColor(MutableDouble3(values), opacity, this)
    }

    fun clampValues(values: MutableDouble3) {
        values.x = values.x.clamp0To1()
        values.y = values.y.clamp0To1()
        values.z = values.z.clamp0To1()
    }
}