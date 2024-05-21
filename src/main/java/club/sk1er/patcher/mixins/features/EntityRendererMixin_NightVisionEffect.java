package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_NightVisionEffect {
    @Inject(method = "getNightVisionBrightness", at = @At("HEAD"), cancellable = true)
    private void cleanerNightVision(EntityLivingBase entitylivingbaseIn, float partialTicks, CallbackInfoReturnable<Float> cir) {
        if (PatcherConfig.disableNightVision) {
            cir.setReturnValue(0F);
            return; // let's avoid checking for cleaner night vision if we're going to disable night vision anyway
        }
        if (PatcherConfig.cleanerNightVision) cir.setReturnValue(!(entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration() < 200) ? 1.0F : (float) entitylivingbaseIn.getActivePotionEffect(Potion.nightVision).getDuration() / 200F);
    }
}
