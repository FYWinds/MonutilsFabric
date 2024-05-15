package fyi.fyw.monutils.modules.backpack

import fyi.fyw.monutils.config.Backpack
import fyi.fyw.monutils.config.Misc
import fyi.fyw.monutils.data.CZCharmStats
import fyi.fyw.monutils.utils.KeysKit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.text.Style
import net.minecraft.text.Text
import java.math.RoundingMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

object ZenithCharmAnalyze {
    private val effectRegex = Regex("^([+\\-.\\d%]+) ([\\w\\s]+)")
    private val statRegex = Regex("[+%]")
    private const val GOLD_SUMMARY = 0xE49B20
    private const val BLUE_SUMMARY = 0x4AC2E5
    private const val RED_SUMMARY = 0xD02E28

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
            val tooltip: MutableList<Text> = origTooltip.filterIndexed { index, _ -> index < 6 }.toMutableList()
            val pushBackTooltip: MutableList<Text> = mutableListOf()
            var matched = 0
            var total = 0

            // Starts from the 6th line, the stats lines
            for (i in 6 until origTooltip.size) {
                val currentLore = origTooltip[i]
                val lineEffect = currentLore.string.substringAfter(" ")

                val objectKey = stats.keys.firstOrNull {
                    try {
                        stats.getString(it) == lineEffect
                                && !it.contains("ACTIONS")
                                && !it.contains("RARITY")
                    } catch (_: Exception) {
                        false
                    }
                }

                if (objectKey == null) {
                    pushBackTooltip.add(currentLore)
                    continue
                }

                var currentStat: Double
                try {
                    currentStat = currentLore.string.substringBefore(" ").replace(statRegex, "").toDouble()
                } catch (e: Exception) {
                    pushBackTooltip.add(currentLore)
                    continue
                }

                total++
                if (Backpack.zenithFilter.isNotEmpty() && !KeysKit.isCtrlDown() && Backpack.zenithFilter.none {
                        lineEffect.contains(it, ignoreCase = true)
                    }) {
                    pushBackTooltip.add(currentLore)
                    continue
                }
                matched++

                val objectNumber = objectKey.replace("DEPTHS_CHARM_EFFECT", "").toInt()
                val objectRoll = stats.getDouble("DEPTHS_CHARM_ROLLS$objectNumber")
                // Using 0.01 as the default max to identify fast
                val maxRoll = CZCharmStats.charmStatMap[lineEffect]?.let { stat ->
                    if (stat.effectCap > 0) stat.legendaryStat[1] else stat.legendaryStat[0]
                } ?: 0.01
                val currStatScore = currentStat / maxRoll
                charmScore += currStatScore
                val currRoll = (objectRoll * 100).toBigDecimal().setScale(2, RoundingMode.HALF_UP)


                tooltip.add(
                    currentLore.copy().append(
                        " [${
                            (currStatScore * 100).toBigDecimal().setScale(1, RoundingMode.HALF_UP)
                        }%]" + if (Backpack.zenithAdvancedMode && KeysKit.isCtrlDown()) " | [M: $maxRoll] (R: $currRoll)" else ""
                    )
                )
            }

            tooltip.add(
                Text.of(
                    "§aCharm Score: §f${
                        (charmScore / charmPower).toBigDecimal().setScale(2, RoundingMode.HALF_UP)
                    }"
                )
            )
            if (total > matched) {
                tooltip.add(Text.empty())
            }

            tooltip.addAll(pushBackTooltip.filterIndexed { index, _ -> index < total - matched })
            tooltip.addAll(pushBackTooltip.filterIndexed { index, _ -> index >= total - matched })
            return tooltip
        } catch (e: Exception) {
            if (Misc.debug) {
                println("Failed to analyze Zenith Charm [${stack.name.string}].")
                e.printStackTrace()
            }
        }
        return null
    }

    private fun getStats(stack: ItemStack): Map<String, Double> {
        val stats = mutableMapOf<String, Double>()
        val monumenta = stack.nbt?.getCompound("Monumenta") ?: return stats
        if (monumenta.getString("Tier") != "zenithcharm") return stats
        if (monumenta.getCompound("PlayerModified") == null) return stats
        val lore = stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.BASIC)
        lore.map { it.string }.forEach {
            val effect = effectRegex.find(it)?.groupValues ?: return@forEach
            val stat = effect[1].removeSuffix("%").toDoubleOrNull() ?: return@forEach
            val name = effect[2]
            stats[name] = stat
        }
        return stats
    }

    fun charmSummary(stack: ItemStack): List<Text>? {
        val client = MinecraftClient.getInstance()
        val player = client.player!!
        if (client.currentScreen?.title?.string?.contains("Charms") != true) return null
        val origTooltip = stack.getTooltip(
            player,
            if (client.options.advancedItemTooltips) TooltipContext.Default.ADVANCED else TooltipContext.Default.BASIC
        )
        if (!origTooltip[1].string.contains("These Charms are currently disabled!")) return null

        val chest =
            player.currentScreenHandler.slots.first { it.inventory is SimpleInventory }.inventory as SimpleInventory
        val charmList = chest.stacks.filterIndexed { index, item ->
            index in 45..53
                    && item.hasCustomName() // Make sure not red glass pane
        }
        var charmStats = mutableMapOf<String, StatSummary>()
        charmList.forEach { charm ->
            val stats = getStats(charm)
            stats.forEach { (key, value) ->
                val stat = charmStats[key]
                if (stat == null) {
                    charmStats[key] = StatSummary(value)
                } else {
                    stat.value += value
                }
            }
        }
        charmStats = charmStats.toSortedMap()

        charmStats.forEach { (effect, stat) ->
            CZCharmStats.charmStatMap[effect]?.let { statData ->
                if (statData.effectCap > 0) {
                    stat.isMax = stat.value >= statData.effectCap
                    stat.isBad = stat.value < 0
                    stat.value = min(stat.value, statData.effectCap)
                } else if (statData.effectCap < 0) {
                    stat.isMax = stat.value <= statData.effectCap
                    stat.isBad = stat.value > 0
                    stat.value = max(stat.value, statData.effectCap)
                }
                if (statData.isPercent) {
                    stat.isPercentage = true
                }
            }
        }

        val newTooltip = mutableListOf<Text>()
        newTooltip.add(origTooltip[0])

        fun addTooltip(effect: String, stat: StatSummary) {
            val value = if (round(stat.value) == stat.value) stat.value.toInt()
            else if (stat.value.toBigDecimal().scale() < 2) stat.value.toBigDecimal().setScale(1, RoundingMode.HALF_UP)
            else stat.value.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
            newTooltip.add(
                Text.literal(
                    "$effect${if (stat.isPercentage) "%" else ""} : $value${if (stat.isPercentage) "%" else ""}"
                ).setStyle(
                    if (stat.isBad) Style.EMPTY.withColor(RED_SUMMARY)
                    else if (stat.isMax) Style.EMPTY.withColor(GOLD_SUMMARY)
                    else Style.EMPTY.withColor(BLUE_SUMMARY)
                ).append(
                    if (stat.isMax) " (Max)" else ""
                )
            )
        }

        if (Backpack.zenithFilter.isNotEmpty()) {
            charmStats.filter { (effect, _) ->
                effect in Backpack.zenithFilter
            }.forEach { addTooltip(it.key, it.value) }

            newTooltip.add(Text.empty())

            charmStats.filter { (effect, _) ->
                effect !in Backpack.zenithFilter
            }.forEach { addTooltip(it.key, it.value) }
        } else {
            charmStats.forEach { addTooltip(it.key, it.value) }
        }

        newTooltip.addAll(origTooltip.subList(2, origTooltip.size))

        return newTooltip
    }

    class StatSummary(
        var value: Double,
        var isPercentage: Boolean = false,
        var isBad: Boolean = false,
        var isMax: Boolean = false
    )
}