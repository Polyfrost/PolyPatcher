package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.util.fov.FovHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_FovModifier {

    @ModifyReturnValue(method = "getFOVModifier", at = @At("RETURN"))
    private float patcher$modifyFov(float original, @Local(ordinal = 0) Block block) {
        return FovHandler.INSTANCE.onFovModifierChange(block, original);
    }
}
