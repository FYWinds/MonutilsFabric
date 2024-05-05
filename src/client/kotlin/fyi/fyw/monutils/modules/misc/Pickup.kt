package fyi.fyw.monutils.modules.misc

import fyi.fyw.monutils.config.Misc
import net.minecraft.client.MinecraftClient
import kotlin.math.abs

object Pickup {
    private var ticksSneaking = 0
    private var switched = false

    fun tick(client: MinecraftClient) {
        val player = client.player ?: return
        val screen = player.currentScreenHandler
        if (client.currentScreen != null) return
        // velocity check
        val velocity = player.velocity
        if (!(player.isSpectator || player.isCreative) &&
            player.isSneaking &&
            player.isOnGround &&
            player.lastRenderPitch >= 55.0 &&
            (abs(velocity.x) <= 0.05 && abs(velocity.z) <= 0.05)
        ) {
            // turn on pickup
            ticksSneaking++
            if (!switched && ticksSneaking > (Misc.pickupDelay / 50)) {
                client.networkHandler?.sendCommand("pu ${Misc.pickupModeSneak.value}")
                switched = true
            }
        } else if (!player.isSneaking) {
            // turn off pickup
            if (switched) {
                client.networkHandler?.sendCommand("pu ${Misc.pickupModeNormal.value}")
                switched = false
            }
            ticksSneaking = 0
        } else {
            ticksSneaking = 0
        }
    }

    enum class PickupMode(val value: String) {
        ALL("all"),
        INTERESTING("interesting"),
        LORED("lore"),
        TIERED("tiered"),
    }
}