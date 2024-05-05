package fyi.fyw.monutils.utils.math

import java.util.*
import kotlin.math.sqrt

/**
 * @author Wagyourtail
 * @since 1.2.6 [citation needed]
 */
open class Vec2D {
    var x1: Double
    var y1: Double
    var x2: Double
    var y2: Double

    constructor(x1: Double, y1: Double, x2: Double, y2: Double) {
        this.x1 = x1
        this.y1 = y1
        this.x2 = x2
        this.y2 = y2
    }

    constructor(start: Pos2D, end: Pos2D) {
        this.x1 = start.x
        this.y1 = start.y
        this.x2 = end.x
        this.y2 = end.y
    }

    val deltaX: Double
        get() = x2 - x1

    val deltaY: Double
        get() = y2 - y1

    open val start: Pos2D
        get() = Pos2D(x1, y1)

    open val end: Pos2D
        get() = Pos2D(x2, y2)

    open val magnitude: Double
        get() {
            val dx = x2 - x1
            val dy = y2 - y1
            return sqrt(dx * dx + dy * dy)
        }

    open val magnitudeSq: Double
        /**
         * @return magnitude squared
         * @since 1.6.5
         */
        get() {
            val dx = x2 - x1
            val dy = y2 - y1
            return dx * dx + dy * dy
        }

    fun add(vec: Vec2D): Vec2D {
        return Vec2D(x1 + vec.x1, y1 + vec.y1, x2 + vec.x2, y2 + vec.y2)
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     * @since 1.6.3
     */
    fun add(x1: Double, y1: Double, x2: Double, y2: Double): Vec2D {
        return Vec2D(this.x1 + x1, this.y1 + y1, this.x2 + x2, this.y2 + y2)
    }

    fun multiply(vec: Vec2D): Vec2D {
        return Vec2D(x1 * vec.x1, y1 * vec.y1, x2 * vec.x2, y2 * vec.y2)
    }

    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     * @since 1.6.3
     */
    fun multiply(x1: Double, y1: Double, x2: Double, y2: Double): Vec2D {
        return Vec2D(this.x1 * x1, this.y1 * y1, this.x2 * x2, this.y2 * y2)
    }

    /**
     * @param scale
     * @return
     * @since 1.6.3
     */
    open fun scale(scale: Double): Vec2D? {
        return Vec2D(x1 * scale, y1 * scale, x2 * scale, y2 * scale)
    }

    fun dotProduct(vec: Vec2D): Double {
        val dx1 = x2 - x1
        val dx2 = vec.x2 - vec.x1
        val dy1 = y2 - y1
        val dy2 = vec.y2 - vec.y1
        return dx1 * dx2 + dy1 * dy2
    }

    open fun reverse(): Vec2D? {
        return Vec2D(x2, y2, x1, y1)
    }

    /**
     * @return a new Vec2D with the same direction but a magnitude of 1
     * @since 1.6.5
     */
    open fun normalize(): Vec2D? {
        val mag = magnitude
        return Vec2D(x1 / mag, y1 / mag, x2 / mag, y2 / mag)
    }

    override fun toString(): String {
        return String.format("%f, %f -> %f, %f", x1, y1, x2, y2)
    }

    fun to3D(): Vec3D {
        return Vec3D(x1, y1, 0.0, x2, y2, 0.0)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val vec2D = o as Vec2D
        return x1.compareTo(vec2D.x1) == 0 && y1.compareTo(vec2D.y1) == 0 && x2.compareTo(vec2D.x2) == 0 && y2.compareTo(
            vec2D.y2
        ) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(x1, y1, x2, y2)
    }

    fun compareTo(other: Vec2D): Int {
        var i: Int = start.compareTo(other.start)
        if (i == 0) {
            i = end.compareTo(other.end)
        }
        return i
    }
}