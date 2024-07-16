package club.sk1er.patcher.mixins.performance;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin_RemoveInvalidEntities {
    //#if MC==10809
    @Inject(method = "getSpecialRenderer", at = @At("HEAD"), cancellable = true)
    private <T extends TileEntity> void patcher$returnNullIfInvalid(TileEntity tileEntityIn, CallbackInfoReturnable<TileEntitySpecialRenderer<T>> cir) {
        if (tileEntityIn == null || tileEntityIn.isInvalid()) {
            cir.setReturnValue(null);
        }
    }
    //#endif
}
