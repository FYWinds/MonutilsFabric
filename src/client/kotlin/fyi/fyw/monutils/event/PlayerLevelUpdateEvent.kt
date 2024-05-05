package fyi.fyw.monutils.event

data class PlayerLevelUpdateEvent(val experienceLevel: Int, val totalExperience: Int, val experienceProgress: Float) :
    Event() {
    override val cancellable = false
}