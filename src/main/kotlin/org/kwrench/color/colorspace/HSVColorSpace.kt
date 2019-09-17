package org.kwrench.color.colorspace

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.clamp0To1
import org.kwrench.math.max
import org.kwrench.math.min
import org.kwrench.math.wrap0To1

/**
 * Hue, saturation, value color space.
 * See https://en.wikipedia.org/wiki/HSL_and_HSV
 */
object HSVColorSpace: HueSatLumColorSpace() {

    override fun toRGB(source: Double3, target: MutableDouble3) {
        val hue = source.x
        val sat = source.y
        val value = source.z

        val hueBand = 6.0 * hue.wrap0To1()
        val s = sat.clamp0To1()
        val v = value.clamp0To1()

        val chroma = v * s
        val smallestValue = v - chroma
        return createRGBColorUsingHueBand(hueBand, chroma, smallestValue, target)
    }

    override fun fromRGB(source: Double3, target: MutableDouble3) {
        target.x = getRainbowHue(source)
        target.y = getSaturationHSV(source)
        target.z = getValue(source)
    }

    /**
     * @return the (HSV) value of the specified color, 0 = black, 1 = bright/saturated.
     */
    fun getValue(rgb: Double3): Double {
        val r = rgb.x
        val g = rgb.y
        val b = rgb.z

        return max(r,g,b)
    }


    /**
     * @return the HSV saturation of the specified color, 0 = greyscale, 1 = fully saturated or darker when V approaches 0.
     */
    fun getSaturationHSV(rgb: Double3): Double {
        val r = rgb.x
        val g = rgb.y
        val b = rgb.z

        val value = max(r,g,b)
        val chroma = value - min(r,g,b)

        return if (value <= 0.0) 0.0 // Black
        else chroma / value
    }

}