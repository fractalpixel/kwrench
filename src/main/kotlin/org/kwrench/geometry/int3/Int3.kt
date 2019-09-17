package org.kwrench.geometry.int3

import org.kwrench.geometry.double2.MutableDouble2
import org.kwrench.geometry.double3.Double3
import org.kwrench.geometry.double3.MutableDouble3
import org.kwrench.geometry.int2.MutableInt2


/**
 * Simple 3D integer point / vector
 */
interface Int3 {

    val x: Int
    val y: Int
    val z: Int


    operator fun plus(other: Int3): MutableInt3 =
            MutableInt3(x + other.x,
                    y + other.y,
                    z + other.z)

    operator fun minus(other: Int3): MutableInt3 =
            MutableInt3(x - other.x,
                    y - other.y,
                    z - other.z)

    operator fun times(other: Int3): MutableInt3 =
            MutableInt3(x * other.x,
                    y * other.y,
                    z * other.z)

    operator fun div(other: Int3): MutableInt3 =
            MutableInt3(x / other.x,
                    y / other.y,
                    z / other.z)

    operator fun times(scalar: Int): MutableInt3 =
            MutableInt3(x * scalar,
                    y * scalar,
                    z * scalar)

    operator fun div(scalar: Int): MutableInt3 =
            MutableInt3(x / scalar,
                    y / scalar,
                    z / scalar)

    fun gridOrigo(gridSize: Int3): MutableInt3 {
        val v = floorDiv(gridSize)
        v.scale(gridSize)
        return v
    }

    fun floorDiv(other: Int3): MutableInt3 {
        fun floorDiv(v: Int, size: Int): Int = (v / size) - if (v < 0) 1 else 0
        return MutableInt3(floorDiv(x, other.x),
                floorDiv(y, other.y),
                floorDiv(z, other.z))
    }

    fun project(scale: Double3,
                offset: Double3,
                out: MutableDouble3 = MutableDouble3()
    ): MutableDouble3 {
        out.set(this)
        out.scale(scale)
        out.add(offset)
        return out
    }

    fun inRange(ends: Int3): Boolean = inRange(endX = ends.x,
                                               endY = ends.y,
                                               endZ = ends.z)

    fun inRange(starts: Int3, ends: Int3): Boolean = inRange(
            starts.x, starts.y, starts.z,
            ends.x, ends.y, ends.z)

    fun inRange(startX: Int = 0,
                startY: Int = 0,
                startZ: Int = 0,
                endX: Int = 0,
                endY: Int = 0,
                endZ: Int = 0): Boolean =
            x >= startX && x < endX &&
            y >= startY && y < endY &&
            z >= startZ && z < endZ


    fun scaleSum(xScale: Int = 1, yScale: Int = 1, zScale: Int = 1): Int = x * xScale + y * yScale + z * zScale

    fun multiplyAll(xOffset: Int = 0, yOffset: Int = 0, zOffset: Int = 0): Int = (x+xOffset) * (y+yOffset) * (z+zOffset)

    fun toInt2(): MutableInt2 = MutableInt2(x, y)
    fun toDouble2(): MutableDouble2 = MutableDouble2(x.toDouble(), y.toDouble())
    fun toDouble3(): MutableDouble3 = MutableDouble3(x.toDouble(), y.toDouble(), z.toDouble())

    /**
     * Get an index for the specified grid pos, assuming grid cells stored in a one dimensional array,
     * with z major, x minor order, or null if the specified position is outside this volume
     */
    fun toIndex(gridPos: Int3, gridSize: Int3): Int? {
        if (gridPos.x in 0 .. gridSize.x-1 &&
            gridPos.y in 0 .. gridSize.y-1 &&
            gridPos.z in 0 .. gridSize.z-1) {
            return gridPos.z * gridSize.x * gridSize.y +
                   gridPos.y * gridSize.x +
                   gridPos.x
        }
        else return null
    }

    companion object {
        val ZEROES = ImmutableInt3(0, 0, 0)
        val ONES = ImmutableInt3(1, 1, 1)
        val X_AXIS = ImmutableInt3(1, 0, 0)
        val Y_AXIS = ImmutableInt3(0, 1, 0)
        val Z_AXIS = ImmutableInt3(0, 0, 1)
    }
}

/*
fun Vector3.toInt3Rounded(): Int3 =
        Int3(MathUtils.round(this.x),
                MathUtils.round(this.y),
                MathUtils.round(this.z))

fun Vector3.toInt3Floored(): Int3 =
        Int3(MathUtils.fastFloor(this.x),
                MathUtils.fastFloor(this.y),
                MathUtils.fastFloor(this.z))
*/

