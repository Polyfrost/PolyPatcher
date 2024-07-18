package club.sk1er.patcher.mixins.performance.forge;

//#if MC==10809
import net.minecraftforge.client.model.TRSRTransformation;
//#endif
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(
    //#if MC==10809
    value = TRSRTransformation.class, remap = false
    //#else
    //$$ value = net.minecraft.client.Minecraft.class
    //#endif
)
public class TRSRTransformationMixin_OptimizeTransforms {
    //#if MC==10809
    @Shadow
    @Final
    private static TRSRTransformation identity;

    @Inject(method = "compose", at = @At("HEAD"), cancellable = true)
    private void patcher$earlyExitCompose(TRSRTransformation b, CallbackInfoReturnable<TRSRTransformation> cir) {
        TRSRTransformation thiz = (TRSRTransformation) (Object) this;
        if (thiz == identity) {
            cir.setReturnValue(b);
        } else if (b == identity) {
            cir.setReturnValue(thiz);
        }
    }

    @Inject(method = "blockCenterToCorner", at = @At("HEAD"), cancellable = true)
    private static void patcher$earlyExitBlockCenter(TRSRTransformation transform, CallbackInfoReturnable<TRSRTransformation> cir) {
        if (transform == identity) {
            cir.setReturnValue(transform);
        }
    }

    @Inject(method = "blockCornerToCenter", at = @At("HEAD"), cancellable = true)
    private static void patcher$earlyExitBlockCorner(TRSRTransformation transform, CallbackInfoReturnable<TRSRTransformation> cir) {
        if (transform == identity) {
            cir.setReturnValue(transform);
        }
    }
    //#endif
}
