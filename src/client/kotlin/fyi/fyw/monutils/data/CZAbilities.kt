package fyi.fyw.monutils.data

import com.google.gson.JsonParser
import fyi.fyw.monutils.utils.FileUtils
import java.io.InputStreamReader

object CZAbilities {
    const val RESOURCE_PATH = "assets/monutils/data/czabilities.json"
    val czAbilities: List<String> = loadCZAbilities()

    private fun loadCZAbilities(): MutableList<String> {
        val lst = mutableListOf<String>()
        val json = FileUtils.getResource(RESOURCE_PATH) ?: return lst
        try {
            val root = JsonParser.parseReader(InputStreamReader(json)).asJsonArray
            root.forEach {
                lst.add(it.asString)
            }
        } catch (e: Exception) {
            println("Failed to load zenith abilities data.")
            e.printStackTrace()
        }
        println("Loaded ${lst.size} abilities stats.")
        return lst
    }
}