package org.kwrench.color

import org.kwrench.color.colorspace.ColorSpace
import org.kwrench.color.colorspace.HSLuvColorSpace
import org.kwrench.color.colortype.ColorType
import org.kwrench.geometry.double3.Double3
import org.kwrench.random.Rand


/**
 * Creates a new random color with the specified min and max values for color components.
 * Defaults to a random color with opaque alpha.
 *
 * @param colorType color type specific object used to interface with that color type (e.g. javaFx color, awt color, any 3D library color, etc.)
 * @param minRed minimum value for red.  Clamped to 0..1 range.
 * @param maxRed maximum value for red.  Clamped to minRed .. 1 range.
 * @param minGreen minimum value for green.  Clamped to 0..1 range.
 * @param maxGreen maximum value for green.  Clamped to minGreen .. 1 range.
 * @param minBlue minimum value for blue.  Clamped to 0..1 range.
 * @param maxBlue maximum value for blue.  Clamped to minBlue .. 1 range.
 * @param minAlpha minimum value for alpha.  Clamped to 0..1 range.  Defaults to 1
 * @param maxAlpha maximum value for alpha.  Clamped to minAlpha..1 range.
 * @param colorOut color instance to reuse, or null if none.
 * @return a random color uniformly picked from the specified color space.
 */
fun <C: Any> Rand.nextColorRGB(colorType: ColorType<C>,
                               minRed: Double = 0.0,
                               maxRed: Double = 1.0,
                               minGreen: Double = 0.0,
                               maxGreen: Double = 1.0,
                               minBlue: Double = 0.0,
                               maxBlue: Double = 1.0,
                               minAlpha: Double = 1.0,
                               maxAlpha: Double = 1.0,
                               colorOut: C? = null): C =
    colorType.randomColorRGB(this, minRed, maxRed, minGreen, maxGreen, minBlue, maxBlue, minAlpha, maxAlpha, colorOut)


/**
 * Creates a new random color with the specified min and max values for hue-saturation-luminance components.
 * Defaults to a random color with opaque alpha.
 *
 * @param colorType color type specific object used to interface with that color type (e.g. javaFx color, awt color, any 3D library color, etc.)
 * @param minHue minimum value for hue.  If outside the 0..1 range, the result of the randomization will be wrapped around to the 0..1 range.
 * @param maxHue maximum value for hue.  If outside the 0..1 range, the result of the randomization will be wrapped around to the 0..1 range.
 * @param minSat minimum value for saturation.  Clamped to 0..1 range.
 * @param maxSat maximum value for saturation.  Clamped to minSat..1 range.
 * @param minLightness minimum value for lightness.  Clamped to 0..1 range.
 * @param maxLightness maximum value for lightness.  Clamped to minLum..1 range.
 * @param minOpacity minimum value for opacity.  Clamped to 0..1 range.  Defaults to 1
 * @param maxOpacity maximum value for opacity.  Clamped to minAlpha..1 range.
 * @param colorOut color instance to reuse, or null if none.
 * @return a random color uniformly picked from the specified color space.
 */
fun <C: Any> Rand.nextColor(colorType: ColorType<C>,
                            minValue: Double3,
                            maxValue: Double3,
                            minOpacity: Double = 1.0,
                            maxOpacity: Double = 1.0,
                            colorSpace: ColorSpace = HSLuvColorSpace,
                            colorOut: C? = null): C {
    val color = GenColor(colorSpace = colorSpace)
    color.setRandom(minValue, maxValue, minOpacity, maxOpacity, this)
    return color.toColor(colorType, colorOut)
}

/**
 * Creates a new random color with the specified min and max values for hue-saturation-luminance components.
 * Defaults to a random color with opaque alpha.
 *
 * @param colorType color type specific object used to interface with that color type (e.g. javaFx color, awt color, any 3D library color, etc.)
 * @param minHue minimum value for hue.  If outside the 0..1 range, the result of the randomization will be wrapped around to the 0..1 range.
 * @param maxHue maximum value for hue.  If outside the 0..1 range, the result of the randomization will be wrapped around to the 0..1 range.
 * @param minSat minimum value for saturation.  Clamped to 0..1 range.
 * @param maxSat maximum value for saturation.  Clamped to minSat..1 range.
 * @param minLightness minimum value for lightness.  Clamped to 0..1 range.
 * @param maxLightness maximum value for lightness.  Clamped to minLum..1 range.
 * @param minOpacity minimum value for opacity.  Clamped to 0..1 range.  Defaults to 1
 * @param maxOpacity maximum value for opacity.  Clamped to minAlpha..1 range.
 * @param colorOut color instance to reuse, or null if none.
 * @return a random color uniformly picked from the specified color space.
 */
fun <C: Any> Rand.nextColorGaussian(colorType: ColorType<C>,
                                    meanValue: Double3 = Double3.HALFS,
                                    meanOpacity: Double = 0.5,
                                    amplitude1: Double = 0.5,
                                    amplitude2: Double = 0.5,
                                    amplitude3: Double = 0.5,
                                    opacityAmplitude: Double = 0.5,
                                    colorSpace: ColorSpace = HSLuvColorSpace,
                                    colorOut: C? = null): C {
    val color = GenColor(colorSpace = colorSpace)
    color.set(meanValue, meanOpacity, colorSpace)
    color.addRandomGaussian(amplitude1, amplitude2, amplitude3, opacityAmplitude, this)
    return color.toColor(colorType, colorOut)
}


