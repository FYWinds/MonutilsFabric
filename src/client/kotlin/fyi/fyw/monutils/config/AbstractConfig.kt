package fyi.fyw.monutils.config

import java.nio.file.Path

abstract class AbstractConfig<T : Any>(val filePath: Path) {
    abstract var data: T

    abstract fun loadFromFile(): T
    abstract fun saveToFile()
    abstract fun defaultData(): T
}
