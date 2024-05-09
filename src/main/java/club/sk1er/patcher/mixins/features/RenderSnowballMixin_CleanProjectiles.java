package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderSnowball.class)
public class RenderSnowballMixin_CleanProjectiles<T extends Entity> {

    @Inject(method = "doRender", at = @At("HEAD"), cancellable = true)
    public void patcher$cleanProjectiles(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.cleanProjectiles && entity.ticksExisted < 2) ci.cancel();
    }

}
