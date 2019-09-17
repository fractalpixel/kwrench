package org.kwrench.color.colortype

import org.kwrench.color.colorspace.ColorSpace
import org.kwrench.color.colorspace.HSLuvColorSpace
import org.kwrench.color.colorspace.RGBColorSpace
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.interpolation.ValueMixer
import org.kwrench.math.clamp0To1
import org.kwrench.math.clampTo
import org.kwrench.math.mix
import org.kwrench.random.Rand

/**
 * Color related utilities for interoperability.
 * Implement this as an object for each color type supported (javafx, awt, any 3D engines...)
 */
abstract class ColorType<C: Any>: ValueMixer<C> {

    /**
     * Return color values, regardless of color space.
     */
    abstract fun getColorValues(color: C, out: MutableDouble3 = MutableDouble3()): Double3

    /**
     * The color space that the specified color is expressed in.
     * Defaults to sRGB
     */
    open fun getColorSpace(color: C): ColorSpace = RGBColorSpace

    /**
     * Get color values in the specified color space.
     */
    fun getColorValues(color: C, targetColorSpace: ColorSpace, out: MutableDouble3 = MutableDouble3()): Double3 {
        val colorValues = getColorValues(color, out)
        targetColorSpace.fromColorSpace(colorValues, out, getColorSpace(color))
        return out
    }

    /**
     * @return the opacity (or alpha) component of the color, in the 0..1 range, where 0 is transparent and 1 is opaque.
     */
    abstract fun getOpacity(color: C): Double


    /**
     * Get color values in RGB color space
     */
    fun getRGB(color: C, out: MutableDouble3 = MutableDouble3()): Double3 = getColorValues(color, RGBColorSpace, out)

    /**
     * Get color values in HSLuv color space
     */
    fun getHSLuv(color: C, out: MutableDouble3 = MutableDouble3()): Double3 = getColorValues(color, HSLuvColorSpace, out)

    /**
     * Alias for [getOpacity].
     */
    fun getAlpha(color: C): Double = getOpacity(color)


    /**
     * Creates a new color (or tries to change an existing color instance)
     * with the specified red, green, blue and opacity values.
     * The values of the color components should be in the 0..1 range (if outside,
     * this method should clamp them to that range before creating the color).
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     */
    abstract fun createColorRGB(red: Double,
                                green: Double,
                                blue: Double,
                                alpha: Double,
                                colorOut: C? = null): C

    /**
     * Creates a new color (or tries to change an existing color instance)
     * with the specified red, green, blue and opacity values.
     * The values of the color components should be in the 0..1 range (if outside,
     * this method should clamp them to that range before creating the color).
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     */
    open fun createColorRGB(rgb: Double3,
                            alpha: Double,
                            colorOut: C? = null): C {
        return createColorRGB(
            rgb.x.clamp0To1(),
            rgb.y.clamp0To1(),
            rgb.z.clamp0To1(),
            alpha.clamp0To1(),
            colorOut)
    }

    /**
     * Creates a new color (or tries to change an existing color instance)
     * with the specified [values] in the specified [colorSpace]
     * The values of the color components should be in the 0..1 range (if outside,
     * this method should clamp them to that range before creating the color).
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     */
    open fun createColor(values: Double3,
                         opacity: Double,
                         colorSpace: ColorSpace,
                         colorOut: C? = null): C {
        val temp = MutableDouble3()
        colorSpace.toRGB(values, temp)
        return createColorRGB(
            temp,
            opacity,
            colorOut)
    }

    /**
     * Creates a new color (or tries to change an existing color instance)
     * with the specified values in the specified [colorSpace]
     * The values of the color components should be in the 0..1 range (if outside,
     * this method should clamp them to that range before creating the color).
     *
     * @param value1 the first color components to assign to the new color.
     *               (in an rgb color space this is red, in a hue sat lum color space this is hue).
     * @param value2 the second color components to assign to the new color.
     *               (in an rgb color space this is green, in a hue sat lum color space this is saturation).
     * @param value3 the third color components to assign to the new color.
     *               (in an rgb color space this is blue, in a hue sat lum color space this is luminance).
     * @param colorSpace the color space the values are given in.
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     */
    open fun createColor(value1: Double,
                         value2: Double,
                         value3: Double,
                         opacity: Double,
                         colorSpace: ColorSpace,
                         colorOut: C? = null): C {
        return createColor(MutableDouble3(
            value1.clamp0To1(),
            value2.clamp0To1(),
            value3.clamp0To1()), opacity, colorSpace, colorOut)
    }

