package club.sk1er.patcher.mixins.performance.forge;

//#if MC==10809
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
//#endif

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.BitSet;

@Mixin(
    //#if MC==10809
    value = FMLControlledNamespacedRegistry.class, remap = false
    //#else
    //$$ net.minecraft.client.Minecraft.class
    //#endif
)
public class FMLControlledNamespacedRegistryMixin_TrimAvailabilityMap {
    //#if MC==10809
    @Shadow @Final private BitSet availabilityMap;

    @ModifyArg(method = "<init>(Lnet/minecraft/util/ResourceLocation;IILjava/lang/Class;ZLnet/minecraftforge/fml/common/registry/FMLControlledNamespacedRegistry$AddCallback;)V", at = @At(value = "NEW", target = "Ljava/util/BitSet;<init>(I)V"), index = 0)
    public int noPreAllocate(int nbits) {
        return Math.min(nbits, 0xFFFF);
    }

    @Inject(method = "validateContent", at = @At("HEAD"))
    public void trimToSize(ResourceLocation registryName, CallbackInfo ci) {
        try {
            ReflectionHelper.findMethod(BitSet.class, this.availabilityMap, new String[]{"trimToSize"}).invoke(this.availabilityMap);
        } catch (Exception ignored) {

        }
    }
    //#endif
}
