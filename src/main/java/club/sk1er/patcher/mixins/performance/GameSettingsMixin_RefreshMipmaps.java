package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.hooks.ResourceReloadHooks;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class GameSettingsMixin_RefreshMipmaps {

    @Inject(method = "setOptionFloatValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;scheduleResourcesRefresh()Lcom/google/common/util/concurrent/ListenableFuture;"))
    private void onMipmapChange(GameSettings.Options settingsOption, float value, CallbackInfo ci) {
        ResourceReloadHooks.setLoadMipmaps(true);
    }

    @Inject(method = "setOptionFloatValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;scheduleResourcesRefresh()Lcom/google/common/util/concurrent/ListenableFuture;", shift = At.Shift.AFTER))
    private void onMipmapChangeAfter(GameSettings.Options settingsOption, float value, CallbackInfo ci) {
        ResourceReloadHooks.setLoadMipmaps(false);
    }
}
