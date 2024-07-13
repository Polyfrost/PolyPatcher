package club.sk1er.patcher.mixins.performance;

//#if MC==10809
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderChunk.class)
//#endif
public class RenderChunkMixin_OptimizeBeaconBeamRendering {
//#if MC==10809
    /**
     * Minecraft typically renders the beacon beam twice per frame. This is noticeable by comparing the opacity of the
     * beacon beam when the beacon is in view versus when it's out of view (fly 50 blocks above, for instance).
     * <p>
     * This fix is based off a Forge PR that was added to 1.11.x:
     * <a href="https://github.com/MinecraftForge/MinecraftForge/pull/3651/files">forge PR</a>
     */
    @Redirect(method = "rebuildChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/chunk/CompiledChunk;addTileEntity(Lnet/minecraft/tileentity/TileEntity;)V"))
    private void patcher$renderBeamOnce(CompiledChunk instance, TileEntity tileEntityIn) {
        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = TileEntityRendererDispatcher.instance.getSpecialRenderer(tileEntityIn);
        if (!tileentityspecialrenderer.forceTileEntityRender()) instance.addTileEntity(tileEntityIn);
    }
//#endif
}

