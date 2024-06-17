package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.entity.RenderFallingBlock;
import net.minecraft.entity.item.EntityFallingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderFallingBlock.class)
public class RenderFallingBlockMixin_CancelRender {
    @Inject(method = "doRender(Lnet/minecraft/entity/item/EntityFallingBlock;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void patcher$cancelRendering(EntityFallingBlock entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (PatcherConfig.disableFallingBlocks) ci.cancel();
    }

}
