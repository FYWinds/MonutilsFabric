package fyi.fyw.monutils.mixin.client;

import fyi.fyw.monutils.event.ActionBarMessageEvent;
import fyi.fyw.monutils.event.EventManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.client.gui.hud.InGameHud.class)
public class ActionBarMessageMixin {

    @Inject(method = "setOverlayMessage", at = @At("HEAD"))
    private void onSetOverlayMessage(Text message, boolean tinted, CallbackInfo ci) {
        EventManager.INSTANCE.invoke(new ActionBarMessageEvent(message, tinted));
    }

}
