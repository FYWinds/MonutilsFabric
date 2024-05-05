package fyi.fyw.monutils.utils

import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.gui.controllers.string.IStringController
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object PlayerInvUtils {
    fun PlayerInventory.getCombinedInventory(): List<ItemStack> {
        return listOf(this.main, this.armor, this.offHand).flatten()
    }

    fun PlayerInventory.first(predicate: (ItemStack) -> Boolean): ItemStack {
        return this.getCombinedInventory().first { predicate(it) }
    }

    fun PlayerInventory.firstOrNull(predicate: (ItemStack) -> Boolean, default: ItemStack? = null): ItemStack? {
        return this.getCombinedInventory().firstOrNull { predicate(it) } ?: default
    }

    fun PlayerInventory.any(predicate: (ItemStack) -> Boolean): Boolean {
        return this.getCombinedInventory().any { predicate(it) }
    }

    fun PlayerInventory.hasItemWithName(name: String): Boolean {
        return this.any { it.name.string.contains(name, ignoreCase = true) }
    }

    fun PlayerInventory.hasItemWithLore(lore: String, ignoreCase: Boolean = false): Boolean {
        return this.any { item ->
            item.getTooltip(this.player, TooltipContext.BASIC).any { tooltip ->
                tooltip.string.contains(lore, ignoreCase)
            }
        }
    }

    fun PlayerInventory.getHeldingItem(): ItemStack {
        return this.getStack(this.selectedSlot)
    }

    class InvItemStringControllerBuilder(private val option: Option<String>) :
        AbstractControllerBuilderImpl<String>(option) {
        override fun build(): InvItemStringController {
            return InvItemStringController(option)
        }
    }

    class InvItemStringController(private val option: Option<String>) : IStringController<String> {
        override fun option(): Option<String> {
            return option
        }

        override fun getString(): String {
            return option().pendingValue()
        }

        override fun setFromString(value: String) {
            option().requestSet(value)
        }

        override fun formatValue(): Text {
            return Text.literal(option.pendingValue()).formatted(
                if (MinecraftClient.getInstance().player?.inventory?.hasItemWithName(option.pendingValue()) == true) Formatting.GREEN else Formatting.RED
            )
        }
    }
}

