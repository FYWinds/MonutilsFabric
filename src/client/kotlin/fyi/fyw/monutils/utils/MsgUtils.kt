package fyi.fyw.monutils.utils

import fyi.fyw.monutils.config.Misc
import net.minecraft.client.MinecraftClient
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object MsgUtils {

    private val PREFIX = Text.literal("[MonUtils] ").formatted(Formatting.GRAY)
    private val DEBUG_PREFIX =
        Text.literal("[MonUtils Debug] ").formatted(Formatting.RED)

    fun sendDebugMessage(message: String, actionbar: Boolean = false) {
        sendDebugMessage(Text.of(message), actionbar)
    }

    fun sendDebugMessage(message: Text, actionbar: Boolean = false) {
        val client = MinecraftClient.getInstance()
        if (Misc.debug) {
            val msg = DEBUG_PREFIX.copy().append(MutableText.of(message.content).formatted(Formatting.WHITE))
            if (actionbar) {
                client.player?.sendMessage(msg, true)
            } else {
                client.player?.sendMessage(msg, false)
            }
        }
    }
}