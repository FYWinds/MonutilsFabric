package fyi.fyw.monutils.utils

import fyi.fyw.monutils.utils.math.Vec3D
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.util.math.MathHelper

object PlayerUtils {
    fun ClientPlayerEntity.lookAt(x: Double, y: Double, z: Double) {
        val vec = Vec3D(
            this.x,
            this.eyeY,
            this.z,
            x,
            y,
            z
        )
        this.lookAt(vec.yaw, vec.pitch)
    }

    fun ClientPlayerEntity.lookAt(yaw: Float, pitch: Float) {
        this.prevPitch = this.pitch
        this.prevYaw = this.yaw
        this.pitch = MathHelper.clamp(pitch, -90.0f, 90.0f)
        this.yaw = MathHelper.wrapDegrees(yaw)
        this.vehicle?.onPassengerLookAround(this)
    }
}