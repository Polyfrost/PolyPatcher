package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_DamageTilt {

    @Inject(method = "hurtCameraEffect", at = @At(value = "HEAD"), cancellable = true)
    public void patcher$hurtCameraEffect(float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.removeDamageTilt) {
            ci.cancel();
        }
    }
}
