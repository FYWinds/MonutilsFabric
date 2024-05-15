package fyi.fyw.monutils.data

import fyi.fyw.monutils.utils.FileUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object CZCharmStats {
    const val RESOURCE_PATH = "assets/monutils/data/Zenithcharm_stats.json"
    var charmStatMap: Map<String, CharmStat>
    val deserializer = Json { ignoreUnknownKeys = true }

    init {
        charmStatMap = mapOf()
        val inner: CharmStatMap
        val json = FileUtils.getResource(RESOURCE_PATH)
        if (json != null) {
            try {
                inner = deserializer.decodeFromString(json.readAllBytes().decodeToString())
                charmStatMap = inner.data
            } catch (e: Exception) {
                println("Failed to load zenith charm stat data.")
                e.printStackTrace()
            }
        }
        println("Loaded ${charmStatMap.size} zenith charm stats.")
    }

    @Serializable
    data class CharmStat(
        val effectCap: Double,
        val isOnlyPositive: Boolean,
        val isPercent: Boolean,
        val commonStat: List<Double>,
        val uncommonStat: List<Double>,
        val rareStat: List<Double>,
        val epicStat: List<Double>,
        val legendaryStat: List<Double>
    )

    @Serializable
    data class CharmStatMap(
//        val version: String,
//        val credit: String,
        val data: Map<String, CharmStat>
    )
}