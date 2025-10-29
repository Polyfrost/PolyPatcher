package club.sk1er.patcher.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_AdjustEyeHeightLighting {

    //#if MC==10809
    @Shadow
    private Minecraft mc;

    @ModifyArg(method = "updateRenderer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/WorldClient;getLightBrightness(Lnet/minecraft/util/BlockPos;)F"))
    private BlockPos patcher$accountForEyes(BlockPos par1) {
        return new BlockPos(this.mc.getRenderViewEntity().getPositionEyes(1.0F));
    }
    //#endif

}
