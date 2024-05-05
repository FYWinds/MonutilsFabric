package fyi.fyw.monutils.utils

import fyi.fyw.monutils.MonutilsClient
import java.io.InputStream

object FileUtils {
    fun getResource(path: String): InputStream? {
        return try {
            val url = MonutilsClient.javaClass.classLoader.getResource(path)
            if (url == null) {
                null
            } else {
                val connection = url.openConnection()
                connection.useCaches = false
                connection.getInputStream()
            }
        } catch (e: Exception) {
            null
        }
    }
}