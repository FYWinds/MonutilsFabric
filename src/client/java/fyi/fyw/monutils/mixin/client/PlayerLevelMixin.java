package fyi.fyw.monutils.mixin.client;

import fyi.fyw.monutils.event.EventManager;
import fyi.fyw.monutils.event.PlayerLevelUpdateEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = net.minecraft.client.network.ClientPlayerEntity.class)
public class PlayerLevelMixin {

    @Inject(method = "setExperience", at = @At("HEAD"), cancellable = true)
    private void onSetExperience(float progress, int total, int level, CallbackInfo ci) {
        if (EventManager.INSTANCE.invoke(new PlayerLevelUpdateEvent(level, total, progress))) {
            ci.cancel();
        }
    }
}
