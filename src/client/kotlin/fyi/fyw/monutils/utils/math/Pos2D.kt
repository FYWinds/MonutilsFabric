package fyi.fyw.monutils.utils.math

import java.util.*


/**
 * @author Wagyourtail
 * @since 1.2.6 [citation needed]
 */
open class Pos2D(var x: Double, var y: Double) {
    fun add(pos: Pos2D): Pos2D {
        return Pos2D(x + pos.x, y + pos.y)
    }

    /**
     * @param x
     * @param y
     * @return
     * @since 1.6.3
     */
    fun add(x: Double, y: Double): Pos2D {
        return Pos2D(this.x + x, this.y + y)
    }

    /**
     * @param pos the position to subtract
     * @return the new position.
     * @since 1.8.4
     */
    fun sub(pos: Pos2D): Pos2D {
        return Pos2D(x - pos.x, y - pos.y)
    }

    /**
     * @param x the x coordinate to subtract
     * @param y the y coordinate to subtract
     * @return the new position.
     * @since 1.8.4
     */
    fun sub(x: Double, y: Double): Pos2D {
        return Pos2D(this.x - x, this.y - y)
    }

    fun multiply(pos: Pos2D): Pos2D {
        return Pos2D(x * pos.x, y * pos.y)
    }

    /**
     * @param x
     * @param y
     * @return
     * @since 1.6.3
     */
    fun multiply(x: Double, y: Double): Pos2D {
        return Pos2D(this.x * x, this.y * y)
    }

    /**
     * @param pos the position to divide by
     * @return the new position.
     * @since 1.8.4
     */
    fun divide(pos: Pos2D): Pos2D {
        return Pos2D(x / pos.x, y / pos.y)
    }

    /**
     * @param x the x coordinate to divide by
     * @param y the y coordinate to divide by
     * @return the new position.
     * @since 1.8.4
     */
    fun divide(x: Double, y: Double): Pos2D {
        return Pos2D(this.x / x, this.y / y)
    }

    /**
     * @param scale
     * @return
     * @since 1.6.3
     */
    open fun scale(scale: Double): Pos2D {
        return Pos2D(x * scale, y * scale)
    }

    override fun toString(): String {
        return String.format("%f, %f", x, y)
    }

    fun to3D(): Pos3D {
        return Pos3D(x, y, 0.0)
    }

    open fun toVector(): Vec2D {
        return Vec2D(ZERO, this)
    }

    /**
     * @param start_pos
     * @return
     * @since 1.6.4
     */
    open fun toVector(start_pos: Pos2D?): Vec2D {
        return Vec2D(start_pos!!, this)
    }

    /**
     * @param start_x
     * @param start_y
     * @return
     * @since 1.6.4
     */
    fun toVector(start_x: Double, start_y: Double): Vec2D {
        return Vec2D(start_x, start_y, this.x, this.y)
    }

    /**
     * @return
     * @since 1.6.4
     */
    open fun toReverseVector(): Vec2D {
        return Vec2D(this, ZERO)
    }

    /**
     * @param end_pos
     * @return
     * @since 1.6.4
     */
    open fun toReverseVector(end_pos: Pos2D?): Vec2D {
        return Vec2D(this, end_pos!!)
    }

    /**
     * @param end_x
     * @param end_y
     * @return
     * @since 1.6.4
     */
    fun toReverseVector(end_x: Double, end_y: Double): Vec2D {
        return Vec2D(this, Pos2D(end_x, end_y))
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val pos2D = o as Pos2D
        return x.compareTo(pos2D.x) == 0 && y.compareTo(pos2D.y) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(x, y)
    }

    fun compareTo(o: Pos2D): Int {
        var i = x.compareTo(o.x)
        if (i == 0) {
            i = y.compareTo(o.y)
        }
        return i
    }

    companion object {
        val ZERO: Pos2D = Pos2D(0.0, 0.0)
    }
}