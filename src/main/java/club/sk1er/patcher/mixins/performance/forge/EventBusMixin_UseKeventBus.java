package club.sk1er.patcher.mixins.performance.forge;

import com.google.common.collect.MapMaker;
import me.kbrewster.eventbus.forge.KEventBus;
import me.kbrewster.eventbus.forge.invokers.LMFInvoker;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Mixin(value = EventBus.class, remap = false)
public class EventBusMixin_UseKeventBus {
    @Unique
    private final ConcurrentHashMap<Object, ArrayList<IEventListener>> patcher$listeners = new ConcurrentHashMap<>();
    @Unique
    private final Map<Object, ModContainer> patcher$listenerOwners = new MapMaker().weakKeys().weakValues().makeMap();
    @Unique
    private final KEventBus patcher$kEventBus = new KEventBus(new LMFInvoker(), e -> System.err.println("An exception occurred in a method: " + e.getMessage()));

    @Redirect(method = "<init>(Lnet/minecraftforge/fml/common/eventhandler/IEventExceptionHandler;)V", at = @At(value = "INVOKE", target = "Lcom/google/common/base/Preconditions;checkArgument(ZLjava/lang/Object;)V"))
    private void patcher$skipCheck(boolean expression, Object errorMessage) {
        // No-op
    }

    @Inject(method = "register(Ljava/lang/Object;)V", at = @At("HEAD"), cancellable = true)
    private void patcher$registerKevent(Object target, CallbackInfo ci) {
        patcher$kEventBus.register(target);
        ci.cancel();
    }

    @Inject(method = "unregister", at = @At("HEAD"), cancellable = true)
    private void patcher$unregister(Object target, CallbackInfo ci) {
        patcher$kEventBus.unregister(target);
        ci.cancel();
    }

    @Inject(method = "post", at = @At("HEAD"), cancellable = true)
    private void patcher$post(Event event, CallbackInfoReturnable<Boolean> cir) {
        patcher$kEventBus.post(event);
        cir.setReturnValue(event.isCancelable() && event.isCanceled());
    }
}
