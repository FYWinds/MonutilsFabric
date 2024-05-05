package fyi.fyw.monutils.modules.backpack

import fyi.fyw.monutils.config.Backpack
import fyi.fyw.monutils.config.Misc
import fyi.fyw.monutils.data.CZCharmStat
import fyi.fyw.monutils.utils.KeysKit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import java.math.RoundingMode

object ZenithCharmAnalyze {
    private val effectRegex = Regex("^.+?\\s")
    private val statRegex = Regex("[+%]")

    fun analyze(stack: ItemStack): List<Text>? {
        val client = MinecraftClient.getInstance()
        val player = client.player!!
        val origTooltip = stack.getTooltip(
            player,
            if (client.options.advancedItemTooltips) TooltipContext.Default.ADVANCED else TooltipContext.Default.BASIC
        )
        try {
            val monumenta = stack.nbt?.getCompound("Monumenta") ?: return null
            if (monumenta.getString("Tier") != "zenithcharm") return null
            val stats = monumenta.getCompound("PlayerModified") ?: return null
            val charmPower = monumenta.getInt("CharmPower")
            var charmScore = 0.0
            val tooltip = origTooltip.toMutableList()

            // Starts from the 6th line, the stats lines
            for (i in 6 until tooltip.size) {
                val currentLore = tooltip[i]
                val lineEffect = currentLore.string.substringAfter(" ")
                val objectKey = stats.keys.firstOrNull {
                    try {
                        stats.getString(it) == lineEffect
                                && !it.contains("ACTIONS")
                    } catch (_: Exception) {
                        false
                    }
                } ?: continue
                val objectNumber = objectKey.replace("DEPTHS_CHARM_EFFECT", "").toInt()
                val objectRoll = stats.getDouble("DEPTHS_CHARM_ROLLS$objectNumber")
                val maxRoll = CZCharmStat.charmStatMap[lineEffect] ?: 0.0
                val currStatScore = currentLore.string.split(" ").first().replace(statRegex, "").toDouble() / maxRoll
                charmScore += currStatScore
                val currRoll = (objectRoll * 100).toBigDecimal().setScale(2, RoundingMode.HALF_UP)


                tooltip[i] = currentLore.copy().append(
                    " [${
                        (currStatScore * 100).toBigDecimal().setScale(1, RoundingMode.HALF_UP)
                    }%]" + if (Backpack.zenithAdvancedMode && KeysKit.isCtrlDown()) " | [M: $maxRoll] (R: $currRoll)" else ""
                )
            }

            tooltip.add(
                Text.of(
                    "§aCharm Score: §f${
                        (charmScore / charmPower).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                    }"
                )
            )
            return tooltip
        } catch (e: Exception) {
            println("Failed to analyze Zenith Charm [${stack.name.string}].")
            if (Misc.debug) e.printStackTrace()
        }
        return null
    }
}