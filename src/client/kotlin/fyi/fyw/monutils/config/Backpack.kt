package fyi.fyw.monutils.config

import dev.isxander.yacl3.api.*
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder
import fyi.fyw.monutils.Monutils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.text.Text
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createParentDirectories
import kotlin.io.path.notExists


object Backpack {
    private const val CATEGORY: String = "monutils.config.category.backpack"
    private val config: BackpackConfig =
        BackpackConfig(FabricLoader.getInstance().configDir.resolve("Monutils/settings_backpack.json"))

    var backpackFeaturesEnabled: Boolean by ConfigDelegate(config, false)
    var lootBoxWarn: Int by ConfigDelegate(config, 25)
    var analyzeZenithCharm: Boolean by ConfigDelegate(config, false)
    var zenithAdvancedMode: Boolean by ConfigDelegate(config, false)
    var zenithFilter: MutableSet<String> by ConfigDelegate(config, mutableSetOf())

    fun getBuilder(builder: YetAnotherConfigLib.Builder): YetAnotherConfigLib.Builder {
        return builder.category(
            ConfigCategory.createBuilder().apply {
                name(Text.translatableWithFallback(CATEGORY, "Backpack Features"))
                tooltip(Text.translatableWithFallback("$CATEGORY.desc", "Backpack related features settings."))

                group(
                    OptionGroup.createBuilder().apply {
                        name(
                            Text.translatableWithFallback(
                                "monutils.config.backpack.group.general",
                                "General"
                            )
                        )
                        description(
                            OptionDescription.of(
                                Text.translatableWithFallback(
                                    "monutils.config.backpack.group.general.desc",
                                    "General settings for backpack features."
                                )
                            )
                        )

                        option(
                            Option.createBuilder<Boolean>().apply {
                                name(
                                    Text.translatableWithFallback(
                                        "monutils.config.backpack.backpack_features_enabled", "Enable Backpack Features"
                                    )
                                )
                                description(
                                    OptionDescription.of(
                                        Text.translatableWithFallback(
                                            "monutils.config.backpack.backpack_features_enabled.desc",
                                            "Enables or disables all backpack features."
                                        )
                                    )
                                )
                                binding(true, this@Backpack::backpackFeaturesEnabled) { newVal ->
                                    backpackFeaturesEnabled = newVal
                                }
                                controller(TickBoxControllerBuilder::create)
                            }.build()
                        )

                        option(
                            Option.createBuilder<Int>().apply {
                                name(
                                    Text.translatableWithFallback(
                                        "monutils.config.backpack.loot_box_warn",
                                        "Loot Box Warning"
                                    )
                                )
                                description(
                                    OptionDescription.of(
                                        Text.translatableWithFallback(
                                            "monutils.config.backpack.loot_box_warn.desc",
                                            "Warn when loot box count is over this value."
                                        )
                                    )
                                )
                                binding(25, this@Backpack::lootBoxWarn) { newVal -> lootBoxWarn = newVal }
                                controller { opt ->
                                    IntegerSliderControllerBuilder.create(opt).range(1, 100).step(1).valueFormatter {
                                        Text.of("$it")
                                    }
                                }
                            }.build()
                        )
                    }.build()
                )

                group(
                    OptionGroup.createBuilder().apply {
                        name(
                            Text.translatableWithFallback(
                                "monutils.config.backpack.group.zenith",
                                "Zenith Charm Analysis"
                            )
                        )
                        description(
                            OptionDescription.of(
                                Text.translatableWithFallback(
                                    "monutils.config.backpack.group.zenith.desc",
                                    "Zenith Charm Analysis settings."
                                )
                            )
                        )

                        option(
                            Option.createBuilder<Boolean>().apply {
                                name(
                                    Text.translatableWithFallback(
                                        "monutils.config.backpack.analyze_zenith_charm",
                                        "Analyze Zenith Charm"
                                    )
                                )
                                description(
                                    OptionDescription.of(
                                        Text.translatableWithFallback(
                                            "monutils.config.backpack.analyze_zenith_charm.desc",
                                            "Analyze Zenith Charm."
                                        )
                                    )
                                )
                                binding(true, this@Backpack::analyzeZenithCharm) { newVal ->
                                    analyzeZenithCharm = newVal
                                }
                                controller(TickBoxControllerBuilder::create)
                            }.build()
                        )

                        option(
                            Option.createBuilder<Boolean>().apply {
                                name(
                                    Text.translatableWithFallback(
                                        "monutils.config.backpack.zenith_advanced_mode",
                                        "Show Advanced Stats"
                                    )
                                )
                                description(
                                    OptionDescription.of(
                                        Text.translatableWithFallback(
                                            "monutils.config.backpack.zenith_advanced_mode.desc",
                                            "Show advanced stats (R: ROLLS M: MAX) when pressing left control."
                                        )
                                    )
                                )
                                binding(false, this@Backpack::zenithAdvancedMode) { newVal ->
                                    zenithAdvancedMode = newVal
                                }
                                controller(TickBoxControllerBuilder::create)
                            }.build()
                        )
                    }.build()
                )
            }.build()

        )
    }


    @Serializable
    class BackpackData {
        var backpackFeaturesEnabled = true
        var lootBoxWarn = 25
        var analyzeZenithCharm = true
        var zenithAdvancedMode = false
        var zenithFilter: MutableSet<String> = mutableSetOf()
    }

    class BackpackConfig(filePath: Path) : AbstractConfig<BackpackData>(filePath) {
        override var data: BackpackData = loadFromFile()

        override fun loadFromFile(): BackpackData {
            return if (filePath.notExists()) {
                filePath.createParentDirectories()
                defaultData()
            } else {
                try {
                    Json.decodeFromString(BackpackData.serializer(), Files.readString(filePath))
                } catch (e: Exception) {
                    Monutils.logger.error("Failed to load backpack config file, using default values.")
                    e.printStackTrace()
                    defaultData()
                }
            }
        }

        override fun saveToFile() {
            Files.writeString(filePath, Json.encodeToString(BackpackData.serializer(), data))
        }

        override fun defaultData(): BackpackData = BackpackData()
    }
}
