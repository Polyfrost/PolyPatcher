package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.TRSRTransformationHook;
import net.minecraft.client.renderer.block.model.ItemTransformVec3f;
import net.minecraftforge.client.ForgeHooksClient;
//#if MC==10809
import net.minecraftforge.client.model.TRSRTransformation;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeHooksClient.class, remap = false)
public abstract class ForgeHooksClientMixin_OptimizeTransforms {
    //#if MC==10809
    @SuppressWarnings("deprecation")
    @Redirect(method = "applyTransform(Lnet/minecraft/client/renderer/block/model/ItemTransformVec3f;Lcom/google/common/base/Optional;)Lcom/google/common/base/Optional;", at = @At(value = "NEW", target = "net/minecraftforge/client/model/TRSRTransformation"))
    private static TRSRTransformation patcher$from(ItemTransformVec3f transform) {
        return TRSRTransformationHook.from(transform);
    }
    //#endif
}
