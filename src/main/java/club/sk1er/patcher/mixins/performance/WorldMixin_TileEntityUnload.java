package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.ducks.WorldExt;
import gnu.trove.TLongCollection;
import gnu.trove.set.hash.TLongHashSet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(World.class)
public class WorldMixin_TileEntityUnload implements WorldExt {
    //#if MC==10809
    @Shadow
    @Final
    public List<TileEntity> loadedTileEntityList;
    @Shadow
    @Final
    public List<TileEntity> tickableTileEntities;
    @Unique
    private TLongCollection patcher$tileEntitiesChunkToBeRemoved = new TLongHashSet(); // TODO: switch to fast util

    @Override
    public void patcher$markTileEntitiesInChunkForRemoval(Chunk chunk) {
        if (!chunk.getTileEntityMap().isEmpty()) {
            long pos = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);
            this.patcher$tileEntitiesChunkToBeRemoved.add(pos);
        }
    }

    @Inject(method = "updateEntities", at = @At(value = "FIELD", target = "Lnet/minecraft/world/World;processingLoadedTiles:Z", opcode = Opcodes.PUTFIELD, ordinal = 1))
    private void removeInUnloaded(CallbackInfo ci) {
        if (!this.patcher$tileEntitiesChunkToBeRemoved.isEmpty()) {
            java.util.function.Predicate<TileEntity> isInChunk = (tileEntity) -> {
                long tileChunkPos = ChunkCoordIntPair.chunkXZ2Int(tileEntity.getPos().getX() >> 4, tileEntity.getPos().getZ() >> 4);
                return this.patcher$tileEntitiesChunkToBeRemoved.contains(tileChunkPos);
            };
            java.util.function.Predicate<TileEntity> isInChunkDoUnload = (tileEntity) -> {
                boolean inChunk = isInChunk.test(tileEntity);
                if (inChunk)
                {
                    tileEntity.onChunkUnload();
                }
                return inChunk;
            };
            this.tickableTileEntities.removeIf(isInChunk);
            this.loadedTileEntityList.removeIf(isInChunkDoUnload);
            this.patcher$tileEntitiesChunkToBeRemoved.clear();
        }
    }
    //#endif
}
