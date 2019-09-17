package org.kwrench.geometry.int2

import org.kwrench.geometry.double2.Double2
import org.kwrench.geometry.double2.MutableDouble2
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.geometry.int3.MutableInt3

/**
 * Simple 2D integer point / vector
 */
interface Int2 {

    val x: Int
    val y: Int

    operator fun plus(other: Int2): MutableInt2 =
            MutableInt2(x + other.x,
                        y + other.y)

    operator fun minus(other: Int2): MutableInt2 =
            MutableInt2(x - other.x,
                        y - other.y)

    operator fun times(other: Int2): MutableInt2 =
            MutableInt2(x * other.x,
                        y * other.y)

    operator fun div(other: Int2): MutableInt2 =
            MutableInt2(x / other.x,
                        y / other.y)

    operator fun times(scalar: Int): MutableInt2 =
            MutableInt2(x * scalar,
                        y * scalar)

    operator fun div(scalar: Int): MutableInt2 =
            MutableInt2(x / scalar,
                        y / scalar)

    /**
     * Can be used to calculate the grid cell coordinates for a point, given the grid size.
     */
    fun floorDiv(other: Int2): MutableInt2 {
        fun floorDiv(v: Int, size: Int): Int = (v / size) - if (v < 0) 1 else 0
        return MutableInt2(floorDiv(x, other.x),
                           floorDiv(y, other.y))
    }

    /**
     * Can be used to calculate the origin of the grid cell that a point is in, given the grid size.
     */
    fun gridOrigo(gridSize: Int2): MutableInt2 {
        val v = floorDiv(gridSize)
        v.scale(gridSize)
        return v
    }

    fun project(scale: Double2,
                offset: Double2,
                out: MutableDouble2 = MutableDouble2()
    ): MutableDouble2 {
        out.set(this)
        out.scale(scale)
        out.add(offset)
        return out
    }

    fun inRange(ends: Int2): Boolean = inRange(endX = ends.x,
                                               endY = ends.y)

    fun inRange(starts: Int2, ends: Int2): Boolean = inRange(
            starts.x, starts.y,
            ends.x, ends.y)

    fun inRange(startX: Int = 0,
                startY: Int = 0,
                endX: Int = 0,
                endY: Int = 0): Boolean =
            x >= startX && x < endX &&
            y >= startY && y < endY

    fun scaleSum(xScale: Int = 1, yScale: Int = 1): Int = x * xScale + y * yScale

    fun multiplyAll(xOffset: Int = 0, yOffset: Int = 0): Int = (x+xOffset) * (y+yOffset)

    fun toInt3(z: Int = 0): MutableInt3 = MutableInt3(x, y, z)
    fun toDouble2(): MutableDouble2 = MutableDouble2(x.toDouble(), y.toDouble())
    fun toDouble3(z: Double = 0.0): MutableDouble3 = MutableDouble3(x.toDouble(), y.toDouble(), z)


    /**
     * Get an index for the specified grid pos, assuming grid cells stored in a one dimensional array,
     * with z major, x minor order, or null if the specified position is outside this volume
     */
    fun toIndex(gridPos: Int2, gridSize: Int2): Int? {
        if (gridPos.x in 0 .. gridSize.x-1 &&
            gridPos.y in 0 .. gridSize.y-1) {
            return gridPos.y * gridSize.x +
                   gridPos.x
        }
        else return null
    }


    companion object {
        val ZEROES = ImmutableInt2(0, 0)
        val ONES = ImmutableInt2(1, 1)
        val X_AXIS = ImmutableInt2(1, 0)
        val Y_AXIS = ImmutableInt2(0, 1)
    }

}