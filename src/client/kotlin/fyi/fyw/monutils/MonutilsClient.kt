package fyi.fyw.monutils

import fyi.fyw.monutils.commands.ZenithCharmFilter
import fyi.fyw.monutils.config.Backpack
import fyi.fyw.monutils.config.Misc
import fyi.fyw.monutils.modules.backpack.LootboxWarn
import fyi.fyw.monutils.modules.misc.Pickup
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient


object MonutilsClient : ClientModInitializer {

    override fun onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            if (Backpack.backpackFeaturesEnabled) LootboxWarn.checkLootbox(client)
            if (Misc.pickup) Pickup.tick(client)
        })

        registerEvents()
        registerCommands()

        Monutils.logger.info("Monutils Loaded!")

    }

    private fun registerEvents() {
    }

    private fun registerCommands() {
        ZenithCharmFilter.registerCommand()
    }
}