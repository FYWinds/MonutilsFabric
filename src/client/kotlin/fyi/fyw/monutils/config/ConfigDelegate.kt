package fyi.fyw.monutils.config

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ConfigDelegate<T, R : AbstractConfig<*>>(
    private val config: R,
    private val defaultValue: T
) : ReadWriteProperty<Any?, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return config.data.javaClass.getDeclaredField(property.name).let { field ->
            field.isAccessible = true
            field.get(config.data) as? T ?: defaultValue
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        config.data.javaClass.getDeclaredField(property.name).let { field ->
            field.isAccessible = true
            field.set(config.data, value)
        }
        config.saveToFile()
    }
}