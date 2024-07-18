package club.sk1er.patcher.mixins.performance.hudcaching;

import club.sk1er.patcher.screen.render.caching.HUDCaching;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin_HUDCaching {

    @Inject(method = "renderGameOverlay", at = @At("HEAD"))
    private void patcher$resetCaptures(float partialTicks, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderVignetteCaptured = false;
            HUDCaching.renderHelmetCaptured = false;
            HUDCaching.renderPortalCapturedTicks = -1;
            HUDCaching.renderCrosshairsCaptured = false;
        }
    }

    @Inject(method = "renderCrosshairs", at = @At("HEAD"), cancellable = true, remap = false)
    private void patcher$captureRenderCrosshairs(
        //#if MC==10809
        int width, int height,
        //#else
        //$$ float partialTicks,
        //#endif
        CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderCrosshairsCaptured = true;
            ci.cancel();
        }
    }

    @Inject(method = "renderHelmet", at = @At("HEAD"), cancellable = true, remap = false)
    private void patcher$captureRenderHelmet(ScaledResolution res, float partialTicks, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderHelmetCaptured = true;
            ci.cancel();
        }
    }

    @Inject(method = "renderPortal", at = @At("HEAD"), cancellable = true, remap = false)
    private void patcher$captureRenderPortal(ScaledResolution res, float partialTicks, CallbackInfo ci) {
        if (HUDCaching.renderingCacheOverride) {
            HUDCaching.renderPortalCapturedTicks = partialTicks;
            ci.cancel();
        }
    }
}
