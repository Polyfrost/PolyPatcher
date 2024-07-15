package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.hooks.ResourceReloadHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/client/gui/GuiLanguage$List")
public class GuiLanguageMixin_RefreshLanguage {

    @Inject(
        method = "elementClicked",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;refreshResources()V"
        )
    )
    private void onResourceLoad(int par1, boolean par2, int par3, int par4, CallbackInfo ci) {
        ResourceReloadHooks.setLoadLanguage(true);
    }

    @Inject(
        method = "elementClicked",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;refreshResources()V",
            shift = At.Shift.AFTER
        )
    )
    private void onGameOptionWrite(int par1, boolean par2, int par3, int par4, CallbackInfo ci) {
        ResourceReloadHooks.setLoadLanguage(false);
    }
}
