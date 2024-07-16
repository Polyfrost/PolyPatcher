package club.sk1er.patcher.ducks;

import net.minecraft.world.chunk.Chunk;

public interface WorldExt {
    void patcher$markTileEntitiesInChunkForRemoval(Chunk chunk);
}
