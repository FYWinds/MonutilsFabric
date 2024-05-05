package fyi.fyw.monutils

import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object Monutils : ModInitializer {
    val logger = LoggerFactory.getLogger("monutils")

    override fun onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

    }
}