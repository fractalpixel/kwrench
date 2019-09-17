package org.kwrench.color.colortype

import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.math.clamp0To1
import java.awt.Color

/**
 * Swing / AWT specific color utilities.
 */
object AwtColorType: ColorType<Color>() {

    fun getRed(color: Color): Double = color.red / 255.0
    fun getGreen(color: Color): Double = color.green / 255.0
    fun getBlue(color: Color): Double = color.blue / 255.0
    override fun getOpacity(color: Color): Double = color.alpha / 255.0

    override fun getColorValues(color: Color, out: MutableDouble3): Double3 {
        out.x = getRed(color)
        out.y = getGreen(color)
        out.z = getBlue(color)
        return out
    }

    override fun createColorRGB(red: Double, green: Double, blue: Double, alpha: Double, colorOut: Color?): Color {
        // Swing/AWT colors are also immutable, so ignore colorOut and create a new instance.
        // There doesn't seem to be range checking, so clamp values to range.
        return Color(
            red.clamp0To1().toFloat(),
            green.clamp0To1().toFloat(),
            blue.clamp0To1().toFloat(),
            alpha.clamp0To1().toFloat())
    }
}