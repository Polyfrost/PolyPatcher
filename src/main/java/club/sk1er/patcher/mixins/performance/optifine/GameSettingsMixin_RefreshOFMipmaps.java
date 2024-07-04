package club.sk1er.patcher.mixins.performance.optifine;

import club.sk1er.patcher.hooks.ResourceReloadHooks;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameSettings.class)
public class GameSettingsMixin_RefreshOFMipmaps {

    @Dynamic("OptiFine")
    @Inject(method = "setOptionFloatValueOF", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V"))
    private void onMipmapTypeChange(GameSettings.Options settingsOption, float value, CallbackInfo ci) {
        ResourceReloadHooks.setLoadMipmaps(true);
    }

    @Dynamic("OptiFine")
    @Inject(method = "setOptionFloatValueOF", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;refreshResources()V", shift = At.Shift.AFTER))
    private void onMipmapTypeChangeAfter(GameSettings.Options settingsOption, float value, CallbackInfo ci) {
        ResourceReloadHooks.setLoadMipmaps(false);
    }
}
