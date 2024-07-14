package club.sk1er.patcher.mixins.bugfixes;

//#if MC==10809
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderGlobal.class)
//#endif
public class RenderGlobalMixin_RenderOobEntities {
    //#if MC==10809
    @Redirect(method = "setupTerrain", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/chunk/RenderChunk;boundingBox:Lnet/minecraft/util/AxisAlignedBB;", opcode = Opcodes.GETFIELD))
    private AxisAlignedBB patcher$fixOobEntityRendering(RenderChunk instance, Entity viewEntity, double partialTicks, ICamera camera, int frameCount, boolean playerSpectator) {
        double d3 = viewEntity.lastTickPosX + (viewEntity.posX - viewEntity.lastTickPosX) * partialTicks;
        double d4 = viewEntity.lastTickPosY + (viewEntity.posY - viewEntity.lastTickPosY) * partialTicks;
        double d5 = viewEntity.lastTickPosZ + (viewEntity.posZ - viewEntity.lastTickPosZ) * partialTicks;
        BlockPos blockpos1 = new BlockPos(d3, d4 + (double)viewEntity.getEyeHeight(), d5);
        return instance.boundingBox.addCoord(0.0, blockpos1.getY() > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0.0);
    }
    //#endif
}
