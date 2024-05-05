package fyi.fyw.monutils.event

import fyi.fyw.monutils.utils.MsgUtils
import net.minecraft.text.Text
import net.minecraft.util.Formatting

object EventManager {
    private val handlersMap: MutableMap<Class<out Event>, MutableList<EventHandler<*>>> = mutableMapOf()

    fun <E : Event> register(eventType: Class<E>, handler: EventHandler<E>) {
        val handlers = handlersMap.getOrPut(eventType) { mutableListOf() }
        handlers.add(handler as EventHandler<*>)
    }

    fun <E : Event> invoke(event: E): Boolean {
        MsgUtils.sendDebugMessage(Text.literal("[EVENT]").formatted(Formatting.AQUA).append("Dispatching $event"))
        @Suppress("UNCHECKED_CAST")
        (handlersMap[event.javaClass] as? List<EventHandler<E>>)?.forEach { handler ->
            if (event.cancellable && event.cancelled) return true
            handler.handle(event)
        }
        return false
    }
}