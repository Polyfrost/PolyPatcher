package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.config.PatcherConfig;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin_Fog {

    @ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0.01f))
    private float waterBreathing(float constant) {
        return constant * Math.min(1f, PatcherConfig.waterDensity / 100f);
    }

    @ModifyConstant(method = "setupFog", constant = @Constant(floatValue = 0.1f, ordinal = 2))
    private float water(float constant) {
        return constant * PatcherConfig.waterDensity / 100f;
    }

}
