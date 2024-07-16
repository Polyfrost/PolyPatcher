package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.ducks.WorldExt;
import com.google.common.collect.Iterators;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Iterator;

@Mixin(Chunk.class)
public class ChunkMixin_TileEntityUnload {
    //#if MC==10809
    @Shadow
    @Final
    private World worldObj;

    @Redirect(method = "onChunkUnload", at = @At(value = "INVOKE", target = "Ljava/util/Collection;iterator()Ljava/util/Iterator;", ordinal = 0))
    private Iterator patcher$unloadTileEntity(Collection instance) {
        ((WorldExt) this.worldObj).patcher$markTileEntitiesInChunkForRemoval((Chunk)(Object)this);
        return Iterators.emptyIterator();
    }
    //#endif
}
