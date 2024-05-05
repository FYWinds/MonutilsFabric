package fyi.fyw.monutils.event

abstract class EventHandler<E : Event> {
    abstract fun handle(event: E)

    fun registerFor(eventType: Class<E>) {
        EventManager.register(eventType, this)
    }
}