package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_PumpkinBlurRendering {
    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void patcher$cancelPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo ci) {
        if (PatcherConfig.pumpkinOverlayOpacity == 0) ci.cancel();
    }

    @ModifyArg(method = "renderPumpkinOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;color(FFFF)V"), index = 3)
    private float patcher$modifyPumpkinOpacity(float alpha) {
        return PatcherConfig.pumpkinOverlayOpacity;
    }
}
