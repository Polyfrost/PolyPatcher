package club.sk1er.patcher.mixins.performance.forge;

import club.sk1er.patcher.config.PatcherConfig;
import me.kbrewster.eventbus.forge.KEventBus;
import me.kbrewster.eventbus.forge.invokers.LMFInvoker;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EventBus.class, remap = false, priority = 1004)
public class EventBusMixin_UseKeventBus {
    @Unique
    private final KEventBus patcher$kEventBus = new KEventBus(new LMFInvoker(), e -> System.err.println("An exception occurred in a method: " + e.getMessage()));

    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    private void patcher$registerKevent(Object target, CallbackInfo ci) {
        if (PatcherConfig.actuallyReplaceForgeEventBus) {
            patcher$kEventBus.register(target);
            ci.cancel();
        }
    }

    @Inject(method = "unregister", at = @At("HEAD"), cancellable = true)
    private void patcher$unregister(Object target, CallbackInfo ci) {
        if (PatcherConfig.actuallyReplaceForgeEventBus) {
            patcher$kEventBus.unregister(target);
            ci.cancel();
        }
    }

    @Inject(method = "post", at = @At("HEAD"), cancellable = true)
    private void patcher$post(Event event, CallbackInfoReturnable<Boolean> cir) {
        if (PatcherConfig.actuallyReplaceForgeEventBus) {
            patcher$kEventBus.post(event);
            cir.setReturnValue(event.isCancelable() && event.isCanceled());
        }
    }
}
