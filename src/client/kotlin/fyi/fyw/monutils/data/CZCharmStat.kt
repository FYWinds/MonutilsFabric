package fyi.fyw.monutils.data

import com.google.gson.JsonParser
import fyi.fyw.monutils.utils.FileUtils
import java.io.InputStreamReader

object CZCharmStat {
    const val RESOURCE_PATH = "assets/monutils/data/czcharmlist.json"
    val charmStatMap: MutableMap<String, Double> = loadCharmStat()

    private fun loadCharmStat(): MutableMap<String, Double> {
        val map = mutableMapOf<String, Double>()
        val json = FileUtils.getResource(RESOURCE_PATH) ?: return map
        val root = JsonParser.parseReader(InputStreamReader(json)).asJsonObject
        try {
            root.entrySet().forEach {
                map[it.key] = it.value.asDouble
            }
        } catch (e: Exception) {
            println("Failed to load zenith charm stat data.")
            e.printStackTrace()
        }
        println("Loaded ${map.size} zenith charm stats.")
        return map
    }
}