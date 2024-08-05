package club.sk1er.patcher.mixins.performance;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TileEntityRendererDispatcher.class, priority = 100)
public abstract class TileEntityRendererDispatcherMixin_RemoveInvalidEntities {

    //#if MC==10809
    @Shadow
    public abstract <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> teClass);

    /**
     * @author MicrocontrollersDev and Wyvest
     * @reason Remove invalid tile entities from being rendered
     */
    @Overwrite
    private <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn) {
        return tileEntityIn != null && !tileEntityIn.isInvalid() ? this.getSpecialRendererByClass(tileEntityIn.getClass()) : null;
    }
    //#endif
}
