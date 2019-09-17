package org.kwrench.color.colorspace

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.Tau
import org.kwrench.math.fastFloor
import org.kwrench.math.max
import org.kwrench.math.min

/**
 * Common routines for color spaces
 */
abstract class ColorSpaceBase: ColorSpace {

    /**
     * @param hueBand hue in a range from 0 to 6
     * @param chroma chroma of the color
     * @param smallestValue smallest color component (basically common lightness)
     */
    protected fun createRGBColorUsingHueBand(
        hueBand: Double,
        chroma: Double,
        smallestValue: Double,
        out: MutableDouble3) {

        val c = chroma + smallestValue
        val x = chroma * (1.0 - Math.abs((hueBand % 2.0) - 1.0)) + smallestValue
        val m = smallestValue

        val r: Double
        val g: Double
        val b: Double
        val hueBandInt = hueBand.fastFloor()
        when (hueBandInt) {
            0 -> {
                r = c
                g = x
                b = m
            }
            1 -> {
                r = x
                g = c
                b = m
            }
            2 -> {
                r = m
                g = c
                b = x
            }
            3 -> {
                r = m
                g = x
                b = c
            }
            4 -> {
                r = x
                g = m
                b = c
            }
            5 -> {
                r = c
                g = m
                b = x
            }
            else -> throw IllegalStateException("Unexpected hue band '$hueBandInt' when converting colors")
        }

        out.x = r
        out.y = g
        out.z = b
    }

    /**
     * @return the hue of the specified color, from 0 to 1, where red is at 0 (and 1).
     * Color positions:
     * red = 0, Yellow = 0.166, Green = 0.333, cyan = 0.5, blue = 0.666, magenta: 0.833.
     */
    protected fun getRainbowHue(rgb: Double3): Double {
        val r = rgb.x
        val g = rgb.y
        val b = rgb.z

        val maxC = max(r,g,b)
        val minC = min(r,g,b)

        val chroma = maxC - minC

        if (chroma <= 0.0) return 0.0 // Hardcode no color as red

        var hue = when (maxC) {
            r -> {
                (g - b) / chroma
            }
            g -> {
                (b - r) / chroma + 2.0
            }
            else -> { // Max color is blue
                (r - g) / chroma + 4.0
            }
        }

        // Scale to 0..1
        hue /= 6.0f

        // Wrap to 0..1
        if (hue < 0) hue += 1.0
        else if (hue > 1.0) hue -= 1.0

        return hue
    }

    /**
     * Assumes the first color coordinate is circular, and the second one is radial,
     * and the third one is independent, and does mixing using this assumption.
     * Useful for most color spaces containing a circular hue.
     */
    protected fun mixPolarHueSat(
        t: Double,
        a: Double3,
        b: Double3,
        out: MutableDouble3
    ) {
        // Lightness is straightforward
        val l = org.kwrench.math.mix(t, a.z, b.z)

        // Hue and saturation form a polar coordinate system, convert them to cartesian for interpolation
        val a1 = a.x * Tau
        val u1 = a.y * Math.cos(a1)
        val v1 = a.y * Math.sin(a1)

        val a2 = b.x * Tau
        val u2 = b.y * Math.cos(a2)
        val v2 = b.y * Math.sin(a2)

        // Interpolate hue and saturation
        val u = org.kwrench.math.mix(t, u1, u2)
        val v = org.kwrench.math.mix(t, v1, v2)

        // Convert back to polar
        val r = Math.sqrt(u * u + v * v)
        val a = Math.atan2(v, u) / Tau

        out.x = a // Hue
        out.y = r // Saturation
        out.z = l // Lightness
    }

}