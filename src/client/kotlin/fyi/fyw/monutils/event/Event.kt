package fyi.fyw.monutils.event

abstract class Event {
    abstract val cancellable: Boolean

    var cancelled = false
        private set


    fun cancel() {
        if (cancellable) cancelled = true
    }

}