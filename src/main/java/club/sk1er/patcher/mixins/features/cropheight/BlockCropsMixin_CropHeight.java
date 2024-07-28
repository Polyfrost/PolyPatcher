package club.sk1er.patcher.mixins.features.cropheight;

import org.polyfrost.oneconfig.api.hypixel.v0.HypixelUtils;
import org.polyfrost.universal.UMinecraft;
import club.sk1er.patcher.config.PatcherConfig;
import club.sk1er.patcher.hooks.CropUtilities;
import net.minecraft.block.BlockCrops;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockCrops.class)
public abstract class BlockCropsMixin_CropHeight extends BlockMixin_CropHitbox {

    //#if MC==10809
    @Override
    public void getSelectedBoundingBox(World worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if (PatcherConfig.futureHitBoxes && (HypixelUtils.isHypixel() || UMinecraft.getMinecraft().isIntegratedServerRunning())) {
            CropUtilities.updateCropsMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        }
    }

    @Override
    public void collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end, CallbackInfoReturnable<MovingObjectPosition> cir) {
        if (PatcherConfig.futureHitBoxes && (HypixelUtils.isHypixel() || UMinecraft.getMinecraft().isIntegratedServerRunning())) {
            CropUtilities.updateCropsMaxY(worldIn, pos, worldIn.getBlockState(pos).getBlock());
        }
    }
    //#endif
}
