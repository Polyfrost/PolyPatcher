package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.projectile.EntityFishHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFish.class)
public class RenderFishMixin_CleanProjectiles {

    @Inject(method = "doRender(Lnet/minecraft/entity/projectile/EntityFishHook;DDDFF)V", at = @At("HEAD"), cancellable = true)
    public void patcher$cleanProjectiles(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.cleanProjectiles && entity.ticksExisted < 2) ci.cancel();
    }

}
