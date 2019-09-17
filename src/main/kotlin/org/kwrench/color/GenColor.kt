package org.kwrench.color

import org.kwrench.color.colorspace.ColorSpace
import org.kwrench.color.colorspace.HSLuvColorSpace
import org.kwrench.color.colorspace.HueSatLumColorSpace
import org.kwrench.color.colorspace.RGBColorSpace
import org.kwrench.color.colortype.AwtColorType
import org.kwrench.color.colortype.ColorType
import org.kwrench.color.colortype.GenColorType
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.clamp0To1
import org.kwrench.random.Rand

/**
 * Combines three mutable color coordinates, an alpha value, and a color space.
 *
 * Provides conversion routines to RGB as well as to specific types of colors.
 *
 * Aims to be interoperable with different color spaces, but still implement a full set of color functionality,
 * including a high quality alternative to HSL color space.
 *
 * [https://xkcd.com/927/]
 */
class GenColor(initialValues: Double3 = Double3.ZEROES,
               var opacity: Double = 1.0,
               colorSpace: ColorSpace = RGBColorSpace) {

    constructor(
        v1: Double,
        v2: Double,
        v3: Double,
        opacity: Double,
        colorSpace: ColorSpace = RGBColorSpace
    ) :
            this(MutableDouble3(v1, v2, v3), opacity, colorSpace)

    var values = MutableDouble3(initialValues)

    var colorSpace: ColorSpace = colorSpace
        private set

    fun setColorSpace(newColorSpace: ColorSpace) {
        if (colorSpace != newColorSpace) {
            // Convert using existing color space to rgb:
            colorSpace.toRGB(values, values)

            // and then to the destination color space:
            newColorSpace.fromRGB(values, values)

            colorSpace = newColorSpace
        }
    }

    fun set(other: GenColor) {
        this.colorSpace = other.colorSpace
        this.values.set(other.values)
        this.opacity = other.opacity
    }

    fun set(values: Double3 = Double3.ZEROES,
            opacity: Double = 1.0,
            colorSpace: ColorSpace = this.colorSpace) {
        set(values.x, values.y, values.z, opacity, colorSpace)
    }

    fun set(value1: Double = 0.0,
            value2: Double = 0.0,
            value3: Double = 0.0,
            opacity: Double = 1.0,
            colorSpace: ColorSpace = this.colorSpace) {
        this.colorSpace = colorSpace
        this.values.set(value1, value2, value3)
        this.opacity = opacity

        colorSpace.clampValues(this.values)
        this.opacity = this.opacity.clamp0To1()
    }

    /**
     * Set this color from the specified other color type.
     */
    fun set(color: java.awt.Color) {
        setFromColor(color, AwtColorType)
    }


    fun getRGB(rgbOut: MutableDouble3 = MutableDouble3()): MutableDouble3 {
        colorSpace.toRGB(values, rgbOut)
        return rgbOut
    }

    fun setRGB(rgb: Double3) {
        colorSpace.fromRGB(rgb, values)
    }

    fun getHSLuv(hslOut: MutableDouble3 = MutableDouble3(),
                 hslColorSpace: HueSatLumColorSpace = HSLuvColorSpace): MutableDouble3 {
        colorSpace.toColorSpace(values, hslOut, hslColorSpace)
        return hslOut
    }

    fun setHSLuv(hsl: Double3, hslColorSpace: HueSatLumColorSpace = HSLuvColorSpace) {
        colorSpace.fromColorSpace(hsl, values, hslColorSpace)
    }

    fun <C: Any>toColor(colorType: ColorType<C>,
                        colorOut: C? = null): C {
        // Check if we want a mist color
        if (colorType == GenColorType) return this as C

        return colorType.createColorRGB(getRGB(), opacity, colorOut)
    }

    fun <C: Any>setFromColor(color: C, colorType: ColorType<C>) {
        opacity = colorType.getOpacity(color)
        colorType.getColorValues(color, values)
        colorSpace.fromRGB(values, values)
    }

    fun setToMixed(t: Double, a: GenColor, b: GenColor) {
        colorSpace.mix(t, a, b, this)
    }

    fun mixWith(t: Double, other: GenColor) {
        colorSpace.mix(t, this, other, this)
    }

    fun setRandom(minValues: Double3 = Double3.ZEROES,
                  maxValues: Double3 = Double3.ONES,
                  minOpacity: Double = 0.0,
                  maxOpacity: Double = 1.0,
                  random: Rand = Rand.default) {
        values.set(
            random.nextDouble(minValues.x, maxValues.x),
            random.nextDouble(minValues.y, maxValues.y),
            random.nextDouble(minValues.z, maxValues.z)
        )
        opacity = random.nextDouble(minOpacity, maxOpacity)

        colorSpace.clampValues(values)
        opacity = opacity.clamp0To1()
    }

    fun addRandomGaussian(amplitude1: Double = 0.5,
                          amplitude2: Double = 0.5,
                          amplitude3: Double = 0.5,
                          opacityAmplitude: Double = 0.5,
                          random: Rand = Rand.default) {
        values.x += amplitude1 * random.nextGaussian()
        values.y += amplitude2 * random.nextGaussian()
        values.z += amplitude3 * random.nextGaussian()
        opacity  += opacityAmplitude * random.nextGaussian()

        colorSpace.clampValues(values)
        opacity = opacity.clamp0To1()
    }

}


