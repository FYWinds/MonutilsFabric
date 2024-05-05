package fyi.fyw.monutils.gui

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text

object RedirectedMessageScreen {

    val messages: MutableList<Text> = mutableListOf(Text.of("Hello World!"))
    private val client: MinecraftClient = MinecraftClient.getInstance()
    private val textRenderer = client.textRenderer

    private val x = 0;
    private val y = 0;
    private val width = 100; // TODO adapt scale
    private val height = 100;

    fun render(matrices: MatrixStack) {
        // TODO config disable
        matrices.push()
        matrices.translate(x.toDouble(), y.toDouble(), 0.0)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        renderBackground(matrices)
        renderMessages(matrices)

        matrices.pop()
    }

    fun renderBackground(matrices: MatrixStack) {
        // TODO
        DrawableHelper.fill(
            matrices,
            0,
            0,
            width,
            height,
            client.options.getTextBackgroundColor(client.options.chatOpacity.value.toFloat())
        );
    }

    fun renderMessages(matrices: MatrixStack) {
        // TODO
    }
}