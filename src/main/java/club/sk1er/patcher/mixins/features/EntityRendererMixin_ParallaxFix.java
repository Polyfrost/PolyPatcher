package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.DebugCrosshairHook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class, priority = 1001)
public abstract class EntityRendererMixin_ParallaxFix {
    @Shadow
    public abstract void setupOverlayRendering();

    @Shadow
    private Minecraft mc;

    @Unique
    private boolean canDraw = false;

    //#if MC==10809
    @ModifyConstant(method = "orientCamera", constant = @Constant(floatValue = -0.1F))
    private float patcher$modifyParallax(float original) {
        return PatcherConfig.parallaxFix ? 0.05F : original;
    }

    @Inject(method = "renderWorldDirections", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getRenderViewEntity()Lnet/minecraft/entity/Entity;"), cancellable = true)
    private void cancel(float partialTicks, CallbackInfo ci) {
        if (!PatcherConfig.parallaxFix) return;
        canDraw = true;
        ci.cancel();
    }

    @Inject(method = "updateCameraAndRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;renderGameOverlay(F)V"))
    private void drawDebugCrosshair(float partialTicks, long nanoTime, CallbackInfo ci) {
        if (canDraw) {
            setupOverlayRendering();
            DebugCrosshairHook.renderDirections(partialTicks, this.mc);
        }
    }

    @Inject(method = "updateCameraAndRender", at = @At("TAIL"))
    private void resetState(float partialTicks, long nanoTime, CallbackInfo ci) {
        canDraw = false;
    }
    //#endif
}
