package club.sk1er.patcher.mixins.performance.hudcaching;

import club.sk1er.patcher.screen.render.caching.HUDCaching;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class GuiIngameMixin_HUDCaching {

    @Inject(method = "renderGameOverlay", at = @At("HEAD"))
    private void patcher$resetCaptures(float partialTicks, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderVignetteCaptured = false;
            HUDCaching.renderHelmetCaptured = false;
            HUDCaching.renderPortalCapturedTicks = -1;
        }
    }

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    private void patcher$captureRenderVignette(float lightLevel, ScaledResolution scaledRes, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderVignetteCaptured = true;
            ci.cancel();
        }
    }

    @Inject(method = "renderPumpkinOverlay", at = @At("HEAD"), cancellable = true)
    private void patcher$captureRenderPumpkinOverlay(ScaledResolution scaledRes, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderHelmetCaptured = true;
            ci.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true)
    private void patcher$captureRenderPortal(float timeInPortal, ScaledResolution scaledRes, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderPortalCapturedTicks = timeInPortal;
            ci.cancel();
        }
    }
}
