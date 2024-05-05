package fyi.fyw.monutils.config

import dev.isxander.yacl3.api.ConfigCategory
import dev.isxander.yacl3.api.Option
import dev.isxander.yacl3.api.OptionDescription
import dev.isxander.yacl3.api.YetAnotherConfigLib
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder
import dev.isxander.yacl3.api.controller.EnumControllerBuilder
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import fyi.fyw.monutils.Monutils
import fyi.fyw.monutils.modules.misc.Pickup
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists


object Misc {
    private const val CATEGORY: String = "monutils.config.category.misc"
    private val config: MiscConfig =
        MiscConfig(FabricLoader.getInstance().configDir.resolve("Monutils/settings_misc.json"))

    var debug: Boolean by ConfigDelegate(config, false)
    var pickup: Boolean by ConfigDelegate(config, false)
    var pickupDelay: Int by ConfigDelegate(config, 500)
    var pickupModeNormal: Pickup.PickupMode by ConfigDelegate(config, Pickup.PickupMode.LORED)
    var pickupModeSneak: Pickup.PickupMode by ConfigDelegate(config, Pickup.PickupMode.ALL)

    fun getBuilder(builder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder {
        return builder.apply {
            category(ConfigCategory.createBuilder().apply {
                name(Text.translatableWithFallback(CATEGORY, "Miscellaneous"))
                tooltip(
                    Text.translatableWithFallback(
                        "monutils.config.category.misc.tooltip",
                        "Miscellaneous settings."
                    )
                )

                option(Option.createBuilder<Boolean>().apply {
                    name(Text.of("Enable Debug Messages"))
                    description(OptionDescription.of(Text.of("Enable debug messages.")))
                    binding(false, ::debug) { debug = it }
                    controller {
                        BooleanControllerBuilder.create(it).apply {
                            valueFormatter { v -> if (v) Text.of("√") else Text.of("x") }
                            coloured(true)
                        }
                    }
                }.build())

                option(Option.createBuilder<Boolean>().apply {
                    name(Text.translatableWithFallback("monutils.config.misc.pickup", "Auto Pickup"))
                    description(
                        OptionDescription.of(
                            Text.translatableWithFallback(
                                "monutils.config.misc.pickup.desc",
                                "Enable switching pickup mode."
                            )
                        )
                    )
                    binding(false, ::pickup) { pickup = it }
                    controller(TickBoxControllerBuilder::create)
                }.build())

                option(Option.createBuilder<Int>().apply {
                    name(Text.translatableWithFallback("monutils.config.misc.pickupDelay", "Pickup Delay"))
                    description(
                        OptionDescription.of(
                            Text.translatableWithFallback(
                                "monutils.config.misc.pickupDelay.desc",
                                "Delay between pickups."
                            )
                        )
                    )
                    binding(500, ::pickupDelay) { pickupDelay = it }
                    controller { opt ->
                        IntegerSliderControllerBuilder.create(opt).step(100).range(100, 2500).valueFormatter { it ->
                            Text.of("${it}ms")
                        }
                    }
                }.build())

                option(Option.createBuilder<Pickup.PickupMode>().apply {
                    name(Text.translatableWithFallback("monutils.config.misc.pickupModeNormal", "Pickup Mode Normal"))
                    description(
                        OptionDescription.of(
                            Text.translatableWithFallback(
                                "monutils.config.misc.pickupModeNormal.desc",
                                "Pickup mode when not sneaking."
                            )
                        )
                    )
                    binding(Pickup.PickupMode.LORED, ::pickupModeNormal) { pickupModeNormal = it }
                    controller { opt ->
                        EnumControllerBuilder.create(opt).enumClass(Pickup.PickupMode::class.java)
                    }
                }.build())

                option(Option.createBuilder<Pickup.PickupMode>().apply {
                    name(Text.translatableWithFallback("monutils.config.misc.pickupModeSneak", "Pickup Mode Sneak"))
                    description(
                        OptionDescription.of(
                            Text.translatableWithFallback(
                                "monutils.config.misc.pickupModeSneak.desc",
                                "Pickup mode when sneaking."
                            )
                        )
                    )
                    binding(Pickup.PickupMode.ALL, ::pickupModeSneak) { pickupModeSneak = it }
                    controller { opt ->
                        EnumControllerBuilder.create(opt).enumClass(Pickup.PickupMode::class.java)
                    }
                }.build())

            }.build())
        }
    }


    @Serializable
    class MiscData {
        var debug = false
        var pickup = false
        var pickupDelay = 500
        var pickupModeNormal = Pickup.PickupMode.LORED
        var pickupModeSneak = Pickup.PickupMode.ALL
    }

    class MiscConfig(filePath: Path) : AbstractConfig<MiscData>(filePath) {
        override var data: MiscData = loadFromFile()

        override fun loadFromFile(): MiscData {
            return if (filePath.notExists()) {
                filePath.createParentDirectories()
                defaultData()
            } else {
                try {
                    Json.decodeFromString(MiscData.serializer(), Files.readString(filePath))
                } catch (e: Exception) {
                    Monutils.logger.error("Failed to load misc config file, using default values.")
                    e.printStackTrace()
                    defaultData()
                }
            }
        }

        override fun saveToFile() {
            Files.writeString(filePath, Json.encodeToString(MiscData.serializer(), data))
        }

        override fun defaultData(): MiscData = MiscData()
    }
}