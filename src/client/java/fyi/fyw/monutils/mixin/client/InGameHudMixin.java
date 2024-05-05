package fyi.fyw.monutils.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    public void renderMixin(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden) {
            renderRedirectedMessages(matrices);
        }
    }

    @Unique
    private void renderRedirectedMessages(MatrixStack matrices) {
//        RedirectedMessageScreen.INSTANCE.render(matrices);
    }
}
