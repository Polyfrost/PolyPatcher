package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiIngameForge.class)
public class GuiIngameForgeMixin_ActionbarText {

    @Shadow(remap = false)
    public static int left_height;

    @ModifyArg(
        method = "renderRecordOverlay",
        remap = false,
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", remap = true),
        index = 1
    )
    private float patcher$fixOverlappingActionbarText(float y) {
        return PatcherConfig.fixActionbarOverlap && 68 < left_height ? y + 68f - left_height : y;
    }

}
