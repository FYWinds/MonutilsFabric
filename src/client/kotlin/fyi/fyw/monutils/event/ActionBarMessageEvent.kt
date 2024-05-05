package fyi.fyw.monutils.event

import net.minecraft.text.Text

data class ActionBarMessageEvent(val message: Text, val tinted: Boolean) : Event() {
    override val cancellable = true
}