package fyi.fyw.monutils.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import fyi.fyw.monutils.config.Backpack
import fyi.fyw.monutils.data.CZAbilities
import fyi.fyw.monutils.data.CZCharmStats
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text
import java.util.concurrent.CompletableFuture

object ZenithCharmFilter {
    var needClearConfirmation = false
    var clearConfirmationTime = 0L

    fun registerCommand() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            dispatcher.register(
                literal("czfilter").apply {
                    then(
                        literal("add").then(
                            argument("stat", StringArgumentType.greedyString()).suggests(AddSuggestionProvider())
                                .executes { context ->
                                    val stat = context.getArgument("stat", String::class.java)
                                    if (stat in CZAbilities.czAbilities || stat in CZCharmStats.charmStatMap.keys) {
                                        val filter = Backpack.zenithFilter.toMutableSet()
                                        filter.add(stat)
                                        Backpack.zenithFilter = filter

                                        context.source.sendFeedback(
                                            Text.translatable(
                                                "monutils.command.czfilter.add.success",
                                                stat
                                            )
                                        )
                                        return@executes 1
                                    } else {
                                        context.source.sendError(
                                            Text.translatable(
                                                "monutils.command.czfilter.add.error",
                                                stat
                                            )
                                        )
                                        return@executes 0
                                    }
                                }
                        )
                    )

                    then(
                        literal("remove").then(
                            argument("stat", StringArgumentType.greedyString()).suggests(RemoveSuggestionProvider())
                                .executes { context ->
                                    val stat = context.getArgument("stat", String::class.java)
                                    if (stat in Backpack.zenithFilter) {
                                        val filter = Backpack.zenithFilter.toMutableSet()
                                        filter.remove(stat)
                                        Backpack.zenithFilter = filter
                                        context.source.sendFeedback(
                                            Text.translatable(
                                                "monutils.command.czfilter.remove.success",
                                                stat
                                            )
                                        )
                                        return@executes 1
                                    } else {
                                        context.source.sendError(
                                            Text.translatable(
                                                "monutils.command.czfilter.remove.error",
                                                stat
                                            )
                                        )
                                        return@executes 0
                                    }
                                }
                        )
                    )

                    then(
                        literal("list").executes { context ->
                            val filter = Backpack.zenithFilter
                            if (filter.isEmpty()) {
                                context.source.sendFeedback(
                                    Text.translatable("monutils.command.czfilter.list.empty")
                                )
                                return@executes 0
                            } else {
                                val feedback = Text.translatable("monutils.command.czfilter.list.header")
                                filter.forEach {
                                    feedback.append(Text.of("\n"))
                                    feedback.append(Text.translatable("monutils.command.czfilter.list.item", it))
                                }
                                context.source.sendFeedback(feedback)
                                return@executes 1
                            }
                        }
                    )

                    then(
                        literal("clear").executes { context ->
                            if (needClearConfirmation && System.currentTimeMillis() - clearConfirmationTime < 10000) {
                                Backpack.zenithFilter = mutableSetOf()
                                context.source.sendFeedback(
                                    Text.translatable("monutils.command.czfilter.clear.success")
                                )
                                needClearConfirmation = false
                                return@executes 1
                            } else {
                                needClearConfirmation = true
                                clearConfirmationTime = System.currentTimeMillis()
                                context.source.sendFeedback(
                                    Text.translatable("monutils.command.czfilter.clear.confirm")
                                )
                                return@executes 0
                            }
                        }
                    )
                }
            )
        }
    }

    private class AddSuggestionProvider : SuggestionProvider<FabricClientCommandSource> {
        override fun getSuggestions(
            context: CommandContext<FabricClientCommandSource>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            try {
                val input = context.getArgument("stat", String::class.java)
                for (ability in CZAbilities.czAbilities) {
                    if (ability.lowercase().startsWith(input.lowercase())) {
                        builder.suggest(ability)
                    }
                }
                for (stat in CZCharmStats.charmStatMap.keys) {
                    if (stat.lowercase().startsWith(input.lowercase())) {
                        builder.suggest(stat)
                    }
                }
            } catch (_: IllegalArgumentException) {
            }
            return builder.buildFuture()
        }
    }

    private class RemoveSuggestionProvider : SuggestionProvider<FabricClientCommandSource> {
        override fun getSuggestions(
            context: CommandContext<FabricClientCommandSource>,
            builder: SuggestionsBuilder
        ): CompletableFuture<Suggestions> {
            try {
                val input = context.getArgument("stat", String::class.java)
                for (stat in Backpack.zenithFilter) {
                    if (stat.lowercase().startsWith(input.lowercase())) {
                        builder.suggest(stat)
                    }
                }
            } catch (_: IllegalArgumentException) {
            }
            return builder.buildFuture()
        }
    }
}