    /**
     * Mixes two colors in the specified [colorSpace] (defaults to sRGB).
     * @param mixAmount if 0, returns a, if 1, returns b, else returns a new color that is a mix of a and b.
     *                  The resulting color components are clamped to the 0..255 range, but the mixAmount can be negative or over 1 as well.
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     */
    open fun mixColors(mixAmount: Double, a: C, b: C,
                  colorSpace: ColorSpace  = RGBColorSpace,
                  colorOut: C? = null): C {

        val aValues = getColorValues(a, colorSpace)
        val bValues = getColorValues(b, colorSpace)

        val temp = MutableDouble3()
        colorSpace.mix(mixAmount, aValues, bValues, temp)
        val mixedAlpha = mix(mixAmount, getAlpha(a), getAlpha(b))
        return createColor(temp, mixedAlpha, colorSpace, colorOut)
    }

    // Implementation of ValueMixer for C.  Implement more efficient versions of this in implementations..
    override fun mix(t: Double, a: C, b: C, out: C?): C {
        return mixColors(t, a, b, RGBColorSpace, out)
    }

    /**
     * Creates a new random color with the specified min and max values for color components.
     * Defaults to a random color with opaque alpha.
     *
     * @param rand the source for randomness.
     * @param minRed minimum value for red.  Clamped to 0..1 range.
     * @param maxRed maximum value for red.  Clamped to minRed .. 1 range.
     * @param minGreen minimum value for green.  Clamped to 0..1 range.
     * @param maxGreen maximum value for green.  Clamped to minGreen .. 1 range.
     * @param minBlue minimum value for blue.  Clamped to 0..1 range.
     * @param maxBlue maximum value for blue.  Clamped to minBlue .. 1 range.
     * @param minAlpha minimum value for alpha.  Clamped to 0..1 range.  Defaults to 1
     * @param maxAlpha maximum value for alpha.  Clamped to minAlpha..1 range.
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     * @return a random color uniformly picked from the specified color space.
     */
    fun randomColorRGB(rand: Rand,
                       minRed: Double = 0.0,
                       maxRed: Double = 1.0,
                       minGreen: Double = 0.0,
                       maxGreen: Double = 1.0,
                       minBlue: Double = 0.0,
                       maxBlue: Double = 1.0,
                       minAlpha: Double = 1.0,
                       maxAlpha: Double = 1.0,
                       colorOut: C? = null): C {

        val minR = minRed.clamp0To1()
        val minG = minGreen.clamp0To1()
        val minB = minBlue.clamp0To1()
        val minA = minAlpha.clamp0To1()

        val red = rand.nextDouble(minR, maxRed.clampTo(minR, 1.0))
        val green = rand.nextDouble(minG, maxGreen.clampTo(minG, 1.0))
        val blue = rand.nextDouble(minB, maxBlue.clampTo(minB, 1.0))
        val alpha = rand.nextDouble(minA, maxAlpha.clampTo(minA, 1.0))

        return createColorRGB(red, green, blue, alpha, colorOut)
    }


    /**
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     * @return a new color with the specified opacity, and the solid color of this color.
     */
    open fun setOpacity(color: C, opacity: Double, colorOut: C? = null): C {
        val values = getColorValues(color)
        return createColorRGB(
            values,
            opacity.clamp0To1(),
            colorOut)
    }

    /**
     * Set the color values but keep the opacity of the specified color.
     *
     * @param values the color components to assign to the new color.
     * @param colorSpace the color space that the values are given in, defaults to RGB
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     * @return a new color with the specified color values
     */
    open fun setValues(color: C, values: Double3, colorSpace: ColorSpace = RGBColorSpace, colorOut: C? = null): C {
        return createColor(values, getAlpha(color), colorSpace, colorOut)
    }

    /**
     * Set the color values but keep the opacity of the specified color.
     *
     * @param value1 the first color components to assign to the new color.
     *               (in an rgb color space this is red, in a hue sat lum color space this is hue).
     * @param value2 the second color components to assign to the new color.
     *               (in an rgb color space this is green, in a hue sat lum color space this is saturation).
     * @param value3 the third color components to assign to the new color.
     *               (in an rgb color space this is blue, in a hue sat lum color space this is luminance).
     * @param colorSpace the color space that the values are given in, defaults to RGB
     * @param colorOut color instance to reuse, or null if a new color instance should be created and returned.  Not all color types support editable colors.
     * @return a new color with the specified color values
     */
    open fun setValues(color: C, value1: Double, value2: Double, value3: Double, colorSpace: ColorSpace = RGBColorSpace, colorOut: C? = null): C {
        return createColor(
            value1,
            value2,
            value3,
            getAlpha(color),
            colorSpace,
            colorOut)
    }



}





