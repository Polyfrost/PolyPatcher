package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.hooks.TRSRTransformationHook;
//#if MC==10809
import net.minecraft.client.resources.model.ModelRotation;
//#endif
import net.minecraftforge.client.model.ForgeBlockStateV1;
//#if MC==10809
import net.minecraftforge.client.model.TRSRTransformation;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ForgeBlockStateV1.Variant.Deserializer.class, remap = false)
public class ForgeBlockStateV1Mixin_OptimizeTransforms {
    //#if MC==10809
    @Redirect(method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraftforge/client/model/ForgeBlockStateV1$Variant;", at = @At(value = "NEW", target = "net/minecraftforge/client/model/TRSRTransformation"))
    private TRSRTransformation patcher$from(ModelRotation rotation) {
        return TRSRTransformationHook.from(rotation);
    }
    //#endif
}
