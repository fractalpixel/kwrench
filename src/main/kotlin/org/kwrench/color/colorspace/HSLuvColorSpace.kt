package org.kwrench.color.colorspace

import org.hsluv.HUSLColorConverter
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3

/**
 * Hue, saturation, luminance, with perception based color mapping that
 * clearly separates the three concepts.
 *
 * This is recommended for any visualization purposes, but is a bit slower to compute.
 *
 * Uses https://github.com/hsluv/hsluv-java
 *
 * See http://www.hsluv.org/ and http://hclwizard.org/why-hcl/
 */
object HSLuvColorSpace: HueSatLumColorSpace() {
    override fun toRGB(source: Double3, target: MutableDouble3) {
        val srcArray = doubleArrayOf(source.x, source.y, source.z)
        val outArray = HUSLColorConverter.hsluvToRgb(srcArray)
        target.x = outArray[0]
        target.y = outArray[1]
        target.z = outArray[2]
    }

    override fun fromRGB(source: Double3, target: MutableDouble3) {
        val srcArray = doubleArrayOf(source.x, source.y, source.z)
        val outArray = HUSLColorConverter.rgbToHsluv(srcArray)
        target.x = outArray[0]
        target.y = outArray[1]
        target.z = outArray[2]
    }
}