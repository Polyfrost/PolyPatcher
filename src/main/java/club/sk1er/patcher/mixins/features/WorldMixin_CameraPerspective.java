package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Set;

@Mixin(World.class)
public abstract class WorldMixin_CameraPerspective {
    @Unique
    private static final Set<Block> patcher$ignoredBlocks = Sets.newHashSet(
        Blocks.glass,
        Blocks.stained_glass,
        Blocks.glass_pane,
        Blocks.stained_glass_pane,
        Blocks.iron_bars
    );

    @Redirect(method = "rayTraceBlocks(Lnet/minecraft/util/Vec3;Lnet/minecraft/util/Vec3;ZZZ)Lnet/minecraft/util/MovingObjectPosition;", at = @At(value = "INVOKE", target =
        //#if MC<=10809
        "Lnet/minecraft/block/Block;getCollisionBoundingBox(Lnet/minecraft/world/World;Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;)Lnet/minecraft/util/AxisAlignedBB;"
        //#else
        //$$ "Lnet/minecraft/block/state/IBlockState;getCollisionBoundingBox(Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/math/AxisAlignedBB;"
        //#endif
        , ordinal = 1))
    private AxisAlignedBB patcher$shouldCancel(
        //#if MC<=10809
        Block instance, World world, BlockPos blockPos, IBlockState iBlockState
        //#else
        //$$ IBlockState iBlockState, net.minecraft.world.IBlockAccess iBlockAccess, BlockPos blockPos
        //#endif
    ) {
        if (PatcherConfig.betterCamera && patcher$ignoredBlocks.contains(iBlockState.getBlock())) return null;
        return
            //#if MC<=10809
            instance.getCollisionBoundingBox(world, blockPos, iBlockState);
            //#else
            //$$ iBlockState.getCollisionBoundingBox(iBlockAccess, blockPos);
            //#endif
    }
}
