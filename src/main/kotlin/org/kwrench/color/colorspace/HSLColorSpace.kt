package org.kwrench.color.colorspace

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.*

/**
 * Hue Saturation Lightness color space.
 * See https://en.wikipedia.org/wiki/HSL_and_HSV
 */
object HSLColorSpace: HueSatLumColorSpace() {

    override fun toRGB(source: Double3, target: MutableDouble3) {
        val hue = source.x
        val sat = source.y
        val lightness = source.z

        val hueBand = 6.0 * hue.wrap0To1()
        val s = sat.clamp0To1()
        val l = lightness.clamp0To1()

        val chroma = (1.0 - Math.abs(2.0 * l - 1)) * s
        val smallestValue = l - chroma / 2.0
        return createRGBColorUsingHueBand(hueBand, chroma, smallestValue, target)
    }

    override fun fromRGB(source: Double3, target: MutableDouble3) {
        target.x = getRainbowHue(source)
        target.y = getSaturationHSL(source)
        target.z = getLightness(source)
    }

    /**
     * @return the HSL saturation of the specified color, 0 = greyscale, 1 = fully saturated.
     */
    fun getSaturationHSL(rgb: Double3): Double {
        val r = rgb.x
        val g = rgb.y
        val b = rgb.z

        val maxComponent = max(r, g, b)
        val minComponent = min(r, g, b)
        val chroma = maxComponent - minComponent
        val lightness = average(minComponent, maxComponent)

        return if (lightness <= 0.0 || lightness >= 1.0) 0.0 // Full black or white
        else chroma / (1.0 - Math.abs(2.0 * lightness - 1.0)) // See https://en.wikipedia.org/wiki/HSL_and_HSV for formula
    }

    /**
     * @return the (HSL) lightness of the specified color, 0 = black, 1 = white.
     */
    fun getLightness(rgb: Double3): Double {
        val r = rgb.x
        val g = rgb.y
        val b = rgb.z

        return average(min(r,g,b), max(r,g,b))
    }




}