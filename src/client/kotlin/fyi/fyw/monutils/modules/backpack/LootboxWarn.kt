package fyi.fyw.monutils.modules.backpack

import fyi.fyw.monutils.config.Backpack
import fyi.fyw.monutils.utils.MsgUtils
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.Text

object LootboxWarn {
    private var lastShare = 0
    private var currShare = 0
    private var maxShare = 27

    private var debugTicks = 0

    private val shareRegex = Regex("""(\d+)/(27|100) shares$""")

    fun checkLootbox(client: MinecraftClient) {
        val player = client.player ?: return
        val inventory = player.inventory ?: return
        if (client.currentScreen != null) return

        val lootbox = inventory.main.firstOrNull { item ->
            item.getTooltip(player, TooltipContext.BASIC).any { tooltip ->
                tooltip.string.contains("any nearby opened loot chest.")
            }
        } ?: return

        lootbox.getTooltip(player, TooltipContext.BASIC).firstOrNull { tooltip ->
            tooltip.string.contains("shares")
        }?.string?.apply {
            shareRegex.find(this)?.let {
                currShare = it.groupValues[1].toInt()
                maxShare = it.groupValues[2].toInt()
            }
        } ?: return

        if (++debugTicks % 20 == 0) {
            MsgUtils.sendDebugMessage("LootboxWarn: $currShare/$maxShare")
            debugTicks = 0
        }

        if (currShare != lastShare) {

            if (currShare >= Backpack.lootBoxWarn && currShare > lastShare) {
                client.inGameHud.setTitle(Text.of("Lootbox FULL: $currShare/$maxShare"))
                client.inGameHud.setTitleTicks(5, 20, 5)
            }
            lastShare = currShare
        }
    }
}