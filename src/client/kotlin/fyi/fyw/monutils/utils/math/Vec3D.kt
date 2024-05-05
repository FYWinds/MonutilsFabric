package fyi.fyw.monutils.utils.math

import net.minecraft.util.math.MathHelper
import org.joml.Vector3f
import java.util.*
import kotlin.math.atan2
import kotlin.math.sqrt


/**
 * @author Wagyourtail
 * @since 1.2.6 [citation needed]
 */
class Vec3D : Vec2D {
    var z1: Double
    var z2: Double

    constructor(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) : super(x1, y1, x2, y2) {
        this.z1 = z1
        this.z2 = z2
    }

    constructor(start: Pos3D, end: Pos3D) : super(start, end) {
        this.z1 = start.z
        this.z2 = end.z
    }

    val deltaZ: Double
        get() = z2 - z1

    override val start: Pos3D
        get() = Pos3D(x1, y1, z1)

    override val end: Pos3D
        get() = Pos3D(x2, y2, z2)

    override val magnitude: Double
        get() {
            val dx: Double = x2 - x1
            val dy: Double = y2 - y1
            val dz = z2 - z1
            return sqrt(dx * dx + dy * dy + dz * dz)
        }

    override val magnitudeSq: Double
        get() {
            val dx: Double = x2 - x1
            val dy: Double = y2 - y1
            val dz = z2 - z1
            return dx * dx + dy * dy + dz * dz
        }

    fun add(vec: Vec3D): Vec3D {
        return Vec3D(
            this.x1 + vec.x1,
            this.y1 + vec.y1,
            this.z1 + vec.z1,
            this.x2 + vec.x2,
            this.y2 + vec.y2,
            this.z2 + vec.z2
        )
    }

    /**
     * @param pos
     * @return
     * @since 1.6.4
     */
    fun addStart(pos: Pos3D): Vec3D {
        return Vec3D(this.x1 + pos.x, this.y1 + pos.y, this.z1 + pos.z, this.x2, this.y2, this.z2)
    }

    /**
     * @param pos
     * @return
     * @since 1.6.4
     */
    fun addEnd(pos: Pos3D): Vec3D {
        return Vec3D(this.x1, this.y1, this.z1, this.x2 + pos.x, this.y2 + pos.y, this.z2 + pos.z)
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return
     * @since 1.6.4
     */
    fun addStart(x: Double, y: Double, z: Double): Vec3D {
        return Vec3D(this.x1 + x, this.y1 + y, this.z1 + z, this.x2, this.y2, this.z2)
    }

    /**
     * @param x
     * @param y
     * @param z
     * @return
     * @since 1.6.4
     */
    fun addEnd(x: Double, y: Double, z: Double): Vec3D {
        return Vec3D(this.x1, this.y1, this.z1, this.x2 + x, this.y2 + y, this.z2 + z)
    }

    /**
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return
     * @since 1.6.3
     */
    fun add(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Vec3D {
        return Vec3D(this.x1 + x1, this.y1 + y1, this.z1 + z1, this.x2 + x2, this.y2 + y2, this.z2 + z2)
    }

    fun multiply(vec: Vec3D): Vec3D {
        return Vec3D(
            this.x1 * vec.x1,
            this.y1 * vec.y1,
            this.z1 * vec.z1,
            this.x2 * vec.x2,
            this.y2 * vec.y2,
            this.z2 * vec.z2
        )
    }

    /**
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @return
     * @since 1.6.3
     */
    fun multiply(x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double): Vec3D {
        return Vec3D(this.x1 * x1, this.y1 * y1, this.z1 * z1, this.x2 * x2, this.y2 * y2, this.z2 * z2)
    }

    /**
     * @param scale
     * @return
     * @since 1.6.3
     */
    override fun scale(scale: Double): Vec3D {
        return Vec3D(x1 * scale, y1 * scale, z1 * scale, x2 * scale, y2 * scale, z2 * scale)
    }

    /**
     * @return
     * @since 1.6.5
     */
    override fun normalize(): Vec3D {
        val mag = magnitude
        return Vec3D(x1 / mag, y1 / mag, z1 / mag, x2 / mag, y2 / mag, z2 / mag)
    }

    val pitch: Float
        get() {
            val dx: Double = x2 - x1
            val dy: Double = y2 - y1
            val dz = z2 - z1
            val xz = sqrt(dx * dx + dz * dz)
            return 90f - MathHelper.wrapDegrees(Math.toDegrees(atan2(xz, -dy))).toFloat()
        }

    val yaw: Float
        get() {
            val dx: Double = x2 - x1
            val dz = z2 - z1
            return -MathHelper.wrapDegrees(Math.toDegrees(atan2(dx, dz))).toFloat()
        }

    fun dotProduct(vec: Vec3D): Double {
        val dz1 = z2 - z1
        val dz2 = vec.z2 - vec.z1
        return super.dotProduct(vec) + dz1 * dz2
    }

    fun crossProduct(vec: Vec3D): Vec3D {
        val dx1: Double = x2 - x1
        val dx2: Double = vec.x2 - vec.x1
        val dy1: Double = y2 - y1
        val dy2: Double = vec.y2 - vec.y1
        val dz1 = z2 - z1
        val dz2 = vec.z2 - vec.z1
        return Vec3D(0.0, 0.0, 0.0, dy1 * dz2 - dz1 * dy2, dz1 * dx2 - dx1 * dz2, dx1 * dy2 - dy1 * dx2)
    }

    override fun reverse(): Vec3D {
        return Vec3D(x2, y2, z2, x1, y1, z1)
    }

    override fun toString(): String {
        return java.lang.String.format("%f, %f, %f -> %f, %f, %f", x1, y1, z1, x2, y2, z2)
    }

    /**
     * @return
     * @since 1.6.5
     */
    fun toMojangFloatVector(): Vector3f {
        return Vector3f((x2 - x1).toFloat(), (y2 - y1).toFloat(), (z2 - z1).toFloat())
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val vec3D = o as Vec3D
        return x1.compareTo(vec3D.x1) == 0 && y1.compareTo(vec3D.y1) == 0 && x2.compareTo(vec3D.x2) == 0 && y2.compareTo(
            vec3D.y2
        ) == 0 && z1.compareTo(vec3D.z1) == 0 && z2.compareTo(vec3D.z2) == 0
    }

    override fun hashCode(): Int {
        return Objects.hash(super.hashCode(), z1, z2)
    }

    fun compareTo(o: Vec3D): Int {
        var i: Int = start.compareTo(o.start)
        if (i == 0) {
            i = end.compareTo(o.end)
        }
        return i
    }
}