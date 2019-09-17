package org.kwrench.color.colortype

import org.kwrench.color.GenColor
import org.kwrench.color.colorspace.ColorSpace
import org.kwrench.color.colorspace.RGBColorSpace
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3

/**
 * GenColor specific utilities.
 */
object GenColorType: ColorType<GenColor>() {


    override fun getColorValues(color: GenColor, out: MutableDouble3): Double3 {
        out.set(color.values)
        return out
    }

    override fun getOpacity(color: GenColor): Double {
        return color.opacity
    }

    override fun createColorRGB(red: Double, green: Double, blue: Double, alpha: Double, colorOut: GenColor?): GenColor {
        val color = colorOut ?: GenColor()
        color.set(red, green, blue, alpha, RGBColorSpace)
        return color
    }

    override fun createColor(
        values: Double3,
        opacity: Double,
        colorSpace: ColorSpace,
        colorOut: GenColor?
    ): GenColor {
        val color = colorOut ?: GenColor()
        color.set(values, opacity, colorSpace)
        return color
    }

    override fun createColor(
        value1: Double,
        value2: Double,
        value3: Double,
        opacity: Double,
        colorSpace: ColorSpace,
        colorOut: GenColor?
    ): GenColor {
        val color = colorOut ?: GenColor()
        color.set(value1, value2, value3, opacity, colorSpace)
        return color
    }

    override fun getColorSpace(color: GenColor): ColorSpace {
        return color.colorSpace
    }

    override fun mixColors(t: Double, a: GenColor, b: GenColor, colorSpace: ColorSpace, colorOut: GenColor?): GenColor {
        return colorSpace.mix(t, a, b, colorOut)
    }

    override fun setOpacity(color: GenColor, opacity: Double, colorOut: GenColor?): GenColor {
        val out = colorOut ?: GenColor()
        out.set(color)
        out.opacity = opacity
        return out
    }
}