package fyi.fyw.monutils.config

import dev.isxander.yacl3.api.YetAnotherConfigLib
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text

object Config {


    fun getScreen(parent: Screen): Screen {
        return YetAnotherConfigLib.createBuilder().apply {
            title(Text.translatableWithFallback("monutils.config.title", "MonUtils Configuration"))
            Backpack.getBuilder(this)
            Misc.getBuilder(this)
        }.build().generateScreen(parent)
    }
}