package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityRendererDispatcher.class)
public class TileEntityRendererDispatcherMixin_BatchDraw {
    //#if MC==10809
    @Unique private boolean patcher$drawingBatch = false;

    @Inject(method = "preDrawBatch", at = @At(value = "TAIL"), remap = false)
    private void patcher$onPreDrawBatch(CallbackInfo ci) {
        patcher$drawingBatch = true;
    }

    @Inject(method = "drawBatch", at = @At(value = "TAIL"), remap = false)
    private void patcher$onDrawBatch(CallbackInfo ci) {
        patcher$drawingBatch = false;
    }

    @Redirect(method = "renderTileEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;hasFastRenderer()Z"))
    private boolean patcher$renderTileEntity(TileEntity instance) {
        return (!PatcherConfig.batchModelRendering || patcher$drawingBatch) && instance.hasFastRenderer();
    }

    @Redirect(method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntity;DDDFI)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;hasFastRenderer()Z"))
    private boolean patcher$renderTileEntityAt(TileEntity instance) {
        return (!PatcherConfig.batchModelRendering || patcher$drawingBatch) && instance.hasFastRenderer();
    }
    //#endif
}
