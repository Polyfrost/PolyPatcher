package club.sk1er.patcher.mixins.performance;

import club.sk1er.patcher.hooks.ResourceReloadHooks;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SimpleReloadableResourceManager.class)
public class SimpleReloadableResourceManager_OnlyRefreshNecessaryListeners {

    @Shadow
    @Final
    private List<IResourceManagerReloadListener> reloadListeners;

    @Inject(method = "registerReloadListener", at = @At("HEAD"))
    private void onReloadListenerRegister(IResourceManagerReloadListener reloadListener, CallbackInfo ci) {
        if (reloadListener instanceof LanguageManager) {
            ResourceReloadHooks.setLanguageManager(reloadListener);
        }
    }

    @Redirect(method = "notifyReloadListeners", at = @At(value = "FIELD", target = "Lnet/minecraft/client/resources/SimpleReloadableResourceManager;reloadListeners:Ljava/util/List;"))
    private List<IResourceManagerReloadListener> redirectNotifyReloadListeners(SimpleReloadableResourceManager simpleReloadableResourceManager) {
        if (ResourceReloadHooks.shouldLoadLanguage()) {
            return ResourceReloadHooks.getLanguageManager();
        } else {
            return reloadListeners;
        }
    }

    @Inject(method = "notifyReloadListeners", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/ProgressManager;pop(Lnet/minecraftforge/fml/common/ProgressManager$ProgressBar;)V"))
    private void callOptiFineLanguage(CallbackInfo ci) {
        if (ResourceReloadHooks.shouldLoadLanguage()) {
            ResourceReloadHooks.reloadOptiFineLanguage();
        }
    }
}
