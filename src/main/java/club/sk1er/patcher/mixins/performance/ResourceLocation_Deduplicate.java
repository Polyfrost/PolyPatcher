package club.sk1er.patcher.mixins.performance;

import me.jellysquid.mods.hydrogen.common.dedup.IdentifierCaches;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * From <a href="https://github.com/CaffeineMC/hydrogen-fabric/blob/95da36046c2f55617a76ebb8ea1e1f2a330330e5/src/main/java/me/jellysquid/mods/hydrogen/mixin/util/MixinIdentifier.java">CaffeineMC/hydrogen-fabric</a> under GNU LGPL v3.0
 */
@Mixin(ResourceLocation.class)
public class ResourceLocation_Deduplicate {
    @Shadow
    @Final
    @Mutable
    protected String resourceDomain;

    @Shadow
    @Final
    @Mutable
    protected String resourcePath;

    @Inject(method = "<init>(I[Ljava/lang/String;)V", at = @At("RETURN"))
    private void reinit(int what, String[] id, CallbackInfo ci) {
        this.resourceDomain = IdentifierCaches.NAMESPACES.deduplicate(this.resourceDomain);
        this.resourcePath = IdentifierCaches.PATH.deduplicate(this.resourcePath);
    }
}
