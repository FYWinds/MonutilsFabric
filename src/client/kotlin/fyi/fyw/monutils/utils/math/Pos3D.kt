package fyi.fyw.monutils.utils.math

import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import java.util.*

/**
 * @author Wagyourtail
 * @since 1.2.6 [citation needed]
 */
class Pos3D(x: Double, y: Double, var z: Double) : Pos2D(x, y) {
    constructor(vec: Vec3d) : this(vec.getX(), vec.getY(), vec.getZ())

    fun add(pos: Pos3D): Pos3D {
        return Pos3D(x + pos.x, y + pos.y, z + pos.z)
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return
     * @since 1.6.3
     */
    fun add(x: Double, y: Double, z: Double): Pos3D {
        return Pos3D(this.x + x, this.y + y, this.z + z)
    }

    /**
     * @param pos the position to subtract
     * @return the new position.
     * @since 1.8.4
     */
    fun sub(pos: Pos3D): Pos3D {
        return Pos3D(x - pos.x, y - pos.y, z - pos.z)
    }

    /**
     * @param x the x coordinate to subtract
     * @param y the y coordinate to subtract
     * @param z the z coordinate to subtract
     * @return the new position.
     * @since 1.8.4
     */
    fun sub(x: Double, y: Double, z: Double): Pos3D {
        return Pos3D(this.x - x, this.y - y, this.z - z)
    }

    fun multiply(pos: Pos3D): Pos3D {
        return Pos3D(x * pos.x, y * pos.y, z * pos.z)
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return
     * @since 1.6.3
     */
    fun multiply(x: Double, y: Double, z: Double): Pos3D {
        return Pos3D(this.x * x, this.y * y, this.z * z)
    }

    /**
     * @param pos the position to divide by
     * @return the new position.
     * @since 1.8.4
     */
    fun divide(pos: Pos3D): Pos3D {
        return Pos3D(x / pos.x, y / pos.y, z / pos.z)
    }

    /**
     * @param x the x coordinate to divide by
     * @param y the y coordinate to divide by
     * @param z the z coordinate to divide by
     * @return the new position.
     * @since 1.8.4
     */
    fun divide(x: Double, y: Double, z: Double): Pos3D {
        return Pos3D(this.x / x, this.y / y, this.z / z)
    }

    /**
     * @param scale
     * @return
     * @since 1.6.3
     */
    override fun scale(scale: Double): Pos3D {
        return Pos3D(x * scale, y * scale, z * scale)
    }

    override fun toString(): String {
        return String.format("%f, %f, %f", x, y, z)
    }

    override fun toVector(): Vec3D {
        return Vec3D(ZERO, this)
    }

    /**
     * @param start_pos
     * @return
     * @since 1.6.4
     */
    override fun toVector(start_pos: Pos2D?): Vec3D {
        return toVector(start_pos!!.to3D())
    }

    /**
     * @param start_pos
     * @return
     * @since 1.6.4
     */
    fun toVector(start_pos: Pos3D?): Vec3D {
        return Vec3D(start_pos!!, this)
    }

    /**
     * @param start_x
     * @param start_y
     * @param start_z
     * @return
     * @since 1.6.4
     */
    fun toVector(start_x: Double, start_y: Double, start_z: Double): Vec3D {
        return Vec3D(start_x, start_y, start_z, this.x, this.y, this.z)
    }

    /**
     * @return
     * @since 1.6.4
     */
    override fun toReverseVector(): Vec3D {
        return Vec3D(this, ZERO)
    }

    override fun toReverseVector(end_pos: Pos2D?): Vec3D {
        return toReverseVector(end_pos!!.to3D())
    }

    /**
     * @param end_pos
     * @return
     * @since 1.6.4
     */
    fun toReverseVector(end_pos: Pos3D?): Vec3D {
        return Vec3D(this, end_pos!!)
    }

    /**
     * @param end_x
     * @param end_y
     * @param end_z
     * @return
     * @since 1.6.4
     */
    fun toReverseVector(end_x: Double, end_y: Double, end_z: Double): Vec3D {
        return Vec3D(this, Pos3D(end_x, end_y, end_z))
    }

    /**
     * @return
     * @since 1.8.0
     */
    fun toRawBlockPos(): BlockPos {
        return BlockPos.ofFloored(x, y, z)
    }

    /**
     * @return the raw minecraft double vector with the same coordinates as this position.
     * @since 1.8.4
     */
    fun toMojangDoubleVector(): Vec3d {
        return Vec3d(x, y, z)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val pos3D = o as Pos3D
        return x.compareTo(pos3D.x) == 0 && y.compareTo(pos3D.y) == 0 && z.compareTo(pos3D.z) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), z)
    }

    fun compareTo(o: Pos3D): Int {
        var i = super.compareTo(o)
        if (i == 0) {
            i = z.compareTo(o.z)
        }
        return i
    }

    companion object {
        val ZERO: Pos3D = Pos3D(0.0, 0.0, 0.0)
    }
}