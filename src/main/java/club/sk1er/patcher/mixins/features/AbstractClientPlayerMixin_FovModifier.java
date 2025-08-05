package club.sk1er.patcher.mixins.features;

import club.sk1er.patcher.util.fov.FovHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin_FovModifier {

    @ModifyReturnValue(method = "getFovModifier", at = @At("RETURN"))
    private float patcher$modifyFov(float original) {
        return FovHandler.INSTANCE.onFovChange((AbstractClientPlayer) (Object) this, original);
    }
}
