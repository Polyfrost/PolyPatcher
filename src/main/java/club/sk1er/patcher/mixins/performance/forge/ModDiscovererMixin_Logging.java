package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.util.forge.EntrypointCaching;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.discovery.ModDiscoverer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ModDiscoverer.class, remap = false)
public class ModDiscovererMixin_Logging {
    @Unique
    private long patcher$time;

    @Inject(method = "identifyMods", at = @At("HEAD"))
    private void patcher$logStart(CallbackInfoReturnable<List<ModContainer>> cir) {
        patcher$time = System.currentTimeMillis();
    }

    @Inject(method = "identifyMods", at = @At("TAIL"))
    private void patcher$logEnd(CallbackInfoReturnable<List<ModContainer>> cir) {
        EntrypointCaching.INSTANCE.logger.info("ModDiscoverer.identifyMods took " + (System.currentTimeMillis() - patcher$time) + "ms");
    }
}
