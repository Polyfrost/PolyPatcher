package club.sk1er.patcher.mixins.features.lefthand;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin_LeftHandedness {
    //#if MC==10809
    @ModifyConstant(method = "rotateWithPlayerRotations", constant = @Constant(floatValue = 1f, ordinal = 1))
    private float leftHandRotate(float constant) {
        return PatcherConfig.leftHandInFirstPerson ? -constant : constant;
    }
    //#endif
}